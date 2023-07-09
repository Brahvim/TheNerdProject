package com.brahvim.nerd.framework.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.processing_wrapper.NerdDisplayModule;
import com.brahvim.nerd.processing_wrapper.NerdInputModule;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilderSettings;
import com.brahvim.nerd.processing_wrapper.NerdWindowModule;

public class NerdScenesModule extends NerdModule {

	// region Inner classes.
	// My code style: If it is an inner class, also write the name of the outer
	// class. I do this to aid reading and to prevent namespace pollution.

	@FunctionalInterface
	public interface NerdSceneChangedListener {
		public void sceneChanged(NerdSketch sketch,
				Class<? extends NerdScene> previous,
				Class<? extends NerdScene> current);
	}

	/**
	 * Stores scene data while a scene is not active.
	 */
	private static class NerdSceneManagerSceneCache {

		// region Fields.
		private int timesLoaded = 0;
		private final NerdSceneState STATE;
		private final Constructor<? extends NerdScene> CONSTRUCTOR;

		// private NerdEcsManager cachedEcs; // Nope! If the user really wants it,
		// they'll should get stuff serialized via `NerdEcsManager` and handle it...

		private NerdScene cachedReference; // `NerdSceneManager` deletes this when the scene exits.
		private NerdAssetsModule cachedAssets;
		// endregion

		private NerdSceneManagerSceneCache(final Constructor<? extends NerdScene> p_constructor,
				final NerdScene p_cachedReference) {
			this.CONSTRUCTOR = p_constructor;
			this.cachedReference = p_cachedReference;
			this.STATE = this.cachedReference.STATE;
		}

		// region Cache queries.
		public boolean cacheIsNull() {
			return this.cachedReference == null;
		}

		public void /* deleteCache() { */ nullifyCache() {
			// If this was (hopefully) the only reference to the scene object, it gets GCed!
			this.cachedReference = null;
			System.gc();
		}
		// endregion

	}

	public static class NerdSceneManagerSettings {

		public Class<? extends NerdEcsSystem<?>>[] orderOfEcsSystems;

		// region App workflow callbacks scene-or-layer order.
		/**
		 * Dictates to every {@link NerdScenesModule} instance, the order in which a
		 * {@link NerdScene} or {@link NerdLayer} is allowed to call certain "workflow
		 * events" ({@link NerdScene#pre()}, {@link NerdScene#draw()} and
		 * {@link NerdScene#post()}) from Processing.
		 *
		 * @see {@link NerdScenesModule.NerdSceneManagerSettings#preFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#SCENE} by
		 *      default.
		 * @see {@link NerdScenesModule.NerdSceneManagerSettings#drawFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#LAYER} by
		 *      default.
		 * @see {@link NerdScenesModule.NerdSceneManagerSettings#postFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#LAYER} by
		 *      default.
		 */
		public /* static */ enum NerdSketchCallbackOrder {
			SCENE(), LAYER();
		}

		/**
		 * Controls whether {@link NerdScene#pre()} or {@link NerdLayer#pre()} is
		 * called first by the {@link NerdScenesModule}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#SCENE}.
		 */
		public NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder preFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.SCENE;

		/**
		 * Controls whether {@link NerdScene#draw()} or {@link NerdLayer#draw()} is
		 * called first by the {@link NerdScenesModule}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#LAYER}.
		 */
		public NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder drawFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;

		/**
		 * Controls whether {@link NerdScene#post()} or {@link NerdLayer#post()} is
		 * called first by the {@link NerdScenesModule}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#LAYER}.
		 */
		public NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder postFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
		// endregion

		public class OnScenePreload {

			private OnScenePreload() {
			}

			/**
			 * When {@code true}, {@link NerdScene#preload()} is run only the first time
			 * a {@link NerdScene} class is used in the engine. Setting this to
			 * {@code false} loads scene assets each time, so that assets are updated.
			 *
			 * @apiNote {@code true} by default!
			 */
			public boolean preloadOnlyOnce = true;

