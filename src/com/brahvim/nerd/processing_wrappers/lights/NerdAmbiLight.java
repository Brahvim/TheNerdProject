package com.brahvim.nerd.processing_wrappers.lights;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

public class NerdAmbiLight extends NerdLight {

	// region Constructors.
	public NerdAmbiLight(Sketch p_sketch) {
		super(p_sketch);
	}

	public NerdAmbiLight(Sketch p_sketch, PVector p_pos) {
		super(p_sketch, p_pos);
	}

	public NerdAmbiLight(Sketch p_sketch, PVector p_pos, PVector p_color) {
		super(p_sketch, p_pos, p_color);
	}
	// endregion

	@Override
	protected void applyImpl() {
		super.SKETCH.ambientLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z);
	}

}
