package com.brahvim.nerd_demos;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModuleSettings;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdOpenAlModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd_demos.scenes.scene3.DemoScene3;

public class App {

	// Big TODOs!:
	/*
	 * - OpenAL *enumerated* wrapper!
	 * - The `NerdEasingFunction` rewrite.
	 * - Eliminate `NerdScene::draw()`. ECS-only updates.
	 * - Versioned serialization packets containing ECS components.
	 * - Input mappings API like the other, 'real' engines using `Predicate`s.
	 * - Complete the ECS's networking API.
	 * - Stop screwing up with how to use `PGraphics`, cameras etc.
	 * - Let JAR assets be in the `data` folder (each JAR carries a folder).
	 *
	 * Longer tasks:
	 * - Android port!
	 * - ECS wrapper for Processing!
	 * - Dyn4j / Javacpp LiquidFun ECS wrapper!
	 */

	public static NerdAl openAl;
	public static final Class<? extends NerdScene> FIRST_SCENE_CLASS = DemoScene3.class;
	// public static final Class<? extends NerdScene> FIRST_SCENE_CLASS =
	// LoadedSceneClass.DEMO_SCENE_5.getSceneClassLoader();

	public static void main(final String[] p_args) {
		final NerdSketchBuilder builder = new NerdSketchBuilder();

		builder
				.canResize()
				.setAntiAliasing(4)
				.setTitle("The Nerd Project")
				.setIconPath("data/sunglass_nerd.png")
				.setNerdModuleSettings(NerdScenesModule.class, () -> {
					final NerdScenesModuleSettings toRet = new NerdScenesModuleSettings();
					toRet.firstSceneClass = App.FIRST_SCENE_CLASS;
					return toRet;
				});

		// Build the `NerdSketch`!:
		final NerdSketch sketch = builder.build(p_args);

		// Get our OpenAL module's instance:
		App.openAl = sketch.getNerdModule(NerdOpenAlModule.class).getOpenAlManager();
	}

}
