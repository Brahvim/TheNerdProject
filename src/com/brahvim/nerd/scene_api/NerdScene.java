package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;

/**
 * Do not use as anonymous classes!
 *
 * The {@code PApplet} you passed into your
 * {@code SceneManager} is what you get! :)
 */
public class NerdScene implements HasSketchEvents {

  // region [DEPRECATED] `AutoDrawable` stuff!
  /*
   * public NerdScene.AutoDrawable
   * createAutoDrawable(NerdScene.AutoDrawableInstance p_instance) {
   * NerdScene.AutoDrawable toRet = this.new AutoDrawable() {
   * 
   * @Override
   * public void draw() {
   * p_instance.draw();
   * }
   * };
   * 
   * return toRet;
   * }
   * 
   * public interface AutoDrawableInstance {
   * public void draw();
   * }
   * 
   * public abstract class AutoDrawable {
   * private static final ArrayList<AutoDrawable> ALL_AUTO_DRAWABLES = new
   * ArrayList<>(5);
   * 
   * public AutoDrawable() {
   * NerdScene.AutoDrawable.ALL_AUTO_DRAWABLES.add(this);
   * }
   * 
   * public void draw() {
   * }
   * }
   */
  // endregion

  // region `private` / `protected` fields.
  protected final NerdScene SCENE = this;
  protected final Sketch SKETCH;

  // Would've used a `LinkedHashSet`, but am using `ArrayList`s instead since
  // duplicates won't be allowed.
  private final ArrayList<NerdLayer> LAYERS = new ArrayList<>(2);
  private final HashMap<Class<? extends NerdLayer>, Constructor<? extends NerdLayer>> LAYER_CONSTRUCTORS;
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

  /* private */ protected final NerdSceneManager MANAGER; // ~~Don't let the scene manage its `manager`!~~
  private final LayerKey LAYER_INITIALIZER; // Don't let the scene manage its `manager`!
  public final NerdAssetManager ASSETS;
  // endregion

  public class LayerKey extends NerdKey {
    private final NerdScene SCENE;
    private final Sketch SKETCH;

    private LayerKey(NerdScene p_scene, Sketch p_sketch) {
      this.SCENE = p_scene;
      this.SKETCH = p_sketch;
    }

    public NerdScene getScene() {
      return this.SCENE;
    }

    public Sketch getSketch() {
      return this.SKETCH;
    }

    @Override
    public boolean fitsLock(Class<?> p_class) {
      return this.SCENE.getClass().equals(p_class);
    }

  }

  public NerdScene(SceneKey p_sceneKey) {
    // this.SCENE_CLASS = p_sceneKey.getSceneClass();
    this.MANAGER = p_sceneKey.getSceneManager();
    this.SKETCH = this.MANAGER.getSketch();
    this.ASSETS = new NerdAssetManager(SKETCH);

    this.LAYER_CONSTRUCTORS = new HashMap<>();
    this.LAYER_INITIALIZER = new LayerKey(this, this.SKETCH);
  }

  @SafeVarargs
  public NerdScene(SceneKey p_sceneKey, Class<? extends NerdLayer>... p_layerClasses) {
    this(p_sceneKey);

    for (Class<? extends NerdLayer> c : p_layerClasses) {
      this.startLayer(c);
    }
  }

  public Sketch getSketch() {
    return this.SKETCH;
  }

  // region `Layer`-operations.
  // They get a running `Layer`'s reference from its (given) class.
  public NerdLayer getLayer(Class<? extends NerdLayer> p_layerClass) {
    for (NerdLayer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass))
        return l;
    return null; // Also does the work for:
    // if (!this.hasLayer(p_layerClass))
    // return null;
  }

  public HashSet<NerdLayer> getAllLayers(Class<? extends NerdLayer> p_layerClass) {
    // Nobody's gunna do stuff like this. Ugh.
    // No matter what I do, its still gunna crash their program.

    // if (!this.hasLayerOfClass(p_layerClass))
    // return null;

    HashSet<NerdLayer> toRet = new HashSet<>();

    for (NerdLayer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass)) {
        toRet.add(l);
      }

    return toRet;
  }

  @SafeVarargs
  public final void startAllLayers(Class<? extends NerdLayer>... p_layerClasses) {
    for (Class<? extends NerdLayer> c : p_layerClasses) {
      this.startLayer(c);
    }
  }

  public void startLayer(Class<? extends NerdLayer> p_layerClass) {
    if (p_layerClass == null)
      throw new NullPointerException("""
          You weren't supposed to pass `null` into `Scene::startLayer()`.""");

    if (this.hasLayerOfClass(p_layerClass))
      return;

    NerdLayer toStart = null;
    Constructor<? extends NerdLayer> layerConstructor = null;

    // region Getting the constructor.
    try {
      layerConstructor = p_layerClass.getConstructor(NerdScene.LayerKey.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    // endregion

    // region Constructing the `Layer`.
    try {
      toStart = (NerdLayer) layerConstructor.newInstance(this.LAYER_INITIALIZER);
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
  public ArrayList<NerdLayer> getAllLayers() {
    return this.LAYERS;
  }

  public boolean hasLayerOfClass(Class<? extends NerdLayer> p_layerClass) {
    return this.LAYER_CONSTRUCTORS.keySet().contains(p_layerClass);
    // for (Class<? extends Layer> c :
    // this.LAYER_CONSTRUCTORS.keySet().contains(p_layerClass))
    // if (c.equals(p_layerClass))
    // return true;
    // return false;
  }

  public void restartLayer(int p_layerId) {
    Class<? extends NerdLayer> layerClass = this.LAYERS.get(p_layerId).getClass();

    if (!this.hasLayerOfClass(layerClass))
      throw new IllegalArgumentException("This scene owns no such `Layer`.");

    NerdLayer toStart = null;

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
  private void verifyInitializer(NerdSceneManager.SceneKey p_sceneKey) {
    if (p_sceneKey == null)
      throw new IllegalArgumentException(
          "`Scene::run()` should only be called by a `SceneManager`!");
  }

  public void runOnSceneExit(NerdSceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.onSceneExit();
  }

  public void runSetup(NerdSceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.setup();

    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.setup();
  }

  public void runPre(NerdSceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    this.pre();

    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.pre();
  }

  public void runDraw(NerdSceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    for (NerdLayer l : this.LAYERS)
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

  public void runPost(NerdSceneManager.SceneKey p_sceneKey) {
    this.verifyInitializer(p_sceneKey);
    for (NerdLayer l : this.LAYERS)
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
   * {@code Layer::setup()} is called for each {@linkplain NerdLayer}, later.
   */
  protected void setup() {
  }

  /**
   * {@code Scene::pre()} is called first,
   * {@code Layer::pre()} is called for each {@linkplain NerdLayer}, later.
   */
  protected void pre() {
  }

  /**
   * {@code Layer::draw()} is called for each {@linkplain NerdLayer}, first.
   * {@code Scene::draw()} is called later.
   */
  protected void draw() {
  }

  /**
   * {@code Layer::draw()} is called for each {@linkplain NerdLayer}, first.
   * {@code Scene::draw()} is called later.
   */
  protected void post() {
  }
  // endregion
  // endregion

}
