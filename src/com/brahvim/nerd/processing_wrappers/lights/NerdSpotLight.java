package com.brahvim.nerd.processing_wrappers.lights;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

public class NerdSpotLight extends NerdLight {

	public float angle, conc = 4000;
	public PVector dir;

	// region Constructors.
	public NerdSpotLight(Sketch p_sketch) {
		super(p_sketch);
		this.dir = new PVector();
	}

	public NerdSpotLight(Sketch p_sketch, PVector p_pos) {
		super(p_sketch, p_pos);
		this.dir = new PVector();
	}

	public NerdSpotLight(Sketch p_sketch, PVector p_pos, PVector p_color) {
		super(p_sketch, p_pos, p_color);
		this.dir = new PVector();
	}

	public NerdSpotLight(Sketch p_sketch, PVector p_pos, PVector p_color, PVector p_dir) {
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