package com.brahvim.nerd.math.easings.built_in_easings;

import com.brahvim.nerd.math.easings.NerdEasingFunctions;
import com.brahvim.nerd.math.easings.NerdEasingFunction;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdQuadraticEase extends NerdEasingFunction {

	public NerdQuadraticEase(final NerdSketch p_parentSketch) {
		super(p_parentSketch);
	}

	@Override
	public NerdQuadraticEase start() {
		super.start();
		return this;
	}

	@Override
	protected float apply() {
		return NerdEasingFunctions.quadratic(super.aliveTime);
	}

}
