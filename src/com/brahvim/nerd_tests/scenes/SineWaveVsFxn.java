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

		SKETCH.getCurrentCamera().pos.z = 350;
		SKETCH.setSize(SKETCH.INIT_WIDTH, SKETCH.INIT_HEIGHT);
	}

	@Override
	protected void draw() {
		SKETCH.background(0, 125, 0);

		if (SKETCH.mousePressed)
			SKETCH.circle(SKETCH.mouse.x, SKETCH.mouse.y, 50);

		SKETCH.translate(0, -SKETCH.INIT_WIDTH_HALF);
		SKETCH.translate(0, 50);

		SKETCH.push();
		SKETCH.translate(this.fxn.get() * 50, 0);
		SKETCH.fill(255, 0, 0);
		SKETCH.circle(0, 0, 50);
		SKETCH.pop();

		SKETCH.translate(0, 250);

		SKETCH.push();
		SKETCH.translate(this.wave.get() * 50, 0);
		SKETCH.fill(0, 0, 255);
		SKETCH.circle(0, 0, 50);
		SKETCH.pop();
	}

	@Override
	public void mouseClicked() {
		System.out.println("SineWaveVsFxn.mouseClicked()");

		this.fxn.endWhenAngleIs(90);
		this.wave.endWhenAngleIncrementsBy(90);
	}

}