			/**
			 * When {@code true}, {@link NerdScene#preload()} runs the loading process in
			 * multiple threads using a {@link java.util.concurrent.ExecutorService}. If
			 * {@link NerdScenesModule.NerdSceneManagerSettings.OnScenePreload#completeAssetLoadingWithinPreload}
			 * is {@code true}, the asset loading is <i>guaranteed</i> to finish within
			 * {@link NerdScene#preload()}.
			 *
			 * @apiNote {@code true} by default!
			 */
			public boolean useExecutors = true;

			/**
			 * The maximum number of threads multithreaded asset loading started
			 * in {@link NerdScene#preload()} can use. You may change whether that's the
			 * case, by setting
			 * {@link NerdScenesModule.NerdSceneManagerSettings.OnScenePreload#useExecutors}
			 * to {@code true} or {@code false}!
			 *
			 * @apiNote We use <b>{@code 6}</b> threads by default! Enough? Happy?
			 *          I hope you are ":D!~
			 */
			public int maxExecutorThreads = 6;

			/**
			 * If
			 * {@link NerdScenesModule.NerdSceneManagerSettings.OnScenePreload#useExecutors}
			 * is {@code true}, the asset loading process in {@link NerdScene#preload()} is
			 * run using multiple threads. Setting <i>this</i> to {@code true}
			 * <i>guarantees</i> that the asset loading will be finished within
			 * {@link NerdScene#preload()}.
			 *
			 * @apiNote {@code true} by default!
			 */
			public boolean completeAssetLoadingWithinPreload = true;

		}

		public class OnSceneSwitch {

			private OnSceneSwitch() {
			}

			/**
			 * If set to {@code -1}, will call {@link NerdSketch#clear()} and not
			 * {@link NerdSketch#background()}. <b>This is the default behavior!</b>
			 */
			public volatile int clearColor = -1;

			/**
			 * Clears the screen according to
			 * {@link NerdScenesModule.NerdSceneManagerSettings.OnSceneSwitch#clearColor}.
			 *
			 * @apiNote {@code false} by default.
			 */
			public volatile boolean doClear = false;

			/**
			 * Resets {@link NerdScenesModule.NerdSceneManagerSettings#preFirstCaller},
			 * {@link NerdScenesModule.NerdSceneManagerSettings#drawFirstCaller}, and
			 * {@link NerdScenesModule.NerdSceneManagerSettings#postFirstCaller} to their
			 * default values!
			 */
			public volatile boolean resetSceneLayerCallbackOrder = true;

		}

		public final NerdScenesModule.NerdSceneManagerSettings.OnSceneSwitch ON_SWITCH = new OnSceneSwitch();

		public final NerdScenesModule.NerdSceneManagerSettings.OnScenePreload ON_PRELOAD = new OnScenePreload();

	}
	// endregion

	public final NerdScenesModule.NerdSceneManagerSettings SETTINGS;

	// region `protected` and `private` fields.
	protected final NerdBridgedEcsModule ECS_INSTANCE;
	protected boolean sceneSwitchOccured;
	protected NerdScene currScene;

	/**
	 * This {@link HashMap} contains cached data about each {@link NerdScene} class
	 * any {@link NerdScenesModule} instance has cached or ran.
	 * <p>
	 * Actual "caching" of a {@link NerdScene} is when its corresponding
	 * {@link NerdScenesModule.NerdSceneManagerSceneCache#cachedReference} is not
	 * {@code null}.
	 * <p>
	 * The initial capacity here ({@code 2}) is to aid performance, since, the JIT
	 * does no optimization till the first scene switch. All scene switches after
	 * that the initial should be fast enough!
	 */
	private final HashMap<Class<? extends NerdScene>, NerdScenesModule.NerdSceneManagerSceneCache> SCENE_CACHE = new HashMap<>(
			2);

	// Notes on some strange (useless? Useful?!) idea.
	/*
	 * Keep track of what `Sketch`es a manager exists for.
	 * `private static final HashSet<? extends Sketch> SKETCHES = new HashSet<>(1);
	 * "Don't let anybody build another!" <-- ...idea I've stopped.
	 */

