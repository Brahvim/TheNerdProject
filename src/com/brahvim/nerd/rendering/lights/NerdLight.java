package com.brahvim.nerd.rendering.lights;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

public abstract class NerdLight {

	public boolean active = true;
	public PVector pos, color;

	protected final Sketch SKETCH;
	protected boolean pactive;

	// region Constructors.
	public NerdLight(final Sketch p_sketch) {
		this.SKETCH = p_sketch;
		this.pos = new PVector();
		this.color = new PVector();
	}

	public NerdLight(final Sketch p_sketch, final PVector p_pos) {
		this.SKETCH = p_sketch;
		this.pos = p_pos.copy();
		this.color = new PVector();
	}

	public NerdLight(final Sketch p_sketch, final PVector p_pos, final PVector p_color) {
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
