package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.processing_wrapper.NerdGraphics;

import processing.core.PVector;
import processing.opengl.PGraphics3D;

public abstract class NerdLight {

	public PVector pos, color;
	public boolean active = true;

	protected final PGraphics3D GRAPHICS;

	protected boolean pactive;

	// region Constructors.
	public NerdLight(final PGraphics3D p_buffer) {
		this.GRAPHICS = p_buffer;
		this.pos = new PVector();
		this.color = new PVector();
	}

	public NerdLight(final NerdGraphics p_buffer) {
		PGraphics3D buffer = null;

		try {
			buffer = (PGraphics3D) p_buffer.getUnderlyingBuffer();
		} catch (final ClassCastException e) {
			throw new RuntimeException("`NerdLight`s cannot be used without OpenGL.");
		}

		this.GRAPHICS = buffer;
		this.pos = new PVector();
		this.color = new PVector(255, 255, 255);
	}

	public NerdLight(final PGraphics3D p_buffer, final PVector p_pos) {
		this.pos = p_pos;
		this.GRAPHICS = p_buffer;
		this.color = new PVector(255, 255, 255);
	}

	public NerdLight(final NerdGraphics p_buffer, final PVector p_pos) {
		this(p_buffer);
		this.pos = p_pos;
	}

	public NerdLight(final PGraphics3D p_buffer, final PVector p_pos, final PVector p_color) {
		this.pos = p_pos;
		this.color = p_color;
		this.GRAPHICS = p_buffer;
	}

	public NerdLight(final NerdGraphics p_buffer, final PVector p_pos, final PVector p_color) {
		this(p_buffer);
		this.pos = p_pos;
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
