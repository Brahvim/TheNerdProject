package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.asset_loader.AssetManKey;
import com.brahvim.nerd.io.asset_loader.AssetManager;
import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

/**
 * Do not use as anonymous classes!
 *
 * The {@code PApplet} you passed into your
 * {@code SceneManager} is what you get! :)
 */
public class NerdScene implements HasSketchEvents {
  // region Inner classes
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

  public class LayerKey extends NerdKey {
    private final NerdScene SCENE;
    private final Sketch SKETCH;
    private final Class<? extends NerdLayer> LAYER_CLASS;

    private LayerKey(NerdScene p_scene, Sketch p_sketch, Class<? extends NerdLayer> p_layerClass) {
      this.LAYER_CLASS = p_layerClass;
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
    public boolean isFor(Class<?> p_class) {
      return this.LAYER_CLASS.equals(p_class);
    }

  }
  // endregion

  public final AssetManager ASSETS;

  // region `private` / `protected` fields.
  protected final NerdScene SCENE = this;
  protected final Sketch SKETCH;

  // ~~Don't let the scene manage its `manager`!:~~
  /* private */ protected final SceneManager MANAGER;
  private final AssetManKey ASSET_MAN_KEY;
  // Would've used a `LinkedHashSet`, but am using `ArrayList`s instead since
  // duplicates won't be allowed.
  private final ArrayList<NerdLayer> LAYERS = new ArrayList<>(2);
  private final HashMap<Class<? extends NerdLayer>, Constructor<? extends NerdLayer>> LAYER_CONSTRUCTORS;

  private boolean donePreloading;

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

  // endregion

  // region Constructors.
  @SuppressWarnings("unused")
  private NerdScene() {
    this.MANAGER = null;
    this.SKETCH = null;
    this.ASSETS = null;
    // this.SCENE_CLASS = null;
    this.ASSET_MAN_KEY = null;
    this.LAYER_CONSTRUCTORS = null;
  }

  public NerdScene(SceneKey p_key) {
    // region Verify and 'use' key.
    if (p_key == null) {
      throw new IllegalArgumentException("""
          Please use a `NerdSceneManager` instance to make a `NerdScene`!""");
    } else if (p_key.isUsed()) {
      throw new IllegalArgumentException("""
          Please use a `NerdSceneManager` instance to make a `NerdScene`! That is a used key!""");
    } else if (!p_key.isFor(this.getClass()))
      throw new IllegalArgumentException("""
          Please use a `NerdSceneManager` instance to make a `NerdScene`! That key is not for me!""");

    p_key.use();
    // endregion

    // this.SCENE_CLASS = p_key.getSceneClass();
    this.MANAGER = p_key.getSceneManager();
    this.SKETCH = this.MANAGER.getSketch();

    this.ASSET_MAN_KEY = new AssetManKey(SKETCH);
    this.ASSETS = new AssetManager(this.ASSET_MAN_KEY);

    this.LAYER_CONSTRUCTORS = new HashMap<>();
  }

  @SafeVarargs
  protected NerdScene(SceneKey p_key, Class<? extends NerdLayer>... p_layerClasses) {
    this(p_key);

    for (Class<? extends NerdLayer> c : p_layerClasses) {
      this.startLayer(c);
    }
  }
  // endregion

  public Sketch getSketch() {
    return this.SKETCH;
  }

  public boolean hasCompletedPreload(/* SceneManager.SceneKey p_key */) {
    // this.verifyKey(p_key);
    return this.donePreloading;
  }

  // region `Layer`-operations.
  // They get a running `Layer`'s reference from its (given) class.
  public NerdLayer getLayerOfClass(Class<? extends NerdLayer> p_layerClass) {
    for (NerdLayer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass))
        return l;
    return null; // Also does the work for:
    // if (!this.hasLayer(p_layerClass))
    // return null;
  }

  public HashSet<NerdLayer> getAllLayersOfClass(Class<? extends NerdLayer> p_layerClass) {
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
      toStart = (NerdLayer) layerConstructor.newInstance(
          new LayerKey(this, this.SKETCH, p_layerClass));
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
  public ArrayList<NerdLayer> allLayers() {
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
      toStart = this.LAYER_CONSTRUCTORS.get(layerClass).newInstance(
          this.new LayerKey(this, this.SKETCH, layerClass));
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
  private void verifyKey(SceneManager.SceneKey p_key) {
    if (p_key == null) {
      throw new IllegalArgumentException(
          "`NerdScene`s should only be accessed by a `NerdSceneManager`!");
    }

    Class<? extends NerdScene> myClass = this.getClass();
    if (!p_key.isFor(myClass)) {
      throw new IllegalArgumentException(
          "This key was not meant to be used by the `NerdScene`, `"
              + myClass.getSimpleName() + "`!");
    }
  }

  public void runOnSceneExit(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);

    if (!MANAGER.hasCached(this.getClass()))
      this.ASSETS.clear();

    this.onSceneExit();
  }

  public void /* Thread */ runPreload(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);

    // Thread toRet = new Thread(() -> {
    this.preload();
    // });

    // toRet.setName(this.getClass().getSimpleName() + "_AssetLoaderThread");
    // toRet.start();

    // return toRet;
  }

  public void runSetup(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);
    this.setup();

    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.setup();
  }

  public void runPre(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);
    this.pre();

    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.pre();
  }

  public void runDraw(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);
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

  public void runPost(SceneManager.SceneKey p_sceneKey) {
    this.verifyKey(p_sceneKey);
    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.post();

    this.post();
  }
  // endregion

  // region Scene callbacks.
  /**
   * Used by a {@code NerdScene} to load {@code NerdAsset}s
   * into their, or their {@code NerdSceneManager}'s {@code NerdAssetManager}.<br>
   * <br>
   * Use this method for all asset-loading purposes that you would like to do in
   * the background. If {@code NerdSceneManager::preloadSceneAssets} or
   * {@code NerdSceneManager::loadSceneAsync} is called, this method is run
   * async, loading-in all {@code NerdAssets}!
   */
  protected void preload() {
  }

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
