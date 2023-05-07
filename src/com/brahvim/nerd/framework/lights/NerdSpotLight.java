package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PVector;

public class NerdSpotLight extends NerdLight {

	public float angle, conc = 4000;
	public PVector dir;

	// region Constructors.
	public NerdSpotLight(final NerdSketch p_sketch) {
		super(p_sketch);
		this.dir = new PVector();
	}

	public NerdSpotLight(final NerdSketch p_sketch, final PVector p_pos) {
		super(p_sketch, p_pos);
		this.dir = new PVector();
	}

	public NerdSpotLight(final NerdSketch p_sketch, final PVector p_pos, final PVector p_color) {
		super(p_sketch, p_pos, p_color);
		this.dir = new PVector();
	}

	public NerdSpotLight(final NerdSketch p_sketch, final PVector p_pos, final PVector p_color, final PVector p_dir) {
		super(p_sketch, p_pos, p_color);
		this.dir = p_dir.copy();
	}
	// endregion

	@Override
	protected void applyImpl() {
		super.SKETCH.spotLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z,
				this.dir.x, this.dir.y, this.dir.z,
				this.angle, this.conc);
	}

}
