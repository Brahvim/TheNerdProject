package com.brahvim.nerd.framework.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.ecs.NerdEcsManager;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdSceneManager {

	// region Inner classes.
	// My code style: If it is an inner class, also write the name of the outer
	// class. I do this to aid reading and to prevent namespace pollution.

	@FunctionalInterface
	public interface NerdSceneChangeListener {
		public void sceneChanged(NerdSketch sketch,
				Class<? extends NerdScene> previous,
				Class<? extends NerdScene> current);
	}

	/**
	 * Stores scene data while a scene is not active.
	 */
	private static class NerdSceneManagerSceneCache {

		// region Fields.
		private final int timesLoaded = 0;
		private final NerdSceneState STATE;
		private final Constructor<? extends NerdScene> CONSTRUCTOR;

		// private NerdEcsManager cachedEcs; // Nope! If the user really wants it,
		// they'll should get stuff serialized via `NerdEcsManager` and handle it...

		private NerdScene cachedReference; // `NerdSceneManager` deletes this when the scene exits.
		private NerdAssetManager cachedAssets;
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

		public NerdEcsSystem<?>[] orderOfEcsSystems;

		// region App workflow callbacks scene-or-layer order.
		/**
		 * Dictates to every {@link NerdSceneManager} instance, the order in which a
		 * {@link NerdScene} or {@link NerdLayer} is allowed to call certain "workflow
		 * events" ({@link NerdScene#pre()}, {@link NerdScene#draw()} and
		 * {@link NerdScene#post()}) from Processing.
		 * 
		 * @see {@link NerdSceneManager.NerdSceneManagerSettings#preFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#SCENE} by
		 *      default.
		 * @see {@link NerdSceneManager.NerdSceneManagerSettings#drawFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#LAYER} by
		 *      default.
		 * @see {@link NerdSceneManager.NerdSceneManagerSettings#postFirstCaller} -
		 *      {@link NerdSketchCallbackOrder#LAYER} by
		 *      default.
		 */
		public /* static */ enum NerdSketchCallbackOrder {
			SCENE(), LAYER();
		}

		/**
		 * Controls whether {@link NerdScene#pre()} or {@link NerdLayer#pre()} is
		 * called first by the {@link NerdSceneManager}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#SCENE}.
		 */
		public NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder preFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.SCENE;

		/**
		 * Controls whether {@link NerdScene#draw()} or {@link NerdLayer#draw()} is
		 * called first by the {@link NerdSceneManager}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#LAYER}.
		 */
		public NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder drawFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;

		/**
		 * Controls whether {@link NerdScene#post()} or {@link NerdLayer#post()} is
		 * called first by the {@link NerdSceneManager}. If the value of this field is
		 * ever
		 * {@code null}, it is set to its default,
		 * {@link NerdSketchCallbackOrder#LAYER}.
		 */
		public NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder postFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
		// endregion

		public class OnScenePreload {

			private OnScenePreload() {
			}

			/**
			 * When {@code true}, {@link NerdScene#preload()} is run only the first time
			 * the {@link NerdScene} is used. Turn to {@code false} to load scene assets
			 * each time, so that assets are updated.
			 * 
			 * @apiNote {@code true} by default!
			 */
			public volatile boolean preloadOnlyOnce = true;

			/**
			 * When {@code true}, {@link NerdScene#preload()} runs the loading process in
			 * multiple threads using a {@link java.util.concurrent.ExecutorService}. If
			 * {@link NerdSceneManager.NerdSceneManagerSettings.OnScenePreload#completeWithinPreloadCall}
			 * is {@code false}, the asset loading is not guaranteed to finish within
			 * {@link NerdScene#preload()}.
			 * 
			 * @apiNote {@code true} by default!
			 * @implNote Actually, it's {@link NerdScene#runPreload()}.
			 */
			public volatile boolean useExecutors = true;

			/**
			 * @apiNote {@code true} by default!
			 */
			public volatile boolean completeWithinPreloadCall = true;

			/**
			 * The maximum number of threads multithreaded asset loading started
			 * in {@link NerdScene#preload()} can use.
			 * 
			 * @apiNote {@code 6} by default!
			 */
			public volatile int maxExecutorThreads = 6;

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
			 * {@link NerdSceneManager.NerdSceneManagerSettings.OnSceneSwitch#clearColor}.
			 *
			 * @apiNote {@code false} by default.
			 */
			public volatile boolean doClear = false;

			/**
			 * Resets {@link NerdSceneManager.NerdSceneManagerSettings#preFirstCaller},
			 * {@link NerdSceneManager.NerdSceneManagerSettings#drawFirstCaller}, and
			 * {@link NerdSceneManager.NerdSceneManagerSettings#postFirstCaller} to their
			 * default values!
			 */
			public volatile boolean resetSceneLayerCallbackOrder = true;

		}

		public final NerdSceneManager.NerdSceneManagerSettings.OnSceneSwitch ON_SWITCH = new OnSceneSwitch();

		public final NerdSceneManager.NerdSceneManagerSettings.OnScenePreload ON_PRELOAD = new OnScenePreload();

	}
	// endregion

	public final NerdSceneManager.NerdSceneManagerSettings SETTINGS;

	// region `protected` and `private` fields.
	protected final NerdBridgedEcsManager ECS_INSTANCE;
	protected boolean sceneSwitchOccured;
	protected NerdScene currScene;

	/**
	 * This {@link HashMap} contains cached data about each {@link NerdScene} class
	 * any {@link NerdSceneManager} instance has cached or ran.
	 * 
	 * <p>
	 * Actual "caching" of a {@link NerdScene} is when its corresponding
	 * {@link NerdSceneManager.NerdSceneManagerSceneCache#cachedReference} is not
	 * {@code null}.
	 * 
	 * <p>
	 * The initial capacity here ({@code 2}) is to aid performance, since, the JIT
	 * does no optimization till the first scene switch. All scene switches after
	 * that the initial should be fast enough!
	 */
	private final HashMap<Class<? extends NerdScene>, NerdSceneManager.NerdSceneManagerSceneCache> SCENE_CACHE = new HashMap<>(
			2);
	private final NerdSketch SKETCH;

	// Notes on some strange (useless? Useful?!) idea.
	/*
	 * Keep track of what `Sketch`es a manager exists for.
	 * `private static final HashSet<? extends Sketch> SKETCHES = new HashSet<>(1);
	 * "Don't let anybody build another!" <-- ...idea I've stopped.
	 */

	// region Sketch Event Listeners.
	private final LinkedHashSet<NerdSceneChangeListener> SCENE_CHANGE_LISTENERS; // May get passed to constructor!
	private final LinkedHashSet<NerdSceneChangeListener> SCENE_CHANGE_LISTENERS_TO_REMOVE = new LinkedHashSet<>(0);
	// endregion
	private Class<? extends NerdScene> currSceneClass, prevSceneClass;
	// endregion

	public NerdSceneManager(
			final NerdSketch p_sketch,
			final NerdSceneManager.NerdSceneManagerSettings p_settings,
			final LinkedHashSet<NerdSceneManager.NerdSceneChangeListener> p_listeners,
			final NerdEcsSystem<?>[] p_ecsSystems) {
		this.SKETCH = p_sketch;
		this.SETTINGS = p_settings;
		this.SCENE_CHANGE_LISTENERS = p_listeners;
		this.SETTINGS.orderOfEcsSystems = p_ecsSystems;
		this.ECS_INSTANCE = new NerdBridgedEcsManager(this.SKETCH, this.SETTINGS.orderOfEcsSystems);
	}

	// region Workflow callbacks.
	protected void runPre() {
		if (this.currScene != null)
			this.currScene.runPre();
	}

	protected void runPost() {
		if (this.currScene != null)
			this.currScene.runPost();

		this.sceneSwitchOccured = false;
	}

	protected void runDraw() {
		if (this.currScene != null)
			this.currScene.runDraw();
	}

	protected void runExit() {
		if (this.currScene != null)
			this.currScene.runExit();
	}

	protected void runDispose() {
		if (this.currScene != null)
			this.currScene.runDispose();
	}

	// Too expensive! Need a `push()` and `pop()`.
	/*
	 * protected void runPreDraw() {
	 * if (this.currScene != null)
	 * this.currScene.runPreDraw();
	 * }
	 * 
	 * protected void runPostDraw() {
	 * if (this.currScene != null)
	 * this.currScene.runPostDraw();
	 * }
	 */
	// endregion

	// region Event callbacks. Passed to the ECS first, THEN the scene!
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
	protected void mousePressed() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mousePressed();

		this.currScene.mousePressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::mousePressed);
	}

	protected void mouseReleased() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mouseReleased();

		this.currScene.mouseReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseReleased);
	}

	protected void mouseMoved() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mouseMoved();

		this.currScene.mouseMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseMoved);
	}

	protected void mouseClicked() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mouseClicked();

		this.currScene.mouseClicked();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseClicked);
	}

	protected void mouseDragged() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mouseDragged();

		this.currScene.mouseDragged();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseDragged);
	}

	protected void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.mouseWheel(p_mouseEvent);

		this.currScene.mouseWheel(p_mouseEvent);
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseWheel, p_mouseEvent);
	}
	// endregion

	// region Touch event callbacks.
	protected void touchStarted() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.touchStarted();

		this.currScene.touchStarted();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchStarted);
	}

	protected void touchMoved() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.touchMoved();

		this.currScene.touchMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchMoved);
	}

	protected void touchEnded() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.touchEnded();

		this.currScene.touchEnded();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchEnded);
	}
	// endregion

	// region Window event callbacks.
	protected void resized() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.resized();

		this.currScene.resized();
		this.callOnCurrSceneActiveLayers(NerdLayer::resized);
	}

	protected void focusLost() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.focusLost();

		this.currScene.focusLost();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusLost);
	}

	protected void focusGained() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.focusGained();

		this.currScene.focusGained();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusGained);
	}

	protected void monitorChanged() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.monitorChanged();

		this.currScene.monitorChanged();
		this.callOnCurrSceneActiveLayers(NerdLayer::monitorChanged);
	}

	protected void fullscreenChanged(final boolean p_state) {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.fullscreenChanged(p_state);

		this.currScene.fullscreenChanged(p_state);
		this.callOnCurrSceneActiveLayers(NerdLayer::fullscreenChanged, p_state);
	}
	// endregion

	// region Keyboard event callbacks.
	protected void keyTyped() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.keyTyped();

		this.currScene.keyTyped();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyTyped);
	}

	protected void keyPressed() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.keyPressed();

		this.currScene.keyPressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyPressed);
	}

	protected void keyReleased() {
		if (this.currScene == null)
			return;

		if (this.currScene.ECS != null)
			this.currScene.ECS.keyReleased();

		this.currScene.keyReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyReleased);
	}
	// endregion
	// endregion

	// region [`public`] Getters.
	public NerdSketch getSketch() {
		return this.SKETCH;
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

	public NerdSceneManager.NerdSceneManagerSettings getManagerSettings() {
		return this.SETTINGS;
	}
	// endregion

	// region [`public`] Queries.
	public final void addSceneChangeListener(final NerdSceneManager.NerdSceneChangeListener p_listener) {
		this.SCENE_CHANGE_LISTENERS.add(p_listener);
	}

	public final void removeSceneChangeListener(final NerdSceneManager.NerdSceneChangeListener p_listener) {
		this.SCENE_CHANGE_LISTENERS_TO_REMOVE.add(p_listener);
	}

	/**
	 * Returns a {@link HashSet} of {@link NerdScene} classes including only classes
	 * instances of which this {@link NerdSceneManager} has ran.
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

		final var manager = this;
		new Thread("NerdAsyncAssetLoader_" + this.getClass().getSimpleName()) {
			@Override
			public void run() { // Lambdas perform horribly!
				manager.loadSceneAssets(p_sceneClass, p_forcibly);
			}
		}.start();
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

		final NerdSceneManager.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(p_sceneClass);

		if (sceneCache != null)
			if (sceneCache.cachedReference.hasCompletedPreload())
				return;

		this.loadSceneAssets(sceneCache.cachedReference, p_forcibly);
	}
	// endregion

	// region Starting, or switching to a scene.
	public void restartScene() {
		this.restartScene(null);
	}

	public void restartScene(final NerdSceneState p_setupState) {
		if (this.currSceneClass == null)
			return;

		// SceneManager.SceneCache data =
		// this.SCENE_CLASS_TO_CACHE.get(this.currSceneClass);
		// NerdScene toUse = this.constructAndCacheScene(data.CONSTRUCTOR);
		this.startSceneImpl(this.currSceneClass, p_setupState);
	}

	public void startPreviousScene() {
		this.startPreviousScene(null);
	}

	public void startPreviousScene(final NerdSceneState p_setupState) {
		if (this.prevSceneClass == null)
			return;

		final NerdSceneManager.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(this.prevSceneClass);
		final NerdScene toUse = this.constructScene(sceneCache.CONSTRUCTOR);

		this.setScene(toUse, p_setupState);
	}

	// "Cache if not cached" / "Start cached" method.
	// Used to experience these (now solved!) problems:
	/*
	 * - Asking for deletion permissions when you may not be caching is awkward,
	 * - Structure. `cache == null`, `cache.getCache() == null` must result in the
	 * same, but can't be grouped together logicaly, for optimization. This can be
	 * fixed with the use of an "impl" method, but this class already has too many
	 * similarly-named methods!
	 * 
	 * Another approach would be to call `SceneManager::cacheScene()` then query
	 * `SceneManager::SCENE_CACHE`, but that sounds even slower. Even with the JIT!
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
			final NerdAssetManager man = this.SCENE_CACHE.get(SCENE_CLASS).cachedAssets;
			p_scene.ASSETS = man;
		} else { // Else, since you're supposed to run `preload()` every time, do that!:
			p_scene.ASSETS.clear();
			p_scene.runPreload();
			this.SCENE_CACHE.get(SCENE_CLASS).cachedAssets = p_scene.ASSETS;
		}
		// endregion

	}

	// region (`private`) Caching operations.
	private void ensureCache(final Class<? extends NerdScene> p_sceneClass) {
		if (!this.hasCached(p_sceneClass))
			this.cacheScene(p_sceneClass);
	}

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

		final Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

		final NerdScene toCache = this.constructScene(sceneConstructor);

		if (toCache == null)
			throw new RuntimeException("The scene could not be constructed for caching.");
	}
	// endregion

	// region `private` construction-and-setup operations!
	private Constructor<? extends NerdScene> getSceneConstructor(final Class<? extends NerdScene> p_sceneClass) {
		Constructor<? extends NerdScene> toRet = null;

		try {
			toRet = p_sceneClass.getConstructor();
		} catch (final NoSuchMethodException e) {
			System.err.println("""
					Every subclass of `NerdScene` must be `public` with a `public` \"null-constructor\"
					(constructor with no arguments), or no overriden constructors at all.""");
			// e.printStackTrace();
		}

		return toRet;
	}

	private NerdScene constructScene(final Constructor<? extends NerdScene> p_sceneConstructor) {
		NerdScene toRet = null;

		// region Get an instance.
		try {
			toRet = p_sceneConstructor.newInstance();
		} catch (final InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		// endregion

		// region Initialize it!
		final Class<? extends NerdScene> sceneClass = p_sceneConstructor.getDeclaringClass();
		final NerdSceneManager.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(sceneClass);

		// Initialize fields as if this was a part of the construction
		// (done here instead so your subclass doesn't have to write one!):
		toRet.MANAGER = this;
		toRet.SKETCH = toRet.MANAGER.SKETCH;

		// Now, we copy all managers from `toRet.SKETCH`:
		toRet.INPUT = toRet.SKETCH.INPUT;
		toRet.WINDOW = toRet.SKETCH.WINDOW;
		toRet.DISPLAY = toRet.SKETCH.DISPLAYS;
		toRet.ECS = this.ECS_INSTANCE.clearAllData();
		toRet.GRAPHICS = toRet.SKETCH.getNerdGraphics();
		toRet.CAMERA = toRet.SKETCH.setCameraToDefault();
		toRet.ASSETS = new NerdAssetManager(toRet.SKETCH); // Is this actually a good idea?

		// If this is the first time we're constructing this scene,
		// ensure it has a cache and a saved state!
		if (sceneCache == null) {
			toRet.STATE = new NerdSceneState();
			this.SCENE_CACHE.put(sceneClass,
					new NerdSceneManager.NerdSceneManagerSceneCache(p_sceneConstructor, toRet));
		} else
			toRet.STATE = sceneCache.STATE;
		// endregion

		return toRet;
	}

	// Yes, this checks for errors.
	private void startSceneImpl(final Class<? extends NerdScene> p_sceneClass, final NerdSceneState p_state) {
		final Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

		final NerdScene toStart = this.constructScene(sceneConstructor);

		if (toStart == null)
			throw new RuntimeException("The scene could not be constructed.");

		this.setScene(toStart, p_state);
	}

	// The scene-deleter!!!
	private void setScene(final NerdScene p_currentScene, final NerdSceneState p_state) {
		this.SKETCH.WINDOW.cursorVisible = true;
		this.SKETCH.WINDOW.cursorConfined = false;

		// region `this.SETTINGS.ON_SWITCH` tasks.
		if (this.SETTINGS.ON_SWITCH.doClear) {
			if (this.SETTINGS.ON_SWITCH.clearColor == -1)
				this.SKETCH.clear();
			else
				this.SKETCH.background(this.SETTINGS.ON_SWITCH.clearColor);
		}

		if (this.SETTINGS.ON_SWITCH.resetSceneLayerCallbackOrder) {
			this.SETTINGS.preFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.SCENE;
			this.SETTINGS.drawFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
			this.SETTINGS.postFirstCaller = NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder.LAYER;
		}
		// endregion

		this.prevSceneClass = this.currSceneClass;
		if (this.prevSceneClass != null) {
			// Exit the scene, and nullify the cache.
			this.currScene.runSceneChanged();

			// Do not clear! Theyr're cached!
			// if (!this.hasCached(this.currSceneClass))
			// this.currScene.ASSETS.clear();

			final NerdSceneManager.NerdSceneManagerSceneCache sceneCache = this.SCENE_CACHE.get(this.currSceneClass);
			sceneCache.nullifyCache();

			// What `deleteCacheIfCan()` did, I guess (or used to do)!:
			/*
			 * // Delete the scene reference if needed:
			 * SceneManager.SceneCache oldSceneManager.SceneCache =
			 * this.SCENE_CACHE.get(this.previousSceneClass);
			 * if (!oldSceneManager.SceneCache.doNotDelete)
			 * oldSceneManager.SceneCache.deleteCache();
			 * // If this was the only reference to the scene object, the scene gets GCed!
			 * System.gc();
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

		final boolean prevSceneClassNotNull = this.prevSceneClass != null;

		// Helps in resetting style and transformation info across scenes! YAY!:
		if (prevSceneClassNotNull)
			this.SKETCH.pop();

		this.SKETCH.push();

		this.SKETCH.textFont(this.SKETCH.getDefaultFont());

		this.SCENE_CHANGE_LISTENERS.removeAll(this.SCENE_CHANGE_LISTENERS_TO_REMOVE);

		this.currScene.runSetup(p_state);
	}
	// endregion
	// endregion
	// endregion

}
