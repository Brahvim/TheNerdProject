package com.brahvim.nerd_demos.effect_layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

import processing.core.PConstants;

public class CinematicBarsLayer extends NerdLayer {

	public float rectHeight = 60;

	@Override
	protected void draw() {
		GRAPHICS.fill(0);
		GRAPHICS.begin2d();
		GRAPHICS.rectMode(PConstants.CORNER);
		GRAPHICS.rect(0, 0, WINDOW.width, this.rectHeight);
		GRAPHICS.rect(0, WINDOW.height - this.rectHeight,
				WINDOW.width, WINDOW.height);
		GRAPHICS.end2d();
	}

}
