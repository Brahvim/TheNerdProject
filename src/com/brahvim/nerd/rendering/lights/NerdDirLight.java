package com.brahvim.nerd.rendering.lights;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PVector;

public class NerdDirLight extends NerdLight {

	// region Constructors.
	public NerdDirLight(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	public NerdDirLight(final NerdSketch p_sketch, final PVector p_pos) {
		super(p_sketch, p_pos);
	}

	public NerdDirLight(final NerdSketch p_sketch, final PVector p_pos, final PVector p_color) {
		super(p_sketch, p_pos, p_color);
	}
	// endregion

	@Override
	protected void applyImpl() {
		super.SKETCH.directionalLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z);
	}

}