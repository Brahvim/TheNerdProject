package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.StringTable;
import com.brahvim.nerd.io.asset_loader.AssetManKey;
import com.brahvim.nerd.io.asset_loader.AssetManager;
import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.papplet_wrapper.Sketch;

/**
 * <h2>Do not use as an anonymous class!</h2>
 * <i>Always extend!</i>
 */
// * The {@link PApplet} you passed into your
// * {@link SceneManager} is what you get! :)

public class NerdScene implements InputEventHandling {

  // region Inner classes.
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
   * private final static ArrayList<AutoDrawable> ALL_AUTO_DRAWABLES = new
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

  // region Fields.
  // Forgive me for breaking the naming conventions. Forgive me. Please!
  public /* final */ Sketch SKETCH;
  public /* final */ SceneState STATE;
  public /* final */ StringTable STRINGS;
  public /* final */ AssetManager ASSETS;
  public /* final */ SceneManager MANAGER;
  protected /* final */ AssetManKey ASSET_MAN_KEY;

  protected final NerdScene SCENE = this;

  // region `private` fields.
  private int startMillis;
  private boolean donePreloading;

  // ~~Don't let the scene manage its `manager`!:~~

  // Would've used a `LinkedHashSet`, but am using `ArrayList`s instead since
  // duplicates won't be allowed in the former case. We need them!

  // Start at `0`. "Who needs layers anyway?"
  private final ArrayList<NerdLayer> LAYERS = new ArrayList<>(0);
  private final HashMap<Class<? extends NerdLayer>, Constructor<? extends NerdLayer>>

  LAYER_CONSTRUCTORS = new HashMap<>(0);

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
  // endregion

  // region Construction.
  protected NerdScene() {
  }
  // endregion

  // region Queries.
  public Sketch getSketch() {
    return this.SKETCH;
  }

  public int timesSceneWasLoaded() {
    return this.MANAGER.numSceneLoads(this.getClass());
  }

  public boolean hasCompletedPreload(/* SceneManager.SceneKey p_key */) {
    // this.verifyKey(p_key);
    return this.donePreloading;
  }

  // region Timing queries.
  public int startMillis() {
    return this.startMillis;
  }

