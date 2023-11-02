package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.math.easings.built_in_easings.NerdSineEase;
import com.brahvim.nerd_demos.scenes.scene1.DemoScene1;

public class DemoScene2 extends NerdScene {

	private NerdSineEase boxHorizWave, boxVertWave;

	@Override
	protected void setup(final NerdSceneState p_state) {
		CAMERA.completeReset();

		this.boxHorizWave = new NerdSineEase(SKETCH, 100 / 60_000.0f);
		this.boxHorizWave.start();

		this.boxVertWave = new NerdSineEase(SKETCH, 200 / 60_000.0f);
		this.boxVertWave.start();
	}

	@Override
	protected void draw() {
		GRAPHICS.background(0x006699);
		// GRAPHICS.in2d(() -> GRAPHICS.alphaBg(0, 102, 153, 0));

		// if (SKETCH.frameCount % 150 == 0)
		// System.out.println(SKETCH.frameRate);

		GRAPHICS.translate(INPUT.MOUSE_VECTOR);
		GRAPHICS.translate(WINDOW.cx, WINDOW.cy);
		GRAPHICS.text("Scene `2`!", 0, 0);

		// region Translation.
		// CAMERA.center.z = this.boxHorizWave.get() * WINDOW.qx -
		// WINDOW.qx / 1.5f;

		// this.camHorizWave.absoluteValue = false;
		// CAMERA.center.y = this.camHorizWave.get() * WINDOW.qy -
		// WINDOW.qy / 2;
		// CAMERA.center.y *= 0.5f;
		// CAMERA.center.y += WINDOW.qy * 0.5f;
		// this.camHorizWave.absoluteValue = true;

		// CAMERA.pos.y = this.boxVertWave.get() * WINDOW.qy;
		// endregion

		// WINDOW.translate(
		// this.boxHorizWave.get() * WINDOW.qx,
		// this.boxVertWave.get() * WINDOW.qy, 0);
		GRAPHICS.box(45);
	}

	@Override
	public void mouseClicked() {
		MANAGER.startScene(DemoScene1.class);
	}

}
