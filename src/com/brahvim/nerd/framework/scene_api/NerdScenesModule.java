package com.brahvim.nerd.framework.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdModuleSettings;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;
import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.brahvim.nerd.window_management.NerdWindowModule;

public class NerdScenesModule extends NerdModule {

	// region Inner classes.
	// My code style: If it is an inner class, also write the name of the outer
	// class. I do this to aid reading and to prevent namespace pollution.

	@FunctionalInterface
	public interface NerdScenesModuleNewSceneStartedListener {
		void sceneChanged(
				NerdScenesModule p_scenesModule,
				Class<? extends NerdScene<?>> p_previousSceneClass,
				Class<? extends NerdScene<?>> p_currentSceneClass);
	}

	/**
	 * Stores scene data while a scene is not active. This is done for purposes such
	 * as loading assets for the scene before it even starts!
	 */
	/* `package` */ class NerdScenesModuleSceneCache {

		// region Fields.
		/* `package */ int timesLoaded = 0;
		/* `package */ final NerdSceneState STATE;
		/* `package */ final Constructor<? extends NerdScene<?>> CONSTRUCTOR;

		// private NerdEcsModule cachedEcs; // Nope! If the user really wants it,
		// they'll should get stuff serialized via `NerdEcsModule` and handle it...

		/* `package */ NerdScene<?> cachedReference;
		// `NerdSceneModule` deletes this when the scene exits.
		/* `package */ NerdAssetsModule cachedAssets;
		// endregion

		/* `package */ NerdScenesModuleSceneCache(
				final Constructor<? extends NerdScene<?>> p_constructor,
				final NerdScene<?> p_cachedReference) {
			this.CONSTRUCTOR = p_constructor;
			this.cachedReference = p_cachedReference;
			this.STATE = this.cachedReference.STATE;
		}

		// region Cache queries.
		/* `package */ boolean isSceneCacheNull() {
			return this.cachedReference == null;
		}

		/* `package */ void /* deleteCache() { */ nullifyCache() {
			// If this was (hopefully) the only reference to the scene object, it gets GCed!
			this.cachedReference = null;
			System.gc();
		}
		// endregion

	}
	// endregion

	protected NerdScenesModuleSettings scenesModuleSettings;

	// region `protected` and `private` fields.

	protected NerdScene<?> currentScene;
	protected boolean sceneSwitchOccurred;

	/**
	 * This {@link Map} contains cached data about each
	 * {@link NerdScene<SceneGraphicsT>} class
	 * any {@link NerdScenesModule} instance has cached or ran.
	 * <p>
	 * Actual "caching" of a {@link NerdScene<SceneGraphicsT>} is when its
	 * corresponding
	 * {@link NerdScenesModule.NerdScenesModuleSceneCache#cachedReference} is not
	 * {@code null}.
	 * <p>
	 * The initial capacity here ({@code 2}) is to aid performance, since, the JIT
	 * does no optimization till the first scene switch. All scene switches after
	 * that the initial should be fast enough!
	 */
	private final Map<Class<? extends NerdScene<?>>, NerdScenesModule.NerdScenesModuleSceneCache> //
	SCENE_CACHE = new HashMap<>(2);

	private final Set<NerdScenesModule.NerdScenesModuleNewSceneStartedListener> //
	SCENE_CHANGED_LISTENERS = new LinkedHashSet<>(0); // Not gunna have any, will we?

	private final Set<NerdScenesModule.NerdScenesModuleNewSceneStartedListener> //
	SCENE_CHANGED_LISTENERS_TO_REMOVE = new LinkedHashSet<>(0); // Not gunna have any, will we?

	private Class<? extends NerdScene<?>> currentSceneClass, previousSceneClass;
	// endregion

	public <SceneGraphicsT extends NerdGenericGraphics> NerdScenesModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	@Override
	protected void assignModuleSettings(final NerdModuleSettings<?> p_settings) {
		if (p_settings instanceof final NerdScenesModuleSettings settings) {
			this.scenesModuleSettings = settings;
		} else {
			this.scenesModuleSettings = new NerdScenesModuleSettings(null);
		}
	}

	// region `NerdSketch` workflow callbacks.
	@Override
	protected void pre() {
		if (this.currentScene != null)
			this.currentScene.runPre();
	}