  public int millisSinceStart() {
    return this.SKETCH.millis() - this.startMillis;
  }
  // endregion
  // endregion

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
    for (Class<? extends NerdLayer> c : p_layerClasses)
      this.startLayer(c);
  }

  private Constructor<? extends NerdLayer> getLayerConstructor(Class<? extends NerdLayer> p_layerClass) {
    Constructor<? extends NerdLayer> toRet = this.LAYER_CONSTRUCTORS.get(p_layerClass);

    if (toRet != null)
      return toRet;

    try {
      toRet = p_layerClass.getConstructor();
    } catch (NoSuchMethodException e) {
      System.err.println("""
          Every subclass of `NerdLayer` must have a `public` \"null-constructor\"
          (A constructor taking no arguments), or no overriden constructors at all.
          """);
    } catch (SecurityException e) {
      e.printStackTrace();
    }

    this.LAYER_CONSTRUCTORS.put(p_layerClass, toRet);
    return toRet;
  }

  private NerdLayer constructLayer(Constructor<? extends NerdLayer> p_layerConstructor) {
    NerdLayer toRet = null;

    try {
      toRet = (NerdLayer) p_layerConstructor.newInstance(
      // new NerdScene.LayerKey(this, this.SKETCH, p_layerClass)
      );
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    toRet.SCENE = this;
    toRet.SKETCH = this.SKETCH;
    toRet.MANAGER = this.MANAGER;

    return toRet;
  }

  public void startLayer(Class<? extends NerdLayer> p_layerClass) {
    if (p_layerClass == null)
      throw new NullPointerException(
          "You weren't supposed to pass `null` into `NerdScene::startLayer()`.");

    // We allow multiple layer instances, by the way.

    Constructor<? extends NerdLayer> layerConstructor = this.getLayerConstructor(p_layerClass);
    NerdLayer toStart = this.constructLayer(layerConstructor);

    synchronized (this.LAYERS) {
      this.LAYERS.add(toStart);
    }
    toStart.setActive(true);
  }

  /**
   * This method gives the user the freedoms such as changing
   * layer rendering order.
   */
  public ArrayList<NerdLayer> allLayers() {
    return this.LAYERS;
  }

  public void restartLayer(NerdLayer p_layer) {
    if (p_layer == null)
      return;

    final Class<? extends NerdLayer> LAYER_CLASS = p_layer.getClass();

    // If an instance of this layer does not already exist,
    if (!this.LAYERS.contains(p_layer)) {
      System.out.printf(
          "No instance of `NerdLayer` `%s` exists. Making one...\n",
          LAYER_CLASS.getSimpleName());

      this.startLayer(LAYER_CLASS);
      return;
    }

    NerdLayer toStart = this.constructLayer(this.getLayerConstructor(LAYER_CLASS));
    this.LAYERS.set(this.LAYERS.indexOf(p_layer), toStart);

    p_layer.setActive(false);
    toStart.setActive(true);
  }
  // endregion

  // region Anything callback-related, LOL.
  // region `SceneManager.SceneKey` app-workflow callback runners.
  /*
   * 
   * private void verifyKey(SceneManager.SceneKey p_key) {
   * if (p_key == null) {
   * throw new IllegalArgumentException(
   * "`NerdScene`s should only be accessed by a `NerdSceneManager`!");
   * }
   * 
   * Class<? extends NerdScene> myClass = this.getClass();
   * if (!p_key.isFor(myClass)) {
   * throw new IllegalArgumentException(
   * "This key was not meant to be used by the `NerdScene`, `"
   * + myClass.getSimpleName() + "`!");
   * }
   * }
   */

  /* package */ void runOnSceneExit() {
    // this.verifyKey(p_sceneKey);

    this.onSceneExit();
  }

  /* `package`, not `public`! */ void /* Thread */ runPreload() {
    // this.verifyKey(p_sceneKey);

    // Thread toRet = new Thread(() -> {
    this.preload();
    this.donePreloading = true;
    // });

    // toRet.setName(this.getClass().getSimpleName() + "_AssetLoaderThread");
    // toRet.start();

    // return toRet;
  }

  /* package */ void runSetup() {
    // this.verifyKey(p_sceneKey);
    this.startMillis = this.SKETCH.millis();
    this.setup();

    // `NerdLayer`s don't get to respond to this `setup()`.
    // for (NerdLayer l : this.LAYERS)
    // if (l != null)
    // if (l.isActive())
    // l.setup();
  }

  /* package */ void runPre() {
    // this.verifyKey(p_sceneKey);
    this.pre();

    for (NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.pre();
  }

  /* package */ void runDraw() {
    // this.verifyKey(p_sceneKey);
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

  /* package */ void runPost() {
    // this.verifyKey(p_sceneKey);
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
   * async, loading-in all {@code NerdAssets}!<br>
   * <br>
   * Since {@code NerdScene}s could be a part of the same `Sketch`, it is
   * important to ensure that this method is `synchronized`.
   */
  protected synchronized void preload() {
  }

  protected void onSceneExit() {
  }
  // endregion

  // region App workflow callbacks.
  /**
   * {@link NerdScene#setup()} is called first,
   * {@link NerdLayer#setup()} is called for each {@link NerdLayer}, later.
   */
  protected void setup() {
  }

  /**
   * {@link NerdScene#pre()} is called first,
   * {@link NerdLayer#pre()} is called for each {@link NerdLayer}, later.
   */
  protected void pre() {
  }

  /**
   * {@link NerdLayer#draw()} is called for each {@link NerdLayer}, first.
   * {@link NerdScene#draw()} is called later.
   */
  protected void draw() {
  }

  /**
   * {@link NerdLayer#draw()} is called for each {@link NerdLayer}, first.
   * {@link NerdScene#draw()} is called later.
   */
  protected void post() {
  }
  // endregion
  // endregion

}
