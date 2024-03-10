package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdSpotLight extends NerdAbstractLight {

	// region Fields.
	public PVector dir;
	public float angle, conc = 4000;

	private final PVector PDIR = new PVector();
	// endregion

	// region Constructors.
	public NerdSpotLight(final PGraphics3D p_buffer) {
		super(p_buffer);
		this.dir = new PVector(0, 1, 0);
	}

	public NerdSpotLight(final PGraphics3D p_buffer, final PVector p_pos) {
		super(p_buffer, p_pos);
		this.dir = new PVector(0, 1, 0);
	}

	public NerdSpotLight(final NerdP3dGraphics p_buffer, final PVector p_pos) {
		super(p_buffer, p_pos);
	}

	public NerdSpotLight(final PGraphics3D p_buffer, final PVector p_pos, final PVector p_color) {
		super(p_buffer, p_pos, p_color);
		this.dir = new PVector(0, 1, 0);
	}

	public NerdSpotLight(final NerdP3dGraphics p_buffer, final PVector p_pos, final PVector p_color) {
		super(p_buffer, p_pos, p_color);
	}

	public NerdSpotLight(final PGraphics3D p_buffer, final PVector p_pos, final PVector p_color, final PVector p_dir) {
		super(p_buffer, p_pos, p_color);
		this.dir = p_dir;
	}

	public NerdSpotLight(final NerdP3dGraphics p_buffer, final PVector p_pos, final PVector p_color,
			final PVector p_dir) {
		super(p_buffer, p_pos, p_color);
		this.dir = p_dir;
	}
	// endregion

	public PVector getDirectionInPreviousFrame() {
		return this.PDIR;
	}

	@Override
	protected void applyImpl() {
		final PVector toUse = this.dir == null ? this.PDIR : this.dir;

		super.GRAPHICS.spotLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z,
				toUse.x, toUse.y, toUse.z,
				this.angle, this.conc);

		if (this.dir != null)
			this.PDIR.set(this.dir);
	}

}
