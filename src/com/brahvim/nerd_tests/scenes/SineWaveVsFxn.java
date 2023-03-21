package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.math.SineFunction;
import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class SineWaveVsFxn extends NerdScene {
	private SineWave wave;
	private SineFunction fxn;

	@Override
	protected void setup(SceneState p_state) {
		this.wave = new SineWave(SKETCH);
		this.fxn = new SineFunction(SKETCH);

		this.fxn.start();
		this.wave.start();

		SKETCH.setSize(400, 400);
	}

	@Override
	protected void draw() {
		SKETCH.background(0, 125, 0);

		// SKETCH.translate(-SKETCH.cx, -SKETCH.cy);

		if (SKETCH.mousePressed)
			SKETCH.circle(SKETCH.mouseX, SKETCH.mouseY, 50);

		SKETCH.translate(0, 50);

		SKETCH.push();
		SKETCH.translate(SKETCH.cx + this.fxn.get() * SKETCH.cx, 0);
		SKETCH.fill(255, 0, 0);
		SKETCH.circle(0, 0, 50);
		SKETCH.pop();

		SKETCH.translate(0, 250);

		SKETCH.push();
		SKETCH.translate(SKETCH.cx + this.wave.get() * SKETCH.cx, 0);
		SKETCH.fill(0, 0, 255);
		SKETCH.circle(0, 0, 50);
		SKETCH.pop();
	}

	@Override
	public void mouseClicked() {
		System.out.println("SineWaveVsFxn.mouseClicked()");

		this.fxn.endWhenAngleIs(90);
		this.wave.endWhenAngleIs(90);
	}

}
