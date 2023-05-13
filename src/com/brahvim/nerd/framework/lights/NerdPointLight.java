package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PVector;

public class NerdPointLight extends NerdLight {

	// region Constructors.
	public NerdPointLight(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	public NerdPointLight(final NerdSketch p_sketch, final PVector p_pos) {
		super(p_sketch, p_pos);
	}

	public NerdPointLight(final NerdSketch p_sketch, final PVector p_pos, final PVector p_color) {
		super(p_sketch, p_pos, p_color);
	}
	// endregion

	@Override
	protected void applyImpl() {
		super.SKETCH.pointLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z);
	}

}
