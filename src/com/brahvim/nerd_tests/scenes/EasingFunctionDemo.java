package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.math.SineFunction;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class EasingFunctionDemo extends NerdScene {
	private SineFunction sineEase;

	@Override
	protected void setup(SceneState p_state) {
		SKETCH.getCurrentCamera().pos.z = 350;
		this.sineEase = new SineFunction(SKETCH);
		SKETCH.setSize(SKETCH.INIT_WIDTH, SKETCH.INIT_HEIGHT);
	}

	@Override
	protected void draw() {
		SKETCH.background(0x006699);
		SKETCH.circle(this.sineEase.get() * SKETCH.INIT_WIDTH_HALF, 0, 50);
	}

	@Override
	public void mouseClicked() {
		System.out.println("Alright! The easing algorithm stops here...");
		this.sineEase.completeReset();
		this.sineEase.endWhenAngleIncrementsBy(90);
		this.sineEase.start();
	}

}
