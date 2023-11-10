package com.brahvim.nerd.framework.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.necessary_modules.NerdDisplayModule;
import com.brahvim.nerd.necessary_modules.NerdInputModule;
import com.brahvim.nerd.necessary_modules.NerdWindowModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

/**
 * <h2>Do not use as an anonymous class!</h2>
 * <i>Always extend!</i>
 */
public abstract class NerdScene {

	public final NerdScene SCENE = this;

	// region `protected` fields.
	// Forgive me for breaking naming conventions here.
	// Forgive me. Please!
	protected NerdSketch SKETCH;
	protected NerdSceneState STATE;
	protected NerdInputModule INPUT;
	protected NerdWindowModule WINDOW;
	protected NerdAssetsModule ASSETS;
	protected NerdScenesModule MANAGER;
	protected NerdDisplayModule DISPLAY;
	protected NerdGenericGraphics GRAPHICS;
	// endregion

	// region `private` fields.
	private int startMillis;
	private boolean donePreloading;

	// Start at `0`. "Who needs layers anyway?"
	private final List<NerdLayer> LAYERS = new ArrayList<>(0);
	// Worth remembering: `LinkedHashSet`s allow duplicate objects, store everything
	// in *insertion order*, but have no `indexOf()` method!

	private final Map<Class<? extends NerdLayer>, Constructor<? extends NerdLayer>>
	// ////////////////////////////////////////////////////////////////////////////////
	LAYER_CONSTRUCTORS = new HashMap<>(0);
	// endregion

	protected NerdScene() {
	}

