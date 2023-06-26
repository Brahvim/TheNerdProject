package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.processing_wrapper.NerdGraphics;

import processing.core.PVector;
import processing.opengl.PGraphics3D;

public abstract class NerdLight {

	// region Fields.
	public boolean active = true;

	protected final PGraphics3D GRAPHICS;

	protected boolean pactive;
	protected PVector pos, color;
	// endregion

	// region Constructors.
	protected NerdLight(final PGraphics3D p_buffer) {
		this.GRAPHICS = p_buffer;
		this.pos = new PVector();
		this.color = new PVector();
	}

	protected NerdLight(final NerdGraphics p_buffer) {
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

	protected NerdLight(final PGraphics3D p_buffer, final PVector p_pos) {
		this.pos = p_pos;
		this.GRAPHICS = p_buffer;
		this.color = new PVector(255, 255, 255);
	}

	protected NerdLight(final NerdGraphics p_buffer, final PVector p_pos) {
		this(p_buffer);
		this.pos = p_pos;
	}

	protected NerdLight(final PGraphics3D p_buffer, final PVector p_pos, final PVector p_color) {
		this.pos = p_pos;
		this.color = p_color;
		this.GRAPHICS = p_buffer;
	}

	protected NerdLight(final NerdGraphics p_buffer, final PVector p_pos, final PVector p_color) {
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

	public PVector getPos() {
		return this.pos;
	}

	public PVector getColor() {
		return this.color;
	}

	public PGraphics3D getRegisteredBuffer() {
		return this.GRAPHICS;
	}

	public PVector setPos(final PVector p_pos) {
		final PVector toRet = this.pos;
		this.pos = p_pos;
		return toRet;
	}

	public PVector setColor(final PVector p_color) {
		final PVector toRet = this.color;
		this.color = p_color;
		return toRet;
	}

	protected abstract void applyImpl();

}
