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

import processing.core.PConstants;

public class NerdSketchBuilderSettings {

	public Class<? extends NerdScene> firstSceneClass;
	public int width = 400, height = 400, antiAliasing;
	public HashMap<String, NerdExtension> nerdExtensions = new HashMap<>(0);
	public NerdScenesModule.NerdScenesModuleSettings scenesModuleSettings;
	public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
	public Function<NerdSketch, LinkedHashSet<NerdModule>> nerdModulesSupplier;
	public HashSet<Class<? extends NerdScene>> scenesToPreload = new HashSet<>(0);
	public Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] ecsSystemOrder = //
			NerdEcsModule.getEcsSystemsDefaultOrder();
	public NerdScenesModule.NerdScenesModuleSettings.NerdSketchCallbackOrder //
	preCallOrder, drawCallOrder, postCallOrder;
	public boolean preventCloseOnEscape, startedFullscreen, canResize,
			cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;

	// User-defined callback listeners!!!:
	public LinkedHashSet<Consumer<NerdSketch>> sketchConstructedListeners,
			settingsListeners, setupListeners, exitListeners, disposalListeners;

	public LinkedHashSet<Consumer<NerdSketch>> preListeners, postListeners,
			drawListeners, preDrawListeners, postDrawListeners;

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
		this.sketchConstructedListeners = new LinkedHashSet<>();
	}

}
