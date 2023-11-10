package com.brahvim.nerd.framework.scene_api;

import java.util.HashSet;
import java.util.Set;

import com.brahvim.nerd.processing_wrapper.NerdModuleSettings;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdScenesModuleSettings extends NerdModuleSettings<NerdScenesModule> {

	public final Class<? extends NerdScene<?>> FIRST_SCENE_CLASS;

	/**
	 * Dictates to every {@link NerdScenesModule} instance, the order in which a
	 * {@link NerdScene} or {@link NerdLayer} is allowed to call certain "workflow
	 * events" ({@link NerdScene#pre()}, {@link NerdScene#draw()} and
	 * {@link NerdScene#post()}) from Processing.
	 *
	 * @see {@link NerdScenesModuleSettings#preFirstCaller} -
	 *      {@link NerdSceneLayerCallbackOrder#SCENE} by
	 *      default.
	 * @see {@link NerdScenesModuleSettings#drawFirstCaller} -
	 *      {@link NerdSceneLayerCallbackOrder#LAYER} by
	 *      default.
	 * @see {@link NerdScenesModuleSettings#postFirstCaller} -
	 *      {@link NerdSceneLayerCallbackOrder#LAYER} by
	 *      default.
	 */
	public /* `static` */ enum NerdSceneLayerCallbackOrder {
		SCENE(), LAYER();
	}

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
		 * {@link NerdScenesModuleSettings.OnScenePreload#completeAssetLoadingWithinPreload}
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
		 * {@link NerdScenesModuleSettings.OnScenePreload#useExecutors}
		 * to {@code true} or {@code false}!
		 *
		 * @apiNote We use <b>{@code 6}</b> threads by default! Enough? Happy?
		 *          I hope you are ":D!~
		 */
		public int maxExecutorThreads = 6;

		/**
		 * If
		 * {@link NerdScenesModuleSettings.OnScenePreload#useExecutors}
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
		public int clearColor = -1;

		/**
		 * Clears the screen according to
		 * {@link NerdScenesModuleSettings.OnSceneSwitch#clearColor}.
		 *
		 * @apiNote {@code false} by default.
		 */
		public boolean doClear = false;

		/**
		 * Resets {@link NerdScenesModuleSettings#preFirstCaller},
		 * {@link NerdScenesModuleSettings#drawFirstCaller}, and
		 * {@link NerdScenesModuleSettings#postFirstCaller} to their
		 * default values!
		 */
		public boolean resetSceneLayerCallbackOrder = true;

	}

	public final NerdScenesModuleSettings.OnSceneSwitch ON_SWITCH = new OnSceneSwitch();

	public final NerdScenesModuleSettings.OnScenePreload ON_PRELOAD = new OnScenePreload();

	// region Callback order specifiers.
	/**
	 * Controls whether {@link NerdScene#pre()} or {@link NerdLayer#pre()} is
	 * called first by the {@link NerdScenesModule}. If the value of this field is
	 * ever {@code null}, it is set to its default,
	 * {@link NerdSceneLayerCallbackOrder#SCENE}.
	 */
	public NerdScenesModuleSettings.NerdSceneLayerCallbackOrder preFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.SCENE;

	/**
	 * Controls whether {@link NerdScene#draw()} or {@link NerdLayer#draw()} is
	 * called first by the {@link NerdScenesModule}. If the value of this field is
	 * ever{@code null}, it is set to its default,
	 * {@link NerdSceneLayerCallbackOrder#LAYER}.
	 */
	public NerdScenesModuleSettings.NerdSceneLayerCallbackOrder drawFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;

	/**
	 * Controls whether {@link NerdScene#post()} or {@link NerdLayer#post()} is
	 * called first by the {@link NerdScenesModule}. If the value of this field is
	 * ever {@code null}, it is set to its default,
	 * {@link NerdSceneLayerCallbackOrder#LAYER}.
	 */
	public NerdScenesModuleSettings.NerdSceneLayerCallbackOrder postFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.LAYER;
	// endregion

	public Set<Class<? extends NerdScene<?>>> classesOfScenesToPreload = new HashSet<>(0);

	public NerdScenesModuleSettings(final Class<? extends NerdScene<?>> p_firstSceneClass) {
		this.FIRST_SCENE_CLASS = p_firstSceneClass;
	}

}