	// region Sketch Event Listeners.
	private final LinkedHashSet<NerdScenesModule.NerdSceneChangedListener> SCENE_CHANGED_LISTENERS;
	private final LinkedHashSet<NerdScenesModule.NerdSceneChangedListener>
	/*		*/ SCENE_CHANGED_LISTENERS_TO_REMOVE = new LinkedHashSet<>(0);
	// endregion
	private Class<? extends NerdScene> currSceneClass, prevSceneClass;
	// endregion

	public NerdScenesModule(final NerdSketch p_sketch, final NerdSketchBuilderSettings p_settings) {
		super(p_sketch);

		this.SETTINGS = p_settings.sceneManagerSettings;
		this.SETTINGS.orderOfEcsSystems = p_settings.ecsSystemOrder;
		this.SCENE_CHANGED_LISTENERS = p_settings.sceneChangedListeners;
		this.ECS_INSTANCE = new NerdBridgedEcsModule(super.SKETCH, this.SETTINGS.orderOfEcsSystems);
	}

	// region Workflow callbacks.
	@Override
	protected void pre() {
		if (this.currScene != null)
			this.currScene.runPre();
	}

	@Override
	protected void post() {
		if (this.currScene != null)
			this.currScene.runPost();

		this.sceneSwitchOccured = false;
	}

	@Override
	protected void draw() {
		if (this.currScene != null)
			this.currScene.runDraw();
	}

	@Override
	protected void exit() {
		if (this.currScene != null)
			this.currScene.runExit();
	}

	@Override
	protected void dispose() {
		if (this.currScene != null)
			this.currScene.runDispose();
	}

	// Too expensive! Need a `push()` and `pop()`.
	/*
	 * protected void runPreDraw() {
	 * if (this.currScene != null)
	 * this.currScene.runPreDraw();
	 * }
	 * protected void runPostDraw() {
	 * if (this.currScene != null)
	 * this.currScene.runPostDraw();
	 * }
	 */
	// endregion

	// region Event callbacks. Passed to the ECS first, THEN the scene!
	// `NerdLayer` callers:
	protected void callOnCurrSceneActiveLayers(final Consumer<NerdLayer> p_eventCallbackMethod) {
		if (p_eventCallbackMethod != null)
			for (final NerdLayer l : this.currScene.getLayers())
				if (l != null)
					if (l.isActive())
						p_eventCallbackMethod.accept(l);
	}

	protected <OtherArgT> void callOnCurrSceneActiveLayers(
			final BiConsumer<NerdLayer, OtherArgT> p_eventCallbackMethod, final OtherArgT p_otherArg) {
		if (p_eventCallbackMethod != null)
			for (final NerdLayer l : this.currScene.getLayers())
				if (l != null)
					if (l.isActive())
						p_eventCallbackMethod.accept(l, p_otherArg);
	}