	@Override
	protected void post() {
		if (this.currentScene != null)
			this.currentScene.runPost();

		this.sceneSwitchOccurred = false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void draw() {
		if (super.SKETCH.frameCount == 1 && this.currentScene == null) {
			if (this.scenesModuleSettings.FIRST_SCENE_CLASS == null)
				System.err.println("There is no initial `"
						+ NerdScene.class.getSimpleName()
						+ "` to show!");
			else
				this.startScene((Class<NerdScene<?>>) this.scenesModuleSettings.FIRST_SCENE_CLASS);
		}

		if (this.currentScene != null)
			this.currentScene.runDraw();
	}

	@Override
	protected void exit() {
		if (this.currentScene != null)
			this.currentScene.runExit();
	}

	@Override
	protected void dispose() {
		if (this.currentScene != null)
			this.currentScene.runDispose();
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

	// region Event callbacks. Passed to the ECS first, THEN here!
	// `NerdLayer` callers:
	protected void callOnCurrSceneActiveLayers(final Consumer<NerdLayer> p_eventCallbackMethod) {
		if (p_eventCallbackMethod != null)
			for (final NerdLayer l : this.currentScene.getLayers())
				if (l != null)
					if (l.isActive())
						p_eventCallbackMethod.accept(l);
	}

	protected <OtherArgT> void callOnCurrSceneActiveLayers(
			final BiConsumer<NerdLayer, OtherArgT> p_eventCallbackMethod, final OtherArgT p_otherArg) {
		if (p_eventCallbackMethod != null)
			for (final NerdLayer l : this.currentScene.getLayers())
				if (l != null)
					if (l.isActive())
						p_eventCallbackMethod.accept(l, p_otherArg);
	}

	// region Mouse event callbacks.
	@Override
	public void mousePressed() {
		if (this.currentScene == null)
			return;

		this.currentScene.mousePressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::mousePressed);
	}

	@Override
	public void mouseReleased() {
		if (this.currentScene == null)
			return;

		this.currentScene.mouseReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseReleased);
	}

