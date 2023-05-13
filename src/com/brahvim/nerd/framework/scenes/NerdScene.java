package com.brahvim.nerd.framework.scenes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.papplet_wrapper.NerdDisplayManager;
import com.brahvim.nerd.papplet_wrapper.NerdInputManager;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;
import com.brahvim.nerd.papplet_wrapper.NerdWindowManager;

/**
 * <h2>Do not use as an anonymous class!</h2>
 * <i>Always extend!</i>
 *
 * @author Brahvim Bhaktvatsal
 */
public abstract class NerdScene {

  // region `public` fields.
  public final NerdScene SCENE = this;
  // Forgive me for breaking naming conventions here.
  // Forgive me. Please!
  protected NerdSketch SKETCH;
  protected NerdSceneState STATE;
  protected NerdInputManager INPUT;
  protected NerdAssetManager ASSETS;
  protected NerdSceneManager MANAGER;
  protected NerdWindowManager WINDOW;
  protected NerdAbstractCamera CAMERA;
  protected NerdBridgedEcsManager ECS;
  protected NerdDisplayManager DISPLAYS;
  // endregion

  // region `private` fields.
  private int startMillis;
  private boolean donePreloading;

  // Start at `0`. "Who needs layers anyway?"
  private final ArrayList<NerdLayer> LAYERS = new ArrayList<>(0);
  // Worth remembering: `LinkedHashSet`s allow duplicate objects, store everything
  // in *in order*, but have no `indexOf()` method!

  private final HashMap<Class<? extends NerdLayer>, Constructor<? extends NerdLayer>>
  // ////////////////////////////////////////////////////////////////////////////////
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

  protected NerdScene() {
  }

  // region Queries.
  public NerdSketch getSketch() {
    return this.SKETCH;
  }

  public int getTimesLoaded() {
    return this.MANAGER.getTimesSceneLoaded(this.getClass());
  }

  public boolean hasCompletedPreload() {
    return this.donePreloading;
  }

  // region Time queries.
  public int getStartMillis() {
    return this.startMillis;
  }

  public int getMillisSinceStart() {
    return this.SKETCH.millis() - this.startMillis;
  }
  // endregion
  // endregion

  // region `Layer`-operations.
  // region `onFirstLayerOfClass()` overloads.
  /**
   * Given a {@link NerdLayer} class, performs a task on the instance of that
   * class, which was added <i>first</i> to this {@link NerdScene}.
   */
  public <T extends NerdLayer> void onFirstLayerOfClass(final Class<T> p_layerClass, final Consumer<T> p_task) {
    this.onFirstLayerOfClass(p_layerClass, p_task, null);
  }

