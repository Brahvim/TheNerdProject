package com.brahvim.nerd_demos;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlExt;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuildArtifacts;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd_demos.scenes.scene3.DemoScene3;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;

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

	public static final Class<? extends NerdScene> FIRST_SCENE_CLASS =
			// Use directly in `setFirstSceneClass()` below!:
			// LoadedSceneClass.DEMO_SCENE_5.getSceneClassLoader();
			DemoScene3.class;
	// null;

	// region `App`'s *other* fields.
	public static final int BPM = 100,
			BPM_INT = (int) (App.BPM / 60_000.0f);

	public static GL gl;
	public static GLU glu;
	public static PGL pgl;
	public static NerdAl openAl;
	public static GLWindow glWindow;
	public static PGraphicsOpenGL pgGraphicsGl;

	private static volatile int tickCount;
	private static volatile boolean tick;
	// endregion

	public static void main(final String[] p_args) {
		// region Building the `Sketch`!
		final NerdSketchBuilder builder = new NerdSketchBuilder();
		builder.setStringTablePath(NerdSketch.fromDataDir("Nerd_StringTable.json"))
				.canResize()
				// .startFullscreen()
				.setAntiAliasing(4)
				.setTitle("The Nerd Project")
				.setFirstScene(App.FIRST_SCENE_CLASS)
				.setIconPath("data/sunglass_nerd.png")
				.addNerdExt(new NerdAlExt(s -> {
					// ...for `DemoScene3`!!!:
					s.monoSources = Integer.MAX_VALUE;
					s.stereoSources = Integer.MAX_VALUE;
				}))
				.addSketchConstructionListener(
						s -> System.out.println(s.STRINGS.get("Meta.onConstruct")))
				.setSceneManagerSettings(s -> s.ON_PRELOAD.preloadOnlyOnce = false);

		// Build the sketch and collect build artifacts:
		final NerdSketchBuildArtifacts artifacts = builder.build(p_args);
		App.openAl = artifacts.getExtObject("OpenAL");
		// endregion

		App.startTickThread();
	}

	// region Ticking.
	private static void startTickThread() {
		new Thread(() -> {
			try {
				Thread.sleep(App.BPM_INT);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			App.tick = true;
			App.tickCount++;
		}).start();
	}

	public static boolean hasTick() {
		return App.tick;
	}

	public static int getTickCount() {
		return App.tickCount;
	}

	public static void resetTickCount() {
		App.tickCount = 0;
	}
	// endregion

}
