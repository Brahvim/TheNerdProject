package com.brahvim.nerd_demos.effect_layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

public class CircleTransitionLayer extends NerdLayer {

	@Override
	protected void draw() {
		SKETCH.begin2d();
		SKETCH.fill(0);

		SKETCH.end2d();
	}

}
