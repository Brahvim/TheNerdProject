package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.ecs.NerdEcsComponent;
import com.brahvim.nerd.framework.ecs.NerdEcsManager;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneManager;
import com.brahvim.nerd.framework.scene_api.NerdSceneManager.NerdSceneManagerSettings;
import com.brahvim.nerd.openal.AlContext;

import processing.core.PConstants;

/* `package` */ class NerdSketchBuilderSettings {

	public Class<? extends NerdScene> firstScene;
	public int width = 400, height = 400, antiAliasing;
	public NerdSceneManagerSettings sceneManagerSettings;
	public AlContext.AlContextSettings alContextSettings;
	public HashMap<String, Object> nerdExtensions = new HashMap<>();
	public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
	public HashSet<Class<? extends NerdScene>> scenesToPreload = new HashSet<>(0);
	public Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] ecsSystemOrder =
			// VSCode, you made the decision to tab this:
			NerdEcsManager.getDefaultEcsSystemsOrder();
	public NerdSceneManager.NerdSceneManagerSettings.NerdSketchCallbackOrder preCallOrder, drawCallOrder, postCallOrder;

	// region Listeners!!!
	public LinkedHashSet<Consumer<NerdSketch>> sketchConstructedListeners,
			settingsListeners, setupListeners, exitListeners, disposalListeners;

	public LinkedHashSet<NerdSceneManager.NerdSceneChangeListener> sceneChangeListeners = new LinkedHashSet<>();

	public LinkedHashSet<Consumer<NerdSketch>> preListeners, postListeners,
			drawListeners, preDrawListeners, postDrawListeners;

	public boolean preventCloseOnEscape, startedFullscreen, canResize,
			cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
	// endregion

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
		this.sceneChangeListeners = new LinkedHashSet<>();
		this.sketchConstructedListeners = new LinkedHashSet<>();

	}

}
