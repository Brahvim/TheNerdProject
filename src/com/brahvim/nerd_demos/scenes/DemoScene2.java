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
		SKETCH.background(0x006699);
		// SKETCH.in2d(() -> SKETCH.alphaBg(0, 102, 153, 0));

		// if (SKETCH.frameCount % 150 == 0)
		// System.out.println(SKETCH.frameRate);

		SKETCH.translate(INPUT.MOUSE_VECTOR);
		SKETCH.translate(WINDOW.cx, WINDOW.cy);
		SKETCH.text("Scene `2`!", 0, 0);

		// region Translation.
		// CAMERA.center.z = this.boxHorizWave.get() * SKETCH.qx -
		// SKETCH.qx / 1.5f;

		// this.camHorizWave.absoluteValue = false;
		// CAMERA.center.y = this.camHorizWave.get() * SKETCH.qy -
		// SKETCH.qy / 2;
		// CAMERA.center.y *= 0.5f;
		// CAMERA.center.y += SKETCH.qy * 0.5f;
		// this.camHorizWave.absoluteValue = true;

		// CAMERA.pos.y = this.boxVertWave.get() * SKETCH.qy;
		// endregion

		// SKETCH.translate(
		// this.boxHorizWave.get() * SKETCH.qx,
		// this.boxVertWave.get() * SKETCH.qy, 0);
		SKETCH.box(45);
	}

	@Override
	public void mouseClicked() {
		MANAGER.startScene(DemoScene1.class);
	}

}