	// region Queries.
	public int getTimesLoaded() {
		return this.MANAGER.getTimesSceneLoaded(this.getClass());
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	public NerdGenericGraphics getGraphics() {
		return this.GRAPHICS;
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
		final T instance = this.getFirstLayerOfClass(p_layerClass);

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
		if (p_layerClass == null) {
			p_notFoundTask.run();
			return;
		}

		int i = 0;
		final int LAYERS_SIZE = this.LAYERS.size();

		// For every `NerdLayer`,
		for (; i != LAYERS_SIZE; i++) {
			final NerdLayer l = this.LAYERS.get(i);

			if (l != null) // If it ain't `null`,
				if (l.getClass().equals(p_layerClass)) // And it be from ma' class,
					if (!l.isActive()) // ...if it ain't active,
						p_task.accept((T) l); // ...perform the given task, brah!
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
			final Class<T> p_layerClass, final Consumer<T> p_task, final Runnable p_noLayersFoundTask) {
		if (p_layerClass == null) {
			p_noLayersFoundTask.run();
			return;
		}

		int i = 0;
		final int LAYERS_SIZE = this.LAYERS.size();

		// For every `NerdLayer`,
		for (; i != LAYERS_SIZE; i++) {
			final NerdLayer l = this.LAYERS.get(i);

			if (l != null) // If it ain't `null`,
				if (l.getClass().equals(p_layerClass)) // And it be from ma' class,
					if (l.isActive()) // ...if it is active,
						p_task.accept((T) l); // ...perform the given task, brah!
		}

		// If no `NerdLayer`s were found, perform the other task!:
		if (i == 0 && p_noLayersFoundTask != null)
			p_noLayersFoundTask.run();
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
			final Class<T> p_layerClass, final Consumer<T> p_task, final Runnable p_noLayersFoundTask) {
		if (p_layerClass == null) {
			p_noLayersFoundTask.run();
			return;
		}

		int i = 0;
		final int LAYERS_SIZE = this.LAYERS.size();

		// For every `NerdLayer`,
		for (; i != LAYERS_SIZE; i++) {
			final NerdLayer l = this.LAYERS.get(i);
			if (l != null) // If it ain't `null`,
				if (l.getClass().equals(p_layerClass)) // And it be from ma' class,
					p_task.accept((T) l); // ...perform the given task, brah!
		}

		// If no `NerdLayer`s were found, perform the other task!:
		if (i == 0 && p_noLayersFoundTask != null)
			p_noLayersFoundTask.run();
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
	 * Gives an {@link List} of {@link NerdLayer} instances of the given
	 * subclass this {@link NerdScene} contains, which are <b>not</b> active.
	 */
	public List<NerdLayer> getInactiveLayers(final Class<? extends NerdLayer> p_layerClass) {
		if (p_layerClass == null)
			return new ArrayList<>();

		final List<NerdLayer> toRet = new ArrayList<>();

		for (final NerdLayer l : this.LAYERS)
			if (l != null && l.getClass().equals(p_layerClass) && !l.isActive())
				toRet.add(l);

		return toRet;
	}

	/**
	 * Gives an {@link List} of {@link NerdLayer} instances of the given
	 * subclass this {@link NerdScene} contains, which are also active.
	 */
	public List<NerdLayer> getActiveLayers(final Class<? extends NerdLayer> p_layerClass) {
		if (p_layerClass == null)
			return new ArrayList<>();

		final List<NerdLayer> toRet = new ArrayList<>();

		for (final NerdLayer l : this.LAYERS)
			if (l != null && l.getClass().equals(p_layerClass) && l.isActive())
				toRet.add(l);

		return toRet;
	}

	/**
	 * Gives an {@link List} of {@link NerdLayer} instances of the given
	 * subclass this {@link NerdScene} contains.
	 */
	public List<NerdLayer> getLayers(final Class<? extends NerdLayer> p_layerClass) {
		final List<NerdLayer> toRet = new ArrayList<>();

		for (final NerdLayer l : this.LAYERS)
			if (l != null && l.getClass().equals(p_layerClass))
				toRet.add(l);

		return toRet;
	}

	/**
	 * This method gives the user access to all {@link NerdLayer} instances being
	 * used by this {@link NerdScene} along with rights such as changing layer
	 * rendering order.
	 */
	public List<NerdLayer> getLayers() {
		return this.LAYERS;
	}
	// endregion

	// region `NerdLayer` state-management.

	/*
	 * @SafeVarargs // I'm not willing to limit your freedom, but this method HAS to
	 * be `final`...
	 * public final NerdLayer[] addLayers(final Class<? extends NerdLayer>...
	 * p_layerClasses) {
	 * final NerdLayer[] toRet = new NerdLayer[p_layerClasses.length];
	 * for (int i = 0; i < p_layerClasses.length; i++) {
	 * final Class<? extends NerdLayer> c = p_layerClasses[i];
	 * toRet[i] = this.addLayers(c);
	 * }
	 * return toRet;
	 * }
	 */

	public <RetT extends NerdLayer> RetT addLayer(final Class<RetT> p_layerClass) {
		if (p_layerClass == null)
			throw new NullPointerException(
					"You weren't supposed to pass `null` into `NerdScene::startLayer()`.");

		// We allow multiple layer instances, by the way.

		final Constructor<? extends NerdLayer> layerConstructor = this.getLayerConstructor(p_layerClass);
		final NerdLayer toRet = this.constructLayer(layerConstructor);

		if (toRet == null)
			throw new NullPointerException("Could not construct `NerdLayer`!");

		toRet.setActive(true);
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

		final Class<? extends NerdLayer> layerClass = p_layer.getClass();

		// If an instance of this layer does not already exist,
		if (!this.LAYERS.contains(p_layer)) {
			System.out.printf("No instance of `%s` exists. Making one...%n", layerClass.getSimpleName());
			this.addLayer(layerClass);
			return;
		}

		final NerdLayer toStart = this.constructLayer(this.getLayerConstructor(layerClass));

		if (toStart == null)
			throw new NullPointerException("You passed a `null` `NerdLayer` into `NerdScene::restartLayer()` :|");

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
					(constructor with no arguments), or no overridden constructors at all.""");
		} catch (final SecurityException e) {
			e.printStackTrace();
		}

		this.LAYER_CONSTRUCTORS.put(p_layerClass, toRet);
		return toRet;
	}

	private NerdLayer constructLayer(final Constructor<? extends NerdLayer> p_layerConstructor) {
		if (p_layerConstructor == null)
			return null;

		NerdLayer toRet = null;

		// region Construct `toRet`.
		try {
			toRet = p_layerConstructor.newInstance();
		} catch (final InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		// endregion

		if (toRet != null) {
			toRet.SCENE = this;
			toRet.STATE = toRet.SCENE.STATE;
			toRet.INPUT = toRet.SCENE.INPUT;
			toRet.SKETCH = toRet.SCENE.SKETCH;
			// toRet.ASSETS = toRet.SCENE.ASSETS;
			toRet.WINDOW = toRet.SCENE.WINDOW;
			// toRet.CAMERA = toRet.SCENE.CAMERA;

			toRet.MANAGER = toRet.SCENE.MANAGER;
			toRet.DISPLAY = toRet.SCENE.DISPLAY;
			toRet.GRAPHICS = toRet.SCENE.GRAPHICS;
		}

		return toRet;
	}
	// endregion
	// endregion

	// region Anything callback-related, LOL.
	/* `package` */ void runSetup(final NerdSceneState p_state) {
		this.startMillis = this.SKETCH.millis();
		this.setup(p_state);

		// TODO: `NerdLayer`s should get to respond to this `setup()`.
	}

	/* `package` */ synchronized void runPreload() {
		this.preload();
		this.ASSETS.forceLoading();

		if (this.MANAGER.scenesModuleSettings.ON_PRELOAD.useExecutors) {
			final ThreadPoolExecutor executor = new ThreadPoolExecutor(
					0, this.MANAGER.scenesModuleSettings.ON_PRELOAD.maxExecutorThreads,
					10L, TimeUnit.SECONDS, new SynchronousQueue<>(),
					new ThreadFactory() {
						private static int threadCount = 1;

						@Override
						public Thread newThread(final Runnable p_threadTask) {
							// Using `NerdScene.this` to get the name of exact class the API user wrote:
							return new Thread(p_threadTask,
									NerdScene.this.getClass().getSimpleName()
											+ "ParallelAssetLoaderThread"
											+ threadCount++);
						}
					});

			final Set<Future<?>> futures = new HashSet<>(this.ASSETS.size());
			this.ASSETS.forEach(a -> futures.add(executor.submit(a::startLoading)));
			executor.shutdown(); // This tells the executor to stop accepting new tasks.

			// If you must complete within this function, do that:
			if (this.MANAGER.scenesModuleSettings.ON_PRELOAD.completeAssetLoadingWithinPreload)
				try {
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // Keep going, keep going...
					// Can't simply cheat the implementation to make it wait forever!
				} catch (final InterruptedException e) {
					// e.printStackTrace();
					Thread.currentThread().interrupt();
				}
		} else
			this.ASSETS.forEach(NerdAsset::startLoading);

		this.donePreloading = true;

	}

	/* `package` */ void runSceneChanged() {
		this.sceneChanged();
	}

	/* `package` */ void runSceneInit() {
		this.sceneInit();
	}

	/* `package` */ void runDispose() {
		this.dispose();
	}

	/* `package` */ void runDraw() {
		if (this.MANAGER.scenesModuleSettings.drawFirstCaller == null)
			this.MANAGER.scenesModuleSettings.drawFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;

		// To avoid asynchronous changes from causing repetition, we put both parts in
		// `if` and `else` block.

		switch (this.MANAGER.scenesModuleSettings.drawFirstCaller) {
			case SCENE -> {
				this.GRAPHICS.push();
				this.draw();
				this.GRAPHICS.pop();

				for (final NerdLayer l : this.LAYERS)
					if (l != null)
						if (l.isActive()) {
							this.GRAPHICS.push();
							l.draw();
							this.GRAPHICS.pop();
						}
			}

			case LAYER -> {
				for (final NerdLayer l : this.LAYERS)
					if (l != null)
						if (l.isActive()) {
							this.GRAPHICS.push();
							l.draw();
							this.GRAPHICS.pop();
						}

				this.GRAPHICS.push();
				this.draw();
				this.GRAPHICS.pop();
			}
		}

	}

	/* `package` */ void runPost() {
		if (this.MANAGER.scenesModuleSettings.postFirstCaller == null)
			this.MANAGER.scenesModuleSettings.postFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;

		// To avoid asynchronous changes from causing repetition, we put both parts in
		// `if` and `else` block.

		switch (this.MANAGER.scenesModuleSettings.preFirstCaller) {
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

		this.exit();
	}

	/* `package` */ void runPre() {
		if (this.MANAGER.scenesModuleSettings.preFirstCaller == null)
			this.MANAGER.scenesModuleSettings.preFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.SCENE;

		// To avoid asynchronous changes from causing repetition, we put both parts in
		// `if` and `else` block.

		switch (this.MANAGER.scenesModuleSettings.preFirstCaller) {
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
	 * Used by a {@link NerdScene} to load {@link NerdAsset}s
	 * into their, or their {@link NerdScenesModule}'s {@link NerdAssetsModule}.
	 *
	 * <p>
	 * Use this method for all asset-loading purposes that you would like to do in
	 * the background. If {@link NerdScenesModule#loadSceneAssets()} or
	 * {@link NerdScenesModule#loadSceneAssetsAsync} is called, this method is run
	 * async, loading-in all {@link NerdAsset}s!
	 * <p>
	 * Since {@link NerdScene}s could be a part of the same {@link NerdSketch}, it
	 * is important to ensure that this method is {@code synchronized}.
	 */
	protected synchronized void preload() {
	}

	/**
	 * Callback for when the scene changes. Calling certain methods from
	 * {@link NerdScene#MANAGER} <i><b>will</b></i> cause crashes here!
	 */
	protected void sceneChanged() {
	}

	protected void sceneInit() {
	}

	/**
	 * {@link NerdScene#setup()} is called when one of
	 * {@link NerdScenesModule#startScene(Class)},
	 * {@link NerdScenesModule#restartScene(Class)}, or
	 * {@link NerdScenesModule#startPreviousScene(Class)}
	 * is called, after the {@link NerdScene} finishes executing
	 * {@link NerdScene#preload()},
	 * <p>
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
	protected void touchStarted() {
	}

	protected void touchMoved() {
	}

	protected void touchEnded() {
	}
	// endregion

	// region Window focus events.
	protected void resized() {
	}

	protected void focusLost() {
	}

	protected void focusGained() {
	}

	protected void monitorChanged() {
	}

	protected void fullscreenChanged(final boolean p_state) {
	}
	// endregion
	// endregion

}
