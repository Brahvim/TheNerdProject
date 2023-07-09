package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.framework.ecs.NerdEcsComponent;
import com.brahvim.nerd.framework.ecs.NerdEcsModule;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule.NerdSceneManagerSettings;
import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.processing_wrapper.window_man_subs.NerdGlWindowModule;

import processing.core.PConstants;

public class NerdSketchBuilderSettings {

	public Class<? extends NerdScene> firstSceneClass;
	public int width = 400, height = 400, antiAliasing;
	public NerdSceneManagerSettings sceneManagerSettings;
	public AlContext.AlContextSettings alContextSettings;
	public HashMap<String, Object> nerdExtensions = new HashMap<>();
	public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
	public HashSet<Class<? extends NerdScene>> scenesToPreload = new HashSet<>(0);
	public Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] ecsSystemOrder =
			// VSCode, you made the decision to tab this all the way down HERE!:
			NerdEcsModule.getDefaultEcsSystemsOrder();
	public NerdScenesModule.NerdSceneManagerSettings.NerdSketchCallbackOrder preCallOrder, drawCallOrder, postCallOrder;

	// region Listeners!!!
	public LinkedHashSet<Consumer<NerdSketch>> sketchConstructedListeners,
			settingsListeners, setupListeners, exitListeners, disposalListeners;

	public LinkedHashSet<NerdScenesModule.NerdSceneChangedListener> sceneChangedListeners = new LinkedHashSet<>();

	public LinkedHashSet<Consumer<NerdSketch>> preListeners, postListeners,
			drawListeners, preDrawListeners, postDrawListeners;

	public boolean preventCloseOnEscape, startedFullscreen, canResize,
			cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
	// endregion

	protected Function<NerdSketch, HashMap<Class<? extends NerdModule>, NerdModule>> nerdModulesInstantiator;

	public NerdSketchBuilderSettings() {
		// Intializing these listeners:
		this.preListeners = new LinkedHashSet<>();
		this.postListeners = new LinkedHashSet<>();
		this.drawListeners = new LinkedHashSet<>();
		this.preDrawListeners = new LinkedHashSet<>();
		this.postDrawListeners = new LinkedHashSet<>();

		// Intializing these listeners as well, haha!:
		this.exitListeners = new LinkedHashSet<>();
		this.setupListeners = new LinkedHashSet<>();
		this.settingsListeners = new LinkedHashSet<>();
		this.disposalListeners = new LinkedHashSet<>();
		this.sceneChangedListeners = new LinkedHashSet<>();
		this.sketchConstructedListeners = new LinkedHashSet<>();

		this.nerdModulesInstantiator = s -> {
			final HashMap<Class<? extends NerdModule>, NerdModule> toRet = new HashMap<>();
			toRet.put(NerdDisplayModule.class, new NerdDisplayModule(s));
			toRet.put(NerdWindowModule.class, s.SKETCH_SETTINGS.USES_OPENGL ? new NerdGlWindowModule(s) : null);
			toRet.put(NerdInputModule.class, new NerdInputModule(s));
			toRet.put(NerdCallbacksModule.class, new NerdCallbacksModule(s, this));
			toRet.put(NerdScenesModule.class, new NerdScenesModule(s, this));
			return toRet;
		};
	}

}