	@Override
	public void mouseMoved() {
		if (this.currentScene == null)
			return;

		this.currentScene.mouseMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseMoved);
	}

	@Override
	public void mouseClicked() {
		if (this.currentScene == null)
			return;

		this.currentScene.mouseClicked();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseClicked);
	}

	@Override
	public void mouseDragged() {
		if (this.currentScene == null)
			return;

		this.currentScene.mouseDragged();
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseDragged);
	}

	@Override
	public void mouseWheel(
			final processing.event.MouseEvent p_mouseEvent) {
		if (this.currentScene == null)
			return;

		this.currentScene.mouseWheel(p_mouseEvent);
		this.callOnCurrSceneActiveLayers(NerdLayer::mouseWheel, p_mouseEvent);
	}
	// endregion

	// region Touch event callbacks.
	@Override
	protected void touchStarted() {
		if (this.currentScene == null)
			return;

		this.currentScene.touchStarted();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchStarted);
	}

	@Override
	protected void touchMoved() {
		if (this.currentScene == null)
			return;

		this.currentScene.touchMoved();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchMoved);
	}

	@Override
	protected void touchEnded() {
		if (this.currentScene == null)
			return;

		this.currentScene.touchEnded();
		this.callOnCurrSceneActiveLayers(NerdLayer::touchEnded);
	}
	// endregion

	// region Window event callbacks.
	@Override
	protected void resized() {
		if (this.currentScene == null)
			return;

		this.currentScene.resized();
		this.callOnCurrSceneActiveLayers(NerdLayer::resized);
	}

	@Override
	protected void focusLost() {
		if (this.currentScene == null)
			return;

		this.currentScene.focusLost();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusLost);
	}

	@Override
	protected void focusGained() {
		if (this.currentScene == null)
			return;

		this.currentScene.focusGained();
		this.callOnCurrSceneActiveLayers(NerdLayer::focusGained);
	}

	@Override
	protected void monitorChanged() {
		if (this.currentScene == null)
			return;

		this.currentScene.monitorChanged();
		this.callOnCurrSceneActiveLayers(NerdLayer::monitorChanged);
	}

	@Override
	protected void fullscreenChanged(final boolean p_state) {
		if (this.currentScene == null)
			return;

		this.currentScene.fullscreenChanged(p_state);
		this.callOnCurrSceneActiveLayers(NerdLayer::fullscreenChanged, p_state);
	}
	// endregion

	// region Keyboard event callbacks.
	@Override
	public void keyTyped() {
		if (this.currentScene == null)
			return;

		this.currentScene.keyTyped();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyTyped);
	}

	@Override
	public void keyPressed() {
		if (this.currentScene == null)
			return;

		this.currentScene.keyPressed();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyPressed);
	}

	@Override
	public void keyReleased() {
		if (this.currentScene == null)
			return;

		this.currentScene.keyReleased();
		this.callOnCurrSceneActiveLayers(NerdLayer::keyReleased);
	}
	// endregion
	// endregion

	// region [`public`] Getters.
	@SuppressWarnings("unchecked")
	public <SceneGraphicsT extends NerdGenericGraphics> NerdScene<SceneGraphicsT> getCurrentScene() {
		return (NerdScene<SceneGraphicsT>) this.currentScene;
	}

	public boolean didSceneSwitchOccur() {
		return this.sceneSwitchOccurred;
	}

	@SuppressWarnings("unchecked")
	public <SceneGraphicsT extends NerdGenericGraphics> Class<? extends NerdScene<SceneGraphicsT>> getCurrentSceneClass() {
		return (Class<? extends NerdScene<SceneGraphicsT>>) this.currentSceneClass;
	}

	@SuppressWarnings("unchecked")
	public <SceneGraphicsT extends NerdGenericGraphics> Class<? extends NerdScene<SceneGraphicsT>> getPreviousSceneClass() {
		return (Class<? extends NerdScene<SceneGraphicsT>>) this.previousSceneClass;
	}

	public NerdScenesModuleSettings getScenesModuleSettings() {
		return this.scenesModuleSettings;
	}
	// endregion

	// region [`public`] Queries.
	/**
	 * Adds a {@link NerdScenesModule.NerdScenesModuleNewSceneStartedListener} that
	 * allows you to track when a scene starts!
	 *
	 * @param p_listener is the listener you want to add.
	 */
	public final void addNewSceneStartedListener(
			final NerdScenesModule.NerdScenesModuleNewSceneStartedListener p_listener) {
		if (p_listener != null)
			this.SCENE_CHANGED_LISTENERS.add(p_listener);
	}

	/**
	 * Removes a {@link NerdScenesModule.NerdScenesModuleNewSceneStartedListener}
	 * that
	 * would otherwise allow you to track when a scene starts.
	 *
	 * @param p_listener is the listener you want to remove.
	 */
	public final void removeNewSceneStartedListener(
			final NerdScenesModule.NerdScenesModuleNewSceneStartedListener p_listener) {
		this.SCENE_CHANGED_LISTENERS_TO_REMOVE.add(p_listener);
	}

	/**
	 * Returns a {@link HashSet} of {@link NerdScene<SceneGraphicsT>} classes
	 * including only
	 * classes
	 * instances of which this {@link NerdScenesModule} has ran.
	 */
	public final Set<Class<? extends NerdScene<?>>> getKnownScenesSet() {
		return new HashSet<>(this.SCENE_CACHE.keySet());
	}
	// endregion

	// region `Scene`-operations.
	public <SceneGraphicsT extends NerdGenericGraphics> int getTimesSceneLoaded(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		return this.SCENE_CACHE.get(p_sceneClass).timesLoaded;
	}

	// region Invoking the asset loader.
	// To those demanding var-arg versions of these `loadSceneAssets*()` methods:
	// "...no"! (I mean, should I just make a bean of some kind?)

	public <SceneGraphicsT extends NerdGenericGraphics> void loadSceneAssetsAsync(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		this.loadSceneAssetsAsync(p_sceneClass, false);
	}

	public <SceneGraphicsT extends NerdGenericGraphics> void loadSceneAssetsAsync(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass,
			final boolean p_forcibly) {
		if (!this.hasCached(p_sceneClass))
			this.cacheScene(p_sceneClass);

		if (this.givenSceneRanPreload(p_sceneClass))
			return;

		new Thread(() -> this.loadSceneAssets(p_sceneClass, p_forcibly),
				"NerdAsyncAssetLoader_" + this.getClass().getSimpleName()).start();
	}

	// Non-async versions:
	public <SceneGraphicsT extends NerdGenericGraphics> void loadSceneAssets(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		this.loadSceneAssets(p_sceneClass, false);
	}

	public <SceneGraphicsT extends NerdGenericGraphics> void loadSceneAssets(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass,
			final boolean p_forcibly) {
		if (!this.hasCached(p_sceneClass))
			this.cacheScene(p_sceneClass);

		if (this.givenSceneRanPreload(p_sceneClass))
			return;

		final NerdScenesModule.NerdScenesModuleSceneCache sceneCache = this.SCENE_CACHE.get(p_sceneClass);

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
		if (this.currentSceneClass == null)
			return;

		this.startSceneImpl(this.currentSceneClass, p_setupState);
	}

	public void startPreviousScene() {
		this.startPreviousScene(null);
	}

	@SuppressWarnings("unchecked")
	public <SceneGraphicsT extends NerdGenericGraphics> void startPreviousScene(final NerdSceneState p_setupState) {
		if (this.previousSceneClass == null)
			return;

		final NerdScene<SceneGraphicsT> toUse = (NerdScene<SceneGraphicsT>) this
				.constructAndInitScene(this.SCENE_CACHE.get(this.previousSceneClass).CONSTRUCTOR);
		this.setScene(toUse, p_setupState);
	}

	// "Cache if not cached" / "Start cached" method.
	// Used to experience these (now solved!) problems:
	/*
	 * - Asking for deletion permissions when you may not be caching is awkward,
	 * - Structure. `cache == null`, `cache.getCache() == null` must result in the
	 * same, but can't be grouped together logically, for optimization. This can be
	 * fixed with the use of an implementation method, but this class already has
	 * too many similarly-named methods!
	 *
	 * Another approach would be to call `NerdSceneModule::cacheScene()` then query
	 * `NerdSceneModule::SCENE_CACHE`, but that sounds even slower.
	 * Even with the JIT!
	 */

	/**
	 * Starts a {@link NerdScene<SceneGraphicsT>}, and tells using the return value,
	 * whether it
	 * was
	 * restored from cache or started again.
	 */
	public <SceneGraphicsT extends NerdGenericGraphics> boolean startScene(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		return this.startScene(p_sceneClass, null);
	}

	public <SceneGraphicsT extends NerdGenericGraphics> boolean startScene(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass, final NerdSceneState p_setupState) {
		if (p_sceneClass == null)
			throw new NullPointerException(
					"`"
							+ NerdScenesModule.class.getSimpleName()
							+ "`::startScene()` received `null`.");

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
		 * Use `SceneModule::restartScene()
		 * to restart a `NerdScene<SceneGraphicsT>` while it runs!""");
		 */

	}
	// endregion

	// region `NerdScene<SceneGraphicsT>` operations, declared `private`.
	@SuppressWarnings("unchecked")
	private <SceneGraphicsT extends NerdGenericGraphics> boolean givenSceneRanPreload(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		final NerdScene<SceneGraphicsT> sceneCache = (NerdScene<SceneGraphicsT>) this.SCENE_CACHE
				.get(p_sceneClass).cachedReference;

		// `SonarLint` did this optimization, yay!:
		return sceneCache != null && sceneCache.hasCompletedPreload();
		// return sceneCache == null ? false : sceneCache.hasCompletedPreload();
	}

	@SuppressWarnings("unchecked")
	private <SceneGraphicsT extends NerdGenericGraphics> void loadSceneAssets(
			final NerdScene<SceneGraphicsT> p_scene,
			final boolean p_forcibly) {
		if (p_scene == null)
			return;

		// If forced to, do it:
		if (p_forcibly) {
			p_scene.runPreload();
			return;
		}

		final Class<? extends NerdScene<SceneGraphicsT>> sceneClass = (Class<NerdScene<SceneGraphicsT>>) p_scene
				.getClass();

		// If this scene has never been loaded up before, preload the data!
		if (this.getTimesSceneLoaded(sceneClass) == 0) {
			// p_scene.ASSETS.clear(); // Not needed - this code will never have bugs. Hah!
			p_scene.runPreload();
			this.SCENE_CACHE.get(sceneClass).cachedAssets = p_scene.ASSETS;
			return;
		}

		// region Preloads other than the first one.
		// We're allowed to preload only once?
		// Don't re-load, just use the cache!:
		if (this.scenesModuleSettings.ON_PRELOAD.preloadOnlyOnce) {
			final NerdAssetsModule assets = this.SCENE_CACHE.get(sceneClass).cachedAssets;
			super.getSketchModulesMap().put(NerdAssetsModule.class, assets);
			p_scene.ASSETS = assets;
		} else { // Else, since we're supposed to run `NerdScene<SceneGraphicsT>::preload()` each
					// time, do
					// that!:
			p_scene.ASSETS.clear();
			p_scene.runPreload();
			this.SCENE_CACHE.get(sceneClass).cachedAssets = p_scene.ASSETS;
			// ^^^ `NerdAssetsModule` has literally only ONE field, but alright!
		}
		// endregion

	}

	// region (`private`) Caching operations.
	private <SceneGraphicsT extends NerdGenericGraphics> boolean hasCached(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		// If you haven't been asked to run the scene even once, you didn't cache it!
		// Simply report so:
		// if (!this.SCENE_CACHE.containsKey(p_sceneClass))
		// return false;

		// ...so you ran the scene? Great! ...BUT DO YOU HAVE THE SCENE OBJECT?!
		// return !this.SCENE_CACHE.get(p_sceneClass).isSceneCacheNull();

		// Faster!:
		final NerdScenesModule.NerdScenesModuleSceneCache cachedScene = this.SCENE_CACHE.get(p_sceneClass);
		// return cachedScene == null ? false : !cachedScene.isSceneCacheNull();
		return cachedScene != null && !cachedScene.isSceneCacheNull(); // SonarLint's 'simplification'!
		// (For the JVM, not us 😅)
	}

	private <SceneGraphicsT extends NerdGenericGraphics> void cacheScene(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass /*
																			 * final boolean
																			 * p_isDeletable
																			 */) {
		if (this.SCENE_CACHE.containsKey(p_sceneClass))
			return;

		final Constructor<? extends NerdScene<SceneGraphicsT>> sceneConstructor = this
				.getSceneConstructor(p_sceneClass);
		final NerdScene<SceneGraphicsT> constructedScene = this.constructScene(sceneConstructor);

		if (constructedScene == null)
			throw new IllegalStateException(
					"`NerdScenesModule::constructScene()` returned `null` on an attempt to cache!");

		this.SCENE_CACHE.put(p_sceneClass,
				new NerdScenesModule.NerdScenesModuleSceneCache(sceneConstructor, constructedScene));
	}
	// endregion

	// region `private` construction-and-setup operations!
	private <SceneGraphicsT extends NerdGenericGraphics> Constructor<? extends NerdScene<SceneGraphicsT>> getSceneConstructor(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass) {
		try {
			return p_sceneClass.getConstructor();
		} catch (final NoSuchMethodException e) {
			throw new UnsupportedOperationException("""
					Every subclass of `NerdScene<SceneGraphicsT>` must be declared `public` with a `public`
					"null-constructor" (constructor with no arguments), or should not
					override any constructors at all (to inherit the default one).
					It also must not be an anonymous, or inner class.""");
		}
	}

	private <SceneGraphicsT extends NerdGenericGraphics> NerdScene<SceneGraphicsT> constructScene(
			final Constructor<? extends NerdScene<SceneGraphicsT>> p_sceneConstructor) {
		if (p_sceneConstructor == null) // Won't ever be so, since this class is tightly packed!
			return null; // ...Checking anyway.

		NerdScene<SceneGraphicsT> toRet = null;

		// region Get an instance if possible!
		try {
			toRet = p_sceneConstructor.newInstance();
		} catch (final InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException(
					"No subclass of `NerdScene<SceneGraphicsT>` must be an anonymous or inner class.");
		}
		// endregion

		return toRet;
	}

	@SuppressWarnings("unchecked")
	private <SceneGraphicsT extends NerdGenericGraphics> NerdScene<SceneGraphicsT> constructAndInitScene(
			final Constructor<? extends NerdScene<SceneGraphicsT>> p_sceneConstructor) {
		final NerdScene<SceneGraphicsT> toRet = this.constructScene(p_sceneConstructor);

		if (toRet == null)
			throw new IllegalStateException("`NerdScenesModule::constructScene()` returned `null`!");

		// region Initialize it!
		final Class<? extends NerdScene<SceneGraphicsT>> sceneClass = p_sceneConstructor.getDeclaringClass();
		final NerdScenesModule.NerdScenesModuleSceneCache sceneCache = this.SCENE_CACHE.get(sceneClass);

		// Initialize fields as if this was a part of the construction
		// (done here instead so your subclass doesn't need to write one!):
		toRet.MANAGER = this;
		toRet.SKETCH = toRet.MANAGER.SKETCH;

		// Now, we copy all other objects from `toRet.SKETCH`!:
		// toRet.CAMERA = toRet.GRAPHICS.getCurrentCamera();
		toRet.ASSETS = new NerdAssetsModule(toRet.SKETCH);
		toRet.GRAPHICS = (SceneGraphicsT) toRet.SKETCH.getNerdGenericGraphics();
		toRet.INPUT = toRet.SKETCH.getNerdModule(NerdInputModule.class);
		toRet.WINDOW = toRet.SKETCH.getNerdModule(NerdWindowModule.class);
		toRet.DISPLAY = toRet.SKETCH.getNerdModule(NerdDisplayModule.class);

		// If this is the first time we're constructing this scene,
		// ensure it has a cache and a saved state!
		if (sceneCache == null) {
			toRet.STATE = new NerdSceneState();
			this.cacheScene(sceneClass);
		} else
			toRet.STATE = sceneCache.STATE;
		// endregion

		return toRet;
	}

	// Yes, this checks for errors.
	private <SceneGraphicsT extends NerdGenericGraphics> void startSceneImpl(
			final Class<? extends NerdScene<SceneGraphicsT>> p_sceneClass,
			final NerdSceneState p_state) {
		final NerdScene<SceneGraphicsT> toStart = this.constructAndInitScene(this.getSceneConstructor(p_sceneClass));
		this.setScene(toStart, p_state);
	}

	// The scene-deleter! Also receives EVERY request to start a scene, by the way.
	@SuppressWarnings("unchecked")
	private <SceneGraphicsT extends NerdGenericGraphics> void setScene(final NerdScene<SceneGraphicsT> p_currentScene,
			final NerdSceneState p_state) {
		final NerdWindowModule window = super.SKETCH.getNerdModule(NerdWindowModule.class);
		window.cursorConfined = false;
		window.cursorVisible = true;

		// region `this.SETTINGS.ON_SWITCH` tasks.
		if (this.scenesModuleSettings.ON_SWITCH.doClear) {
			if (this.scenesModuleSettings.ON_SWITCH.clearColor == -1)
				super.SKETCH.clear();
			else
				super.SKETCH.background(this.scenesModuleSettings.ON_SWITCH.clearColor);
		}

		if (this.scenesModuleSettings.ON_SWITCH.resetSceneLayerCallbackOrder) {
			this.scenesModuleSettings.preFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.SCENE;
			this.scenesModuleSettings.drawFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;
			this.scenesModuleSettings.postFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;
		}
		// endregion

		this.previousSceneClass = this.currentSceneClass;
		this.SCENE_CHANGED_LISTENERS.removeAll(this.SCENE_CHANGED_LISTENERS_TO_REMOVE);
		this.SCENE_CHANGED_LISTENERS.forEach(l -> l
				.sceneChanged(this, this.previousSceneClass, this.currentSceneClass));

		if (this.previousSceneClass != null) {
			// Exit the scene, and nullify the cache.
			this.currentScene.runSceneChanged();

			final NerdScenesModule.NerdScenesModuleSceneCache sceneCache = this.SCENE_CACHE.get(this.currentSceneClass);
			if (sceneCache != null)
				sceneCache.nullifyCache(); // Sets the `NerdScene<SceneGraphicsT>` instance to `null` and calls
											// `System.gc()`!
		}

		this.currentSceneClass = (Class<NerdScene<SceneGraphicsT>>) p_currentScene.getClass();
		this.currentScene = p_currentScene;
		this.setupCurrentScene(p_state);
	}

	// Set the time, *then* call `NerdScenesModule::runSetup()`.
	// Called only by `NerdScenesModule::setScene()`:
	private void setupCurrentScene(final NerdSceneState p_state) {
		this.sceneSwitchOccurred = true;
		this.loadSceneAssets(this.currentScene, false);
		this.SCENE_CACHE.get(this.currentSceneClass).timesLoaded++;

		// Helps in resetting style and transformation info across scenes! YAY!:
		if (this.previousSceneClass != null)
			super.SKETCH.pop();

		// We push and pop the style and transforms to auto-reset. Hah!:
		super.SKETCH.push();
		super.SKETCH.textFont(super.SKETCH.getDefaultFont()); // ...Also reset the font. Thanks!

		this.SCENE_CHANGED_LISTENERS.removeAll(this.SCENE_CHANGED_LISTENERS_TO_REMOVE);

		this.currentScene.GRAPHICS.setCurrentCameraToDefault();
		this.currentScene.runSceneInit();

		// This is `null` in SO MANY PLACES!:
		if (p_state == null)
			this.currentScene.runSetup(new NerdSceneState());
		else
			this.currentScene.runSetup(p_state);
	}
	// endregion
	// endregion
	// endregion

}
