package com.brahvim.nerd.math.easings.built_in_easings;

import com.brahvim.nerd.math.easings.Easings;
import com.brahvim.nerd.math.easings.NerdEasingFunction;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class QuadraticEase extends NerdEasingFunction {

	public QuadraticEase(Sketch p_parentSketch) {
		super(p_parentSketch);
	}

	@Override
	protected float apply() {
		return Easings.quadratic(super.aliveTime);
	}

}