  /**
   * Given a {@link NerdLayer} class, performs a task on the instance of that
   * class, which was added <i>first</i> to this {@link NerdScene}. If there is no
   * instance of the given class, performs the other given task.
   */
  // Actual implementation!:
  public <T extends NerdLayer> void onFirstLayerOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_onFoundTask, final Runnable p_notFoundTask) {
    final T instance = (T) this.getFirstLayerOfClass(p_layerClass);

    // Check if we have any such layers:
    if (instance != null) {
      // On finding one, perform the given task!
      if (p_onFoundTask != null)
        p_onFoundTask.accept(instance);
    } else {
      // On finding none, perform the other task!
      if (p_notFoundTask != null)
        p_notFoundTask.run();
    }
  }
  // endregion

  // region `onLayersOfClass()` and similar.
  // region `onInactiveLayersOfClass()` overloads.
  public <T extends NerdLayer> void onInactiveLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task) {
    this.onInactiveLayersOfClass(p_layerClass, p_task, null);
  }

  // Actual implementation!:
  @SuppressWarnings("unchecked")
  public <T extends NerdLayer> void onInactiveLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task, final Runnable p_notFoundTask) {

    int i = 0;
    final int LAYERS_SIZE = this.LAYERS.size();

    // For every `NerdLayer`,
    for (; i != LAYERS_SIZE; i++) {
      final NerdLayer l = this.LAYERS.get(i);

      if (l != null)
        if (l.getClass().equals(p_layerClass))
          if (!l.isActive()) // ...if it's from the same class,
            p_task.accept((T) l); // ...perform the given task!
    }

    // If no `NerdLayer`s were found, perform the other task!:
    if (i == 0 && p_notFoundTask != null)
      p_notFoundTask.run();
  }
  // endregion

  // region `onActiveLayersOfClass()` overloads.
  public <T extends NerdLayer> void onActiveLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task) {
    this.onActiveLayersOfClass(p_layerClass, p_task, null);
  }

  @SuppressWarnings("unchecked")
  public <T extends NerdLayer> void onActiveLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task, final Runnable p_notFoundTask) {

    int i = 0;
    final int LAYERS_SIZE = this.LAYERS.size();

    // For every `NerdLayer`,
    for (; i != LAYERS_SIZE; i++) {
      final NerdLayer l = this.LAYERS.get(i);

      if (l != null)
        if (l.getClass().equals(p_layerClass))
          if (l.isActive()) // ...if it's from the same class,
            p_task.accept((T) l); // ...perform the given task!
    }

    // If no `NerdLayer`s were found, perform the other task!:
    if (i == 0 && p_notFoundTask != null)
      p_notFoundTask.run();
  }
  // endregion

  // region `onLayersOfClass()` overloads.
  /**
   * Given a {@link NerdLayer} class, performs a task on all instances of that
   * class being used by this {@link NerdScene}.
   */
  public <T extends NerdLayer> void onLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task) {
    this.onLayersOfClass(p_layerClass, p_task, null);
  }

  /**
   * Given a {@link NerdLayer} class, performs a task on all instances of that
   * class being used by this {@link NerdScene}. If no instance is found, performs
   * the other given task.
   */
  // Actual implementation!:
  @SuppressWarnings("unchecked")
  public <T extends NerdLayer> void onLayersOfClass(
      final Class<T> p_layerClass, final Consumer<T> p_task, final Runnable p_notFoundTask) {

    int i = 0;
    final int LAYERS_SIZE = this.LAYERS.size();

    // For every `NerdLayer`,
    for (; i != LAYERS_SIZE; i++) {
      final NerdLayer l = this.LAYERS.get(i);

      if (l.getClass().equals(p_layerClass)) // ...if it's from the same class,
        p_task.accept((T) l); // ...perform the given task!
    }

    // If no `NerdLayer`s were found, perform the other task!:
    if (i == 0 && p_notFoundTask != null)
      p_notFoundTask.run();
  }
  // endregion
  // endregion

  // region `getLayer()` and similar.
  // They get a running `Layer`'s reference from its (given) class.
  public <RetT extends NerdLayer> RetT getFirstLayerOfClass(final Class<RetT> p_layerClass) {
    for (final NerdLayer l : this.LAYERS)
      if (l.getClass().equals(p_layerClass))
        return p_layerClass.cast(l);
    return null;
  }

  /**
   * Gives an {@link ArrayList} of {@link NerdLayer} instances of the given
   * subclass this {@link NerdScene} contains, which are <b>not</b> active.
   */
  public ArrayList<NerdLayer> getInactiveLayers(final Class<? extends NerdLayer> p_layerClass) {
    final ArrayList<NerdLayer> toRet = new ArrayList<>();

    for (final NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.getClass().equals(p_layerClass) && !l.isActive())
          toRet.add(l);

    return toRet;
  }

  /**
   * Gives an {@link ArrayList} of {@link NerdLayer} instances of the given
   * subclass this {@link NerdScene} contains, which are also active.
   */
  public ArrayList<NerdLayer> getActiveLayers(final Class<? extends NerdLayer> p_layerClass) {
    final ArrayList<NerdLayer> toRet = new ArrayList<>();

    for (final NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.getClass().equals(p_layerClass) && l.isActive())
          toRet.add(l);

    return toRet;
  }

  /**
   * Gives an {@link ArrayList} of {@link NerdLayer} instances of the given
   * subclass this {@link NerdScene} contains.
   */
  public ArrayList<NerdLayer> getLayers(final Class<? extends NerdLayer> p_layerClass) {
    final ArrayList<NerdLayer> toRet = new ArrayList<>();

    for (final NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.getClass().equals(p_layerClass)) {
          toRet.add(l);
        }

    return toRet;
  }

  /**
   * This method gives the user access to all {@link NerdLayer} instances being
   * used by this {@code NerdScene} along with rights such as changing layer
   * rendering order.
   */
  public ArrayList<NerdLayer> getLayers() {
    return this.LAYERS;
  }
  // endregion

  // region `NerdLayer` state-management.
  @SafeVarargs // I'm not willing to limit your freedom, but this method HAS to be `final`...
  public final NerdLayer[] addLayers(final Class<? extends NerdLayer>... p_layerClasses) {
    final NerdLayer[] toRet = new NerdLayer[p_layerClasses.length];

    for (int i = 0; i < p_layerClasses.length; i++) {
      final Class<? extends NerdLayer> c = p_layerClasses[i];
      toRet[i] = this.addLayers(c);
    }

    return toRet;
  }

  public <RetT extends NerdLayer> RetT addLayers(final Class<RetT> p_layerClass) {
    if (p_layerClass == null)
      throw new NullPointerException(
          "You weren't supposed to pass `null` into `NerdScene::startLayer()`.");

    // We allow multiple layer instances, by the way.

    final Constructor<? extends NerdLayer> layerConstructor = this.getLayerConstructor(p_layerClass);
    final NerdLayer toRet = this.constructLayer(layerConstructor);
    toRet.setActive(true); // Sets stuff up.
    this.LAYERS.add(toRet);
    return p_layerClass.cast(toRet);
  }

  @SafeVarargs // I'm not willing to limit your freedom, but this method HAS to be `final`...
  public final void restartLayers(final Class<? extends NerdLayer>... p_layerClasses) {
    for (final Class<? extends NerdLayer> c : p_layerClasses)
      this.restartLayers(c);
  }

  public void restartLayer(final NerdLayer p_layer) {
    if (p_layer == null)
      return;

    final Class<? extends NerdLayer> LAYER_CLASS = p_layer.getClass();

    // If an instance of this layer does not already exist,
    if (!this.LAYERS.contains(p_layer)) {
      System.out.printf(
          "No instance of `NerdLayer` `%s` exists. Making one...\n",
          LAYER_CLASS.getSimpleName());

      this.addLayers(LAYER_CLASS);
      return;
    }

    final NerdLayer toStart = this.constructLayer(this.getLayerConstructor(LAYER_CLASS));
    this.LAYERS.set(this.LAYERS.indexOf(p_layer), toStart);

    p_layer.setActive(false);
    toStart.setActive(true);
  }
  // endregion

  // region `NerdLayer` construction.
  private Constructor<? extends NerdLayer> getLayerConstructor(final Class<? extends NerdLayer> p_layerClass) {
    Constructor<? extends NerdLayer> toRet = this.LAYER_CONSTRUCTORS.get(p_layerClass);

    if (toRet != null)
      return toRet;

    try {
      toRet = p_layerClass.getConstructor();
    } catch (final NoSuchMethodException e) {
      System.err.println("""
          Every subclass of `NerdLayer` must be `public` with a `public` \"null-constructor\"
          (constructor with no arguments), or no overriden constructors at all.""");
    } catch (final SecurityException e) {
      e.printStackTrace();
    }

    this.LAYER_CONSTRUCTORS.put(p_layerClass, toRet);
    return toRet;
  }

  private NerdLayer constructLayer(final Constructor<? extends NerdLayer> p_layerConstructor) {
    NerdLayer toRet = null;

    // region Construct `toRet`.
    try {
      toRet = (NerdLayer) p_layerConstructor.newInstance(
      // new NerdScene.LayerKey(this, this.SKETCH, p_layerClass)
      );
    } catch (final InstantiationException e) {
      e.printStackTrace();
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
    } catch (final IllegalArgumentException e) {
      e.printStackTrace();
    } catch (final InvocationTargetException e) {
      e.printStackTrace();
    }
    // endregion

    toRet.SCENE = this;
    toRet.STATE = toRet.SCENE.STATE;
    toRet.INPUT = toRet.SCENE.INPUT;
    toRet.SKETCH = toRet.SCENE.SKETCH;
    toRet.ASSETS = toRet.SCENE.ASSETS;
    toRet.WINDOW = toRet.SCENE.WINDOW;
    toRet.CAMERA = toRet.SCENE.CAMERA;
    toRet.MANAGER = toRet.SCENE.MANAGER;
    toRet.DISPLAYS = toRet.SCENE.DISPLAYS;

    return toRet;
  }
  // endregion
  // endregion

  // region Anything callback-related, LOL.
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

  /* `package` */ void runSetup(final NerdSceneState p_state) {
    this.startMillis = this.SKETCH.millis();
    this.ECS.setup(p_state);
    this.setup(p_state);

    // `NerdLayer`s don't get to respond to this `setup()`.
  }

  /* `package` */ synchronized void runPreload() {
    this.ECS.preload();
    this.preload();
    this.ASSETS.forceLoading();

    if (this.MANAGER.SETTINGS.ON_PRELOAD.useExecutors) {
      final ThreadPoolExecutor executor = new ThreadPoolExecutor(
          0, this.MANAGER.SETTINGS.ON_PRELOAD.maxExecutorThreads,
          10L, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> {
            return new Thread(r, "NerdAssetPreloader_" + this.getClass().getSimpleName());
          });

      final HashSet<Future<?>> futures = new HashSet<>(this.ASSETS.size());
      this.ASSETS.forEach(a -> futures.add(executor.submit(a::startLoading)));
      executor.shutdown(); // This tells the executor to stop accepting new tasks.

      // If you must complete within this function, do that:
      if (this.MANAGER.SETTINGS.ON_PRELOAD.completeWithinPreloadCall)
        try {
          executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // Keep going, keep going...
          // Can't simply cheat the implementation to make it wait forever!
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }
    } else
      this.ASSETS.forEach(NerdAsset::startLoading);

    this.donePreloading = true;
  }

  /* `package` */ void runSceneChanged() {
    this.ECS.sceneChanged();
    this.sceneChanged();
  }

  // SUPER expensive for how little they will be used (need `push()` an `pop()`)!:
  /*
   * void runPostDraw() {
   * if (this.MANAGER.SETTINGS.drawFirstCaller == null)
   * this.MANAGER.SETTINGS.drawFirstCaller =
   * NerdSceneManager.SceneManagerSettings.CallbackOrder.LAYER;
   * 
   * // To avoid asynchronous changes from causing repetition, we put both parts
   * in
   * // `if` and `else` block.
   * 
   * switch (this.MANAGER.SETTINGS.drawFirstCaller) {
   * case SCENE -> {
   * this.SKETCH.push();
   * this.postDraw();
   * this.SKETCH.pop();
   * 
   * for (final NerdLayer l : this.LAYERS)
   * if (l != null)
   * if (l.isActive()) {
   * this.SKETCH.push();
   * l.postDraw();
   * this.SKETCH.pop();
   * }
   * }
   * 
   * case LAYER -> {
   * for (final NerdLayer l : this.LAYERS)
   * if (l != null)
   * if (l.isActive()) {
   * this.SKETCH.push();
   * l.postDraw();
   * this.SKETCH.pop();
   * }
   * 
   * this.SKETCH.push();
   * this.postDraw();
   * this.SKETCH.pop();
   * }
   * }
   * }
   * 
   * void runPreDraw() {
   * if (this.MANAGER.SETTINGS.drawFirstCaller == null)
   * this.MANAGER.SETTINGS.drawFirstCaller =
   * NerdSceneManager.SceneManagerSettings.CallbackOrder.LAYER;
   * 
   * // To avoid asynchronous changes from causing repetition, we put both parts
   * in
   * // `if` and `else` block.
   * 
   * switch (this.MANAGER.SETTINGS.drawFirstCaller) {
   * case SCENE -> {
   * this.SKETCH.push();
   * this.preDraw();
   * this.SKETCH.pop();
   * 
   * for (final NerdLayer l : this.LAYERS)
   * if (l != null)
   * if (l.isActive()) {
   * this.SKETCH.push();
   * l.preDraw();
   * this.SKETCH.pop();
   * }
   * }
   * 
   * case LAYER -> {
   * for (final NerdLayer l : this.LAYERS)
   * if (l != null)
   * if (l.isActive()) {
   * this.SKETCH.push();
   * l.preDraw();
   * this.SKETCH.pop();
   * }
   * 
   * this.SKETCH.push();
   * this.preDraw();
   * this.SKETCH.pop();
   * }
   * }
   * }
   */

  /* `package` */ void runDispose() {
    this.ECS.dispose();
    this.dispose();
  }

  /* `package` */ void runDraw() {
    if (this.MANAGER.SETTINGS.drawFirstCaller == null)
      this.MANAGER.SETTINGS.drawFirstCaller = NerdSceneManager.SceneManagerSettings.CallbackOrder.LAYER;

    this.ECS.draw();

    // To avoid asynchronous changes from causing repetition, we put both parts in
    // `if` and `else` block.

    switch (this.MANAGER.SETTINGS.drawFirstCaller) {
      case SCENE -> {
        this.SKETCH.push();
        this.draw();
        this.SKETCH.pop();

        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive()) {
              this.SKETCH.push();
              l.draw();
              this.SKETCH.pop();
            }
      }

      case LAYER -> {
        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive()) {
              this.SKETCH.push();
              l.draw();
              this.SKETCH.pop();
            }

        this.SKETCH.push();
        this.draw();
        this.SKETCH.pop();
      }
    }

  }

  /* `package` */ void runPost() {
    if (this.MANAGER.SETTINGS.postFirstCaller == null)
      this.MANAGER.SETTINGS.postFirstCaller = NerdSceneManager.SceneManagerSettings.CallbackOrder.LAYER;

    this.ECS.post();

    // To avoid asynchronous changes from causing repetition, we put both parts in
    // `if` and `else` block.

    switch (this.MANAGER.SETTINGS.preFirstCaller) {
      case SCENE -> {
        this.post();

        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive())
              l.post();
      }
      case LAYER -> {
        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive())
              l.post();

        this.post();
      }
    }

  }

  /* `package` */ void runExit() {
    for (final NerdLayer l : this.LAYERS)
      if (l != null)
        if (l.isActive())
          l.exit();

    this.ECS.exit();
    this.exit();
  }

  /* `package` */ void runPre() {
    if (this.MANAGER.SETTINGS.preFirstCaller == null)
      this.MANAGER.SETTINGS.preFirstCaller = NerdSceneManager.SceneManagerSettings.CallbackOrder.SCENE;

    this.ECS.pre();

    // To avoid asynchronous changes from causing repetition, we put both parts in
    // `if` and `else` block.

    switch (this.MANAGER.SETTINGS.preFirstCaller) {
      case SCENE -> {
        this.pre();

        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive())
              l.pre();
      }
      case LAYER -> {
        for (final NerdLayer l : this.LAYERS)
          if (l != null)
            if (l.isActive())
              l.pre();

        this.pre();
      }
    }

  }

  // region Scene workflow callbacks.
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

  protected void sceneChanged() {
  }

  /**
   * {@link NerdScene#setup()} is called when one of
   * {@link NerdSceneManager#startScene(Class)},
   * {@link NerdSceneManager#restartScene(Class)}, or
   * {@link NerdSceneManager#startPreviousScene(Class)}
   * is called, after the {@link NerdScene} finishes executing
   * {@link NerdScene#preload()},<br>
   * <br>
   * {@link NerdLayer#setup()} is called <i>when a {@link NerdLayer} is set
   * active</i> using {@link NerdLayer#setActive(boolean)}.
   */
  protected void setup(final NerdSceneState p_state) {
  }

  protected void pre() {
  }

  protected void draw() {
  }

  protected void post() {
  }

  protected void exit() {
  }

  // protected void preDraw() { }

  protected void dispose() {
  }

  // protected void postDraw() { }
  // endregion
  // endregion

  // region Events.
  // region Mouse events.
  public void mousePressed() {
  }

  public void mouseReleased() {
  }

  public void mouseMoved() {
  }

  public void mouseClicked() {
  }

  public void mouseDragged() {
  }

  // @SuppressWarnings("unused")
  public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
  }
  // endregion

  // region Keyboard events.
  public void keyTyped() {
  }

  public void keyPressed() {
  }

  public void keyReleased() {
  }
  // endregion

  // region Touch events.
  public void touchStarted() {
  }

  public void touchMoved() {
  }

  public void touchEnded() {
  }
  // endregion

  // region Window focus events.
  public void resized() {
  }

  public void focusLost() {
  }

  public void focusGained() {
  }

  public void monitorChanged() {
  }

  public void fullscreenChanged(final boolean p_state) {
  }
  // endregion
  // endregion

}