	// region Mouse event callbacks.
	@Override
	public void mousePressed() {
		if (this.currScene == null)
			return;

		this.currScene.mousePressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::mousePressed);
	}

	@Override
	public void mouseReleased() {
		if (this.currScene == null)
			return;

		this.currScene.mouseReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseReleased);
	}

	@Override
	public void mouseMoved() {
		if (this.currScene == null)
			return;

		this.currScene.mouseMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseMoved);
	}

	@Override
	public void mouseClicked() {
		if (this.currScene == null)
			return;

		this.currScene.mouseClicked();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseClicked);
	}

	@Override
	public void mouseDragged() {
		if (this.currScene == null)
			return;

		this.currScene.mouseDragged();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseDragged);
	}

	@Override
	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		if (this.currScene == null)
			return;

		this.currScene.mouseWheel(p_mouseEvent);
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseWheel, p_mouseEvent);
	}
	// endregion

	// region Touch event callbacks.
	@Override
	public void touchStarted() {
		if (this.currScene == null)
			return;

		this.currScene.touchStarted();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchStarted);
	}

	@Override
	public void touchMoved() {
		if (this.currScene == null)
			return;

		this.currScene.touchMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchMoved);
	}

	@Override
	public void touchEnded() {
		if (this.currScene == null)
			return;

		this.currScene.touchEnded();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchEnded);
	}
	// endregion

	// region Window event callbacks.
	@Override
	public void resized() {
		if (this.currScene == null)
			return;

		this.currScene.resized();
		this.callOnCurrSceneActiveLayers(NerdLayer::resized);
	}

	@Override
	public void focusLost() {
		if (this.currScene == null)
			return;

		this.currScene.focusLost();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusLost);
	}

	@Override
	public void focusGained() {
		if (this.currScene == null)
			return;

		this.currScene.focusGained();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusGained);
	}

	@Override
	public void monitorChanged() {
		if (this.currScene == null)
			return;

		this.currScene.monitorChanged();
		this.callOnCurrSceneActiveLayers(NerdLayer::monitorChanged);
	}

	@Override
	public void fullscreenChanged(final boolean p_state) {
		if (this.currScene == null)
			return;

		this.currScene.fullscreenChanged(p_state);
		this.callOnCurrSceneActiveLayers(NerdLayer::fullscreenChanged, p_state);
	}
	// endregion

	// region Keyboard event callbacks.
	@Override
	public void keyTyped() {
		if (this.currScene == null)
			return;

		this.currScene.keyTyped();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyTyped);
	}

	@Override
	public void keyPressed() {
		if (this.currScene == null)
			return;

		this.currScene.keyPressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyPressed);
	}

	@Override
	public void keyReleased() {
		if (this.currScene == null)
			return;

		this.currScene.keyReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyReleased);
	}
	// endregion
	// endregion

	// region [`public`] Getters.
	public NerdSketch getSketch() {
		return super.SKETCH;
	}

	public boolean didSceneSwitchOccur() {
		return this.sceneSwitchOccured;
	}

	public NerdScene getCurrentScene() {
		return this.currScene;
	}

	public Class<? extends NerdScene> getCurrentSceneClass() {
		return this.currSceneClass;
	}

	public Class<? extends NerdScene> getPreviousSceneClass() {
		return this.prevSceneClass;
	}

	public NerdScenesModule.NerdSceneManagerSettings getManagerSettings() {
		return this.SETTINGS;
	}
	// endregion

	// region [`public`] Queries.
	public final void addSceneChangeListener(final NerdScenesModule.NerdSceneChangedListener p_listener) {
		this.SCENE_CHANGED_LISTENERS.add(p_listener);
	}

	public final void removeSceneChangeListener(final NerdScenesModule.NerdSceneChangedListener p_listener) {
		this.SCENE_CHANGED_LISTENERS_TO_REMOVE.add(p_listener);
	}

	/**
	 * Returns a {@link HashSet} of {@link NerdScene} classes including only classes
	 * instances of which this {@link NerdScenesModule} has ran.
	 */
	public final HashSet<Class<? extends NerdScene>> getKnownScenesSet() {
		return new HashSet<>(this.SCENE_CACHE.keySet());
	}
	// endregion

	// region `Scene`-operations.
	public int getTimesSceneLoaded(final Class<? extends NerdScene> p_sceneClass) {
		return this.SCENE_CACHE.get(p_sceneClass).timesLoaded;
	}

	// region Invoking the asset loader.
	// To those demanding var-arg versions of these `loadSceneAssets*()` methods:
	// "...no"! (I mean, should I just make a bean of some kind?)

	public void loadSceneAssetsAsync(final Class<? extends NerdScene> p_sceneClass) {
		this.loadSceneAssetsAsync(p_sceneClass, false);
	}

	public void loadSceneAssetsAsync(final Class<? extends NerdScene> p_sceneClass, final boolean p_forcibly) {
		if (!this.hasCached(p_sceneClass))
			this.cacheScene(p_sceneClass);

		if (this.givenSceneRanPreload(p_sceneClass))
			return;

		new Thread(() -> this.loadSceneAssets(p_sceneClass, p_forcibly),
				"NerdAsyncAssetLoader_" + this.getClass().getSimpleName()).start();
	}

	// Non-async versions:
	public void loadSceneAssets(final Class<? extends NerdScene> p_sceneClass) {
		this.loadSceneAssets(p_sceneClass, false);
	}

	public void loadSceneAssets(final Class<? extends NerdScene> p_sceneClass, final boolean p_forcibly) {
		if (!this.hasCached(p_sceneClass))
			this.cacheScene(p_sceneClass);

		if (this.givenSceneRanPreload(p_sceneClass))
			return;

		final NerdScenesModule.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(p_sceneClass);

		if (sceneCache != null) {
			if (sceneCache.cachedReference.hasCompletedPreload())
				return;

			this.loadSceneAssets(sceneCache.cachedReference, p_forcibly);
		}
	}
	// endregion

	// region Starting, or switching to a scene.
	public void restartScene() {
		this.restartScene(null);
	}

	public void restartScene(final NerdSceneState p_setupState) {
		if (this.currSceneClass == null)
			return;

		this.startSceneImpl(this.currSceneClass, p_setupState);
	}

	public void startPreviousScene() {
		this.startPreviousScene(null);
	}

	public void startPreviousScene(final NerdSceneState p_setupState) {
		if (this.prevSceneClass == null)
			return;

		final NerdScene toUse = this.constructScene(this.SCENE_CACHE.get(this.prevSceneClass).CONSTRUCTOR);

		if (toUse != null)
			this.setScene(toUse, p_setupState);
	}

	// "Cache if not cached" / "Start cached" method.
	// Used to experience these (now solved!) problems:
	/*
	 * - Asking for deletion permissions when you may not be caching is awkward,
	 * - Structure. `cache == null`, `cache.getCache() == null` must result in the
	 * same, but can't be grouped together logicaly, for optimization. This can be
	 * fixed with the use of an implementation method, but this class already has
	 * too many similarly-named methods!
	 *
	 * Another approach would be to call `NerdSceneManager::cacheScene()` then query
	 * `NerdSceneManager::SCENE_CACHE`, but that sounds even slower.
	 * Even with the JIT!
	 */

	/**
	 * Starts a {@link NerdScene}, and tells using the return value, whether it was
	 * restored from cache or started again.
	 */
	public boolean startScene(final Class<? extends NerdScene> p_sceneClass) {
		return this.startScene(p_sceneClass, null);
	}

	public boolean startScene(final Class<? extends NerdScene> p_sceneClass, final NerdSceneState p_setupState) {
		if (p_sceneClass == null)
			throw new NullPointerException("`SceneManager::startScene()` received `null`.");

		if (this.hasCached(p_sceneClass)) {
			this.setScene(this.SCENE_CACHE.get(p_sceneClass).cachedReference, p_setupState);
			return true;
		} else {
			this.startSceneImpl(p_sceneClass, p_setupState);
			return false;
		}

		/*
		 * // This is where `HashSet`s shine more than `ArrayList`s!:
		 * if (this.SCENE_CLASSES.add(p_sceneClass))
		 * this.startSceneImpl(p_sceneClass);
		 * else
		 * throw new IllegalArgumentException("""
		 * Use `SceneManager::restartScene()
		 * to restart a `NerdScene` while it runs!""");
		 */

	}
	// endregion

	// region `private` `NerdScene` operations.
	private boolean givenSceneRanPreload(final Class<? extends NerdScene> p_sceneClass) {
		final NerdScene sceneCache = this.SCENE_CACHE
				.get(p_sceneClass).cachedReference;

		// `SonarLint` did this optimization, yay!:
		return sceneCache != null && sceneCache.hasCompletedPreload();
		// return sceneCache == null ? false : sceneCache.hasCompletedPreload();
	}

	private void loadSceneAssets(final NerdScene p_scene, final boolean p_forcibly) {
		if (p_scene == null)
			return;

		// If forced to, do it:
		if (p_forcibly) {
			p_scene.runPreload();
			return;
		}

		final Class<? extends NerdScene> SCENE_CLASS = p_scene.getClass();

		// If this scene has never been loaded up before, preload the data!
		if (this.getTimesSceneLoaded(SCENE_CLASS) == 0) {
			// p_scene.ASSETS.clear(); // Not needed - this code will never have bugs. Hah!
			p_scene.runPreload();
			this.SCENE_CACHE.get(SCENE_CLASS).cachedAssets = p_scene.ASSETS;
			return;
		}

		// region Preloads other than the first one.
		// You're allowed to preload only once?
		// Don't re-load, just use the cache!:
		if (this.SETTINGS.ON_PRELOAD.preloadOnlyOnce) {
			final NerdAssetsModule man = this.SCENE_CACHE.get(SCENE_CLASS).cachedAssets;
			p_scene.ASSETS = man;
		} else { // Else, since you're supposed to run `preload()` every time, do that!:
			p_scene.ASSETS.clear();
			p_scene.runPreload();
			this.SCENE_CACHE.get(SCENE_CLASS).cachedAssets = p_scene.ASSETS;
		}
		// endregion

	}

	// region (`private`) Caching operations.
	private boolean hasCached(final Class<? extends NerdScene> p_sceneClass) {
		// If you haven't been asked to run the scene even once, you didn't cache it!
		// Say you haven't!:
		if (!this.SCENE_CACHE.containsKey(p_sceneClass))
			return false;

		// ...so you ran the scene? Great! ...BUT DO YOU HAVE THE SCENE OBJECT?!
		return !this.SCENE_CACHE.get(p_sceneClass).cacheIsNull();

		// Ugh, -_- this is cheating...:
		// return !this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference != null;
	}

	private void cacheScene(final Class<? extends NerdScene> p_sceneClass /* final boolean p_isDeletable */) {
		if (this.SCENE_CACHE.containsKey(p_sceneClass))
			return;

		if (this.constructScene(this.getSceneConstructor(p_sceneClass)) == null)
			throw new RuntimeException("The scene could not be constructed for caching.");
	}
	// endregion

	// region `private` construction-and-setup operations!
	private Constructor<? extends NerdScene> getSceneConstructor(final Class<? extends NerdScene> p_sceneClass) {
		try {
			return p_sceneClass.getConstructor();
		} catch (final NoSuchMethodException e) {
			System.err.println("""
					Every subclass of `NerdScene` must be `public` with a `public` \"null-constructor\"
					(constructor with no arguments), or no overriden constructors at all.""");
			return null;
		}
	}

	private NerdScene constructScene(final Constructor<? extends NerdScene> p_sceneConstructor) {
		if (p_sceneConstructor == null)
			return null;

		NerdScene toRet = null;

		// region Get an instance if possible!
		try {
			toRet = p_sceneConstructor.newInstance();
		} catch (final InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		// endregion

		// region Initialize it!
		final Class<? extends NerdScene> sceneClass = p_sceneConstructor.getDeclaringClass();
		final NerdScenesModule.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(sceneClass);

		// Initialize fields as if this was a part of the construction
		// (done here instead so your subclass doesn't have to write one!):
		toRet.MANAGER = this;
		toRet.SKETCH = toRet.MANAGER.SKETCH;

		// Now, we copy all managers from `toRet.SKETCH`:
		toRet.INPUT = toRet.SKETCH.getNerdModule(NerdInputModule.class);
		toRet.WINDOW = toRet.SKETCH.getNerdModule(NerdWindowModule.class);
		toRet.DISPLAY = toRet.SKETCH.getNerdModule(NerdDisplayModule.class);
		toRet.ECS = this.ECS_INSTANCE.clearAllData(); // Method's in `NerdBridgedEcsManager`.
		toRet.GRAPHICS = toRet.SKETCH.getNerdGraphics();
		toRet.CAMERA = toRet.GRAPHICS.getCurrentCamera();
		toRet.ASSETS = new NerdAssetsModule(toRet.SKETCH); // Is this actually a good idea?

		// If this is the first time we're constructing this scene,
		// ensure it has a cache and a saved state!
		if (sceneCache == null) {
			toRet.STATE = new NerdSceneState();
			this.SCENE_CACHE.put(sceneClass,
					new NerdScenesModule.NerdSceneManagerSceneCache(p_sceneConstructor, toRet));
		} else
			toRet.STATE = sceneCache.STATE;
		// endregion

		return toRet;
	}

	// Yes, this checks for errors.
	private void startSceneImpl(final Class<? extends NerdScene> p_sceneClass, final NerdSceneState p_state) {
		final Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

		final NerdScene toStart = this.constructScene(sceneConstructor);

		if (toStart != null)
			this.setScene(toStart, p_state);
		else
			throw new RuntimeException("The scene could not be constructed.");
	}

	// The scene-deleter!!!
	private void setScene(final NerdScene p_currentScene, final NerdSceneState p_state) {
		final NerdWindowModule window = super.SKETCH.getNerdModule(NerdWindowModule.class);
		window.cursorVisible = true;
		window.cursorConfined = false;

		// region `this.SETTINGS.ON_SWITCH` tasks.
		if (this.SETTINGS.ON_SWITCH.doClear) {
			if (this.SETTINGS.ON_SWITCH.clearColor == -1)
				super.SKETCH.clear();
			else
				super.SKETCH.background(this.SETTINGS.ON_SWITCH.clearColor);
		}

		if (this.SETTINGS.ON_SWITCH.resetSceneLayerCallbackOrder) {
			this.SETTINGS.preFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.SCENE;
			this.SETTINGS.drawFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
			this.SETTINGS.postFirstCaller = NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
		}
		// endregion

		this.prevSceneClass = this.currSceneClass;
		this.SCENE_CHANGED_LISTENERS.forEach(
				l -> l.sceneChanged(super.SKETCH, this.prevSceneClass, this.currSceneClass));

		if (this.prevSceneClass != null) {
			// Exit the scene, and nullify the cache.
			this.currScene.runSceneChanged();

			// Do not clear! Theyr're cached!
			// if (!this.hasCached(this.currSceneClass))
			// this.currScene.ASSETS.clear();

			final NerdScenesModule.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(this.currSceneClass);
			sceneCache.nullifyCache();
			System.gc();

			// What `nullifyCache()` is supposed to do:
			/*
			 * // Delete the scene reference if needed:
			 * SceneManager.SceneCache oldSceneManager.SceneCache =
			 * this.SCENE_CACHE.get(this.previousSceneClass);
			 * if (!oldSceneManager.SceneCache.doNotDelete)
			 * oldSceneManager.SceneCache.deleteCache();
			 * // If this was the only reference to the scene object, the scene gets GCed!
			 */
		}

		this.currSceneClass = p_currentScene.getClass();
		this.currScene = p_currentScene;
		this.setupCurrentScene(p_state);
	}

	// Set the time, *then* call `SceneManager::runSetup()`.
	private void setupCurrentScene(final NerdSceneState p_state) {
		this.sceneSwitchOccured = true;
		this.loadSceneAssets(this.currScene, false);
		this.SCENE_CACHE.get(this.currSceneClass).timesLoaded++;

		// Helps in resetting style and transformation info across scenes! YAY!:
		if (this.prevSceneClass != null)
			super.SKETCH.pop();

		// We push and pop the style and transforms to auto-reset. Hah!:
		super.SKETCH.push();
		super.SKETCH.textFont(super.SKETCH.getDefaultFont()); // ...Also reset the font. Thanks!

		this.SCENE_CHANGED_LISTENERS.removeAll(this.SCENE_CHANGED_LISTENERS_TO_REMOVE);

		// This is `null` in SO MANY PLACES!:
		if (p_state == null)
			this.currScene.runSetup(new NerdSceneState());
		else
			this.currScene.runSetup(p_state);
	}
	// endregion
	// endregion
	// endregion

}
