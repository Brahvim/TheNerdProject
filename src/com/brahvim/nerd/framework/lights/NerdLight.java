package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PVector;

public abstract class NerdLight {

	public boolean active = true;
	public PVector pos, color;

	protected final NerdSketch SKETCH;
	protected boolean pactive;

	// region Constructors.
	public NerdLight(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
		this.pos = new PVector();
		this.color = new PVector();
	}

	public NerdLight(final NerdSketch p_sketch, final PVector p_pos) {
		this.SKETCH = p_sketch;
		this.pos = p_pos.copy();
		this.color = new PVector();
	}

	public NerdLight(final NerdSketch p_sketch, final PVector p_pos, final PVector p_color) {
		this.SKETCH = p_sketch;
		this.pos = p_pos.copy();
		this.color = p_color;
	}
	// endregion

	public void apply() {
		if (this.active)
			this.applyImpl();
		this.pactive = this.active;
	}

	public boolean wasActive() {
		return this.pactive;
	}

	protected abstract void applyImpl();

}
