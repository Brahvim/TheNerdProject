package com.brahvim.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.lights.NerdAmbientLight;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_api.NerdScenesModuleSettings;
import com.brahvim.nerd.openal.AlBuffer;
import com.brahvim.nerd.openal.al_asset_loaders.AlOggBufferAsset;
import com.brahvim.nerd_demos.App;
import com.brahvim.nerd_demos.debug_layers.DebugFpsGizmoLayer;
import com.brahvim.nerd_demos.effect_layers.CinematicBarsLayer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class DemoScene3 extends NerdScene {

	// region Fields.
	private PImage bgImage;
	private SmoothCamera CAMERA;
	private CubeManager cubeMan;
	private NerdAmbientLight light;
	// endregion

	@Override
	protected synchronized void preload() {
		for (int i = 1; i != 5; i++)
			ASSETS.addAsset(new AlOggBufferAsset(App.alUpdater, "data/Pops/Pop" + i + ".ogg"));
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		MANAGER.getScenesModuleSettings().drawFirstCaller = NerdScenesModuleSettings.NerdSceneLayerCallbackOrder.SCENE;
		SCENE.addLayer(CinematicBarsLayer.class);
		SCENE.addLayer(DebugFpsGizmoLayer.class);

		CAMERA = new SmoothCamera(GRAPHICS);
		CAMERA.fov = PApplet.radians(75);
		GRAPHICS.setCurrentCamera(this.CAMERA);
		// SKETCH.frameRate(90);

		final AlBuffer<?>[] alBuffers = new AlBuffer<?>[4];
		for (int i = 1; i != 5; i++)
			alBuffers[i - 1] = ASSETS.get("Pop" + i).getData();

		this.cubeMan = new CubeManager(this, alBuffers);
		this.light = new NerdAmbientLight(
				GRAPHICS, new PVector(0, 0, 0),
				// new PVector(255, 255, 0), // Yellow.
				// new PVector(224, 152, 27), // The orange at the top.
				// new PVector(228, 117, 111), // The color in the middle.
				new PVector(232, 81, 194) // The pink at the bottom.
		);

		this.bgImage = this.createBackgroundImage();
		// GRAPHICS.background(this.bgImage);
	}

	@Override
	protected void draw() {
		// GRAPHICS.tint(255, 100);
		GRAPHICS.background(this.bgImage);

		// Faster in `draw()`:
		if (INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
			MANAGER.restartScene();

		GRAPHICS.lights();
		this.light.apply();
		this.cubeMan.draw();

		GRAPHICS.image(SKETCH.getGraphics(), 1000, 1000);
	}

	private PImage createBackgroundImage() {
		final int color1 = SKETCH.color(224, 152, 27),
				color2 = SKETCH.color(232, 81, 194);
		final PImage toRet = SKETCH.createImage(DISPLAY.displayWidth, DISPLAY.displayHeight, PConstants.RGB);

		toRet.loadPixels();

		for (int y = 0; y < toRet.height; y++)
			for (int x = 0; x < toRet.width; x++)
				toRet.pixels[x + y * toRet.width] = SKETCH.lerpColor(
						color1, color2, PApplet.map(y, 0, toRet.height, 0, 1));

		toRet.updatePixels();

		return toRet;
	}

	// region Event callbacks.
	@Override
	public void mouseClicked() {
		switch (INPUT.mouseButton) {
			case PConstants.CENTER -> CAMERA.setRoll(0);
			// case PConstants.RIGHT -> MANAGER.startScene(DemoScene1.class);
			case PConstants.LEFT -> {
				this.cubeMan.emitCubes(1); // this.cubeMan.cubesPerClick);
				// if (this.cubeMan.numCubes() < 2)
				// // this.cubeMan.emitCubes(1);
			}
		}
	}

	@Override
	public void keyPressed() {
		if (INPUT.keyCode == KeyEvent.VK_F) {
			WINDOW.cursorVisible = !WINDOW.cursorVisible;
			CAMERA.holdMouse = !CAMERA.holdMouse;
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		CAMERA.fov -= p_mouseEvent.getCount() * 0.1f;
		CAMERA.fov = PApplet.constrain(CAMERA.fov, 0, 130);
	}

	@Override
	protected void sceneChanged() {
		this.cubeMan.removeAll(); // REALLY helps the GC out!
		System.gc(); // Surprisingly, this is an effective hint to the GC.

		// GRAPHICS.tint(255);
		// GRAPHICS.background(this.bgImage);
	}
	// endregion

}
