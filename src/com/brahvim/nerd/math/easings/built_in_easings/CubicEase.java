package com.brahvim.nerd.math.easings.built_in_easings;

import com.brahvim.nerd.math.easings.Easings;
import com.brahvim.nerd.math.easings.NerdEasingFunction;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class CubicEase extends NerdEasingFunction {

	public CubicEase(final Sketch p_parentSketch) {
		super(p_parentSketch);
	}

	public CubicEase endWhenParameterIs(final float p_endParam) {
		super.endAfterMillis(0);
		return this;
	}

	@Override
	public CubicEase start() {
		super.start();
		return this;
	}

	@Override
	protected float apply() {
		return Easings.cubic(super.aliveTime);
	}

}
