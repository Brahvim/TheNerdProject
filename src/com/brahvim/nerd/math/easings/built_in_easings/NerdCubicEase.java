package com.brahvim.nerd.math.easings.built_in_easings;

import com.brahvim.nerd.math.easings.NerdEasingFunctions;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.math.easings.NerdEasingFunction;

public class NerdCubicEase extends NerdEasingFunction {

	public NerdCubicEase(final NerdSketch p_parentSketch) {
		super(p_parentSketch);
	}

	public NerdCubicEase endWhenParameterIs(final float p_endParam) {
		super.endAfterMillis(0);
		return this;
	}

	@Override
	public NerdCubicEase start() {
		super.start();
		return this;
	}

	@Override
	protected float apply() {
		return NerdEasingFunctions.cubic(super.aliveTime);
	}

}
