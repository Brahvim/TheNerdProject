package com.brahvim.nerd.framework.lights;

import java.util.Objects;

import processing.core.PVector;

public class NerdDirectionalLight implements NerdLightSlotEntry {

	private PVector
	/*   */ direction = new PVector(),
			color = new PVector();

	// region Color and position getters and setters.
	public PVector getColor() {
		return this.color;
	}

	public PVector getDirection() {
		return this.direction;
	}

	public PVector setColor(final PVector p_color) {
		final PVector toRet = this.color;
		this.color = Objects.requireNonNull(p_color);
		return toRet;
	}

	public PVector setDirection(final PVector p_position) {
		final PVector toRet = this.direction;
		this.direction = Objects.requireNonNull(p_position);
		return toRet;
	}
	// endregion

}
