package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

/**
 * Do not use as anonymous classes!
 *
 * The {@code PApplet} you passed into your
 * {@code SceneManager} is what you get! :)
 */
public class Scene implements HasSketchEvents {
  // region `private` / `protected` fields.
  protected final Scene SCENE = this;
  protected final Sketch SKETCH;

  // Would've used a `LinkedHashSet`, but am using `ArrayList`s instead since
  // duplicates won't be allowed.
  private final ArrayList<Layer> LAYERS = new ArrayList<>();
  private final HashMap<Class<? extends Layer>, Constructor<? extends Layer>> LAYER_CONSTRUCTORS;
  /*
   * Alternative approach: storing a reference to the constructor WITHIN the class
   * extending `Layer` itself.
   *
   * ...won't work!
   *
   * Why?
   * - Can't make it [`public`, and] `static`! That'd mean that every subclass of
   * `Layer` would have to provide this field. Making it `public` would also make
   * it accessible everywhere before calling the constructor - not API-friendly!
   * Won't simply declare it `private` and quietly use reflection for access to it
   * either, since that would be slow, and would mean that everybody would have
   * access to it anyway. Let's never consider reflection an option!
   *
   * - Any instance of a layer WILL re-initialize the constructor reference. This
   * can be avoided by making the reference `static` (and also `private`, making
   * it accessible only through a method checking for a `LayerKey`
   * argument, since constructors cannot return anything), but we've already
   * decided not to do that.
   */

  /* private */ protected final SceneManager MANAGER; // ~~Don't let the scene manage its `manager`!~~
  private final LayerKey LAYER_INITIALIZER; // Don't let the scene manage its `manager`!
  // endregion

  public class LayerKey {
    private final Scene SCENE;
    private final Sketch SKETCH;

    private LayerKey(Scene p_scene, Sketch p_sketch) {
      this.SCENE = p_scene;
      this.SKETCH = p_sketch;
    }

    public Scene getScene() {
      return this.SCENE;
    }

    public Sketch getSketch() {
      return this.SKETCH;
    }

  }

  public Scene(SceneKey p_sceneKey) {
    // this.SCENE_CLASS = p_sceneKey.getSceneClass();
    this.MANAGER = p_sceneKey.getSceneManager();
    this.SKETCH = this.MANAGER.getSketch();

    this.LAYER_CONSTRUCTORS = new HashMap<>();
    this.LAYER_INITIALIZER = new LayerKey(this, this.SKETCH);
  }

  @SafeVarargs
  public Scene(SceneKey p_sceneKey, Class<? extends Layer>... p_layerClasses) {
    this(p_sceneKey);

    for (Class<? extends Layer> c : p_layerClasses) {
      this.startLayer(c);
    }
  }

  // region `Layer`-operations.
  // They get a running `Layer`'s reference from its (given) class.
  public Layer getLayer(Class<? extends Layer> p_layerClass) {
    for (Layer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass))
        return l;
    return null; // Also does the work for:
    // if (!this.hasLayer(p_layerClass))
    // return null;
  }

  public HashSet<Layer> getAllLayers(Class<? extends Layer> p_layerClass) {
    // Nobody's gunna do stuff like this. Ugh.
    // No matter what I do, its still gunna crash their program.

    // if (!this.hasLayerOfClass(p_layerClass))
    // return null;

    HashSet<Layer> toRet = new HashSet<>();

    for (Layer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass)) {
        toRet.add(l);
      }

    return toRet;
  }

  @SafeVarargs
  public final void startAllLayers(Class<? extends Layer>... p_layerClasses) {
    for (Class<? extends Layer> c : p_layerClasses) {
      this.startLayer(c);
    }
  }

  public void startLayer(Class<? extends Layer> p_layerClass) {
    if (p_layerClass == null)
      throw new NullPointerException("""
          You weren't supposed to pass `null` into `Scene::startLayer()`.""");

    if (this.hasLayerOfClass(p_layerClass))
      return;

    Layer toStart = null;
    Constructor<? extends Layer> layerConstructor = null;

    // region Getting the constructor.
    try {
      layerConstructor = p_layerClass.getConstructor(Scene.LayerKey.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    // endregion

    // region Constructing the `Layer`.
    try {
      toStart = (Layer) layerConstructor.newInstance(this.LAYER_INITIALIZER);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    // endregion

    // Don't worry about concurrency, vvv *these* vvv are `final`! ^-^
    this.LAYERS.add(toStart);
    this.LAYER_CONSTRUCTORS.put(p_layerClass, layerConstructor);
  }

  /**
   * This method gives the user the freedoms such as changing
   * layer rendering order.
   */
  public ArrayList<Layer> getAllLayers() {
    return this.LAYERS;
  }

  public boolean hasLayerOfClass(Class<? extends Layer> p_layerClass) {
    return this.LAYER_CONSTRUCTORS.keySet().contains(p_layerClass);
    // for (Class<? extends Layer> c :
    // this.LAYER_CONSTRUCTORS.keySet().contains(p_layerClass))
    // if (c.equals(p_layerClass))
    // return true;
    // return false;
  }

  public void restartLayer(int p_layerId) {
    Class<? extends Layer> layerClass = this.LAYERS.get(p_layerId).getClass();

    if (!this.hasLayerOfClass(layerClass))
      throw new IllegalArgumentException("This scene owns no such `Layer`.");

    Layer toStart = null;

    // region Re-construct layer.
    try {
      toStart = this.LAYER_CONSTRUCTORS.get(layerClass).newInstance(this.LAYER_INITIALIZER);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    // endregion

    this.LAYERS.set(p_layerId, toStart);
    toStart.setup();
  }
  // endregion

  // region Anything callback-related, LOL.
  // region `SceneManager.SceneInitializer` app-workflow callback runners.
  private void verifyInitializer(SceneManager.SceneKey p_sceneKey) {
    if (p_sceneKey == null)
      throw new IllegalArgumentException(
          "`Scene::run()` should only be called by a `SceneManager`!");
  }

  public void runOnSceneExit(SceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.onSceneExit();
  }

  public void runSetup(SceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.setup();

    for (Layer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.setup();
  }

  public void runPre(SceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.pre();

    for (Layer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.pre();
  }

  public void runDraw(SceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    for (Layer l : this.LAYERS)
      if (l != null)
        if (l.isActive()) {
          this.SKETCH.pushMatrix();
          this.SKETCH.pushStyle();
          l.draw();
          this.SKETCH.popStyle();
          this.SKETCH.popMatrix();
        }

    this.SKETCH.pushMatrix();
    this.SKETCH.pushStyle();
    this.draw();
    this.SKETCH.popStyle();
    this.SKETCH.popMatrix();
  }

  public void runPost(SceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    for (Layer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.post();

    this.post();
  }
  // endregion

  // region Scene callbacks.
  protected void onSceneExit() {
  }
  // endregion

  // region App workflow callbacks.
  /**
   * {@code Scene::setup()} is called first,
   * {@code Layer::setup()} is called for each {@linkplain Layer}, later.
   */
  protected void setup() {
  }

  /**
   * {@code Scene::pre()} is called first,
   * {@code Layer::pre()} is called for each {@linkplain Layer}, later.
   */
  protected void pre() {
  }

  /**
   * {@code Layer::draw()} is called for each {@linkplain Layer}, first.
   * {@code Scene::draw()} is called later.
   */
  protected void draw() {
  }

  /**
   * {@code Layer::draw()} is called for each {@linkplain Layer}, first.
   * {@code Scene::draw()} is called later.
   */
  protected void post() {
  }
  // endregion
  // endregion

}
