package com.brahvim.nerd.framework.lights;

import java.util.Objects;

import processing.core.PVector;

public class NerdAmbientLight implements NerdLightSlotEntry {

	private PVector
	/*   */ position = new PVector(),
			color = new PVector();

	public NerdAmbientLight() {
	}

	public NerdAmbientLight(final PVector p_color, final PVector p_position) {
		this.setColor(p_color);
		this.setPosition(p_position);
	}

	public NerdAmbientLight(
			final float p_colorX, final float p_colorY, final float p_colorZ,
			final float p_posX, final float p_posY, final float p_posZ) {
		this.color.set(p_colorX, p_colorY, p_colorZ);
		this.position.set(p_posX, p_posY, p_posZ);
	}

	// region Color and position getters and setters.
	public PVector getColor() {
		return this.color;
	}

	public PVector getPosition() {
		return this.position;
	}

	public PVector setColor(final PVector p_color) {
		final PVector toRet = this.color;
		this.color = Objects.requireNonNull(p_color);
		return toRet;
	}

	public PVector setPosition(final PVector p_position) {
		final PVector toRet = this.position;
		this.position = Objects.requireNonNull(p_position);
		return toRet;
	}
	// endregion

}
