package com.brahvim.nerd_demos.scenes.scene1;

import com.brahvim.nerd.framework.scene_api.NerdLayer;
import com.brahvim.nerd.math.easings.built_in_easings.NerdSineEase;

public class BackgroundLayer extends NerdLayer {

	private NerdSineEase fadeWave;

	@Override
	protected void setup() {
		this.fadeWave = new NerdSineEase(SKETCH, 0.001f);
		this.fadeWave.absoluteValue = true;
		this.fadeWave.start();
	}

	@Override
	protected void draw() {
		SKETCH.background(0x006699);
		CAMERA.setClearColor(0x006699);

		// CAN USE `this` with lambdas!
		// ...kinda' like `with()` from Python mode in Processing?:
		// SKETCH.in2d(() -> {
		// SKETCH.rectMode(PConstants.CORNER);
		// SKETCH.rect(0, 0, SKETCH.width, SKETCH.height);
		// });
	}

}
