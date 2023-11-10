package com.brahvim.nerd.framework.lights;

import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdPointLight extends NerdLight {

	// region Constructors.
	public NerdPointLight(final PGraphics3D p_buffer) {
		super(p_buffer);
	}

	public NerdPointLight(final NerdGenericGraphics p_buffer) {
		super(p_buffer);
	}

	public NerdPointLight(final PGraphics3D p_buffer, final PVector p_pos) {
		super(p_buffer, p_pos);
	}

	public NerdPointLight(final NerdGenericGraphics p_buffer, final PVector p_pos) {
		super(p_buffer, p_pos);
	}

	public NerdPointLight(final PGraphics3D p_buffer, final PVector p_pos, final PVector p_color) {
		super(p_buffer, p_pos, p_color);
	}

	public NerdPointLight(final NerdGenericGraphics p_buffer, final PVector p_pos, final PVector p_color) {
		super(p_buffer, p_pos, p_color);
	}
	// endregion

	@Override
	protected void applyImpl() {
		super.GRAPHICS.pointLight(
				super.color.x, super.color.y, super.color.z,
				super.pos.x, super.pos.y, super.pos.z);
	}

}
