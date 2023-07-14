package com.brahvim.nerd_demos;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModuleSettings;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdOpenAlModule;
import com.brahvim.nerd.openal.NerdOpenAlModule.NerdOpenAlModuleSettings;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd_demos.scenes.scene3.DemoScene3;

public class App {

	// Big TODOs!:
	/*
	 * - OpenAL *enumerated* wrapper!
	 * - The `NerdEasingFunction` rewrite.
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
	// LoadedSceneClass.DEMO_SCENE_5.getSceneClassLoader();

	public static void main(final String[] p_args) {
		final DemoSketchBuilder builder = new DemoSketchBuilder();

		builder
				.canResize()
				.setAntiAliasing(4)
				.setIconPath("data/sunglass_nerd.png")
				.setNerdModuleSettings(NerdScenesModule.class, () -> {
					final var toRet = new NerdScenesModuleSettings();
					toRet.firstSceneClass = App.FIRST_SCENE_CLASS;
					return toRet;
				})
				.setNerdModuleSettings(NerdOpenAlModule.class, () -> {
					final var toRet = new NerdOpenAlModuleSettings();
					// ...for `DemoScene3`!!!:
					toRet.monoSources = Integer.MAX_VALUE;
					toRet.stereoSources = Integer.MAX_VALUE;
					return toRet;
				})

		/* ...the semi-colon is separate: */ ;

		// Build the `NerdSketch`!:
		final NerdSketch sketch = builder.build(p_args);

		// Get our `NerdAl` instance:
		App.openAl = sketch.getNerdModule(NerdOpenAlModule.class).getOpenAlManager();
	}

}
