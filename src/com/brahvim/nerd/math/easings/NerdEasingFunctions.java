package com.brahvim.nerd.math.easings;

import com.brahvim.nerd.utils.NerdReflectionUtils;

import processing.core.PConstants;

// Thanks to `https://easings.net`!

public class NerdEasingFunctions {

	private NerdEasingFunctions() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this);

	}

	public static float cubic(final float x) {
		return 1.0f - (float) Math.pow(1 - x, 3);
	}

	public static float quadratic(final float x) {
		return x * x;
	}

	public static float exponential(final float x) {
		return x == 0 ? 0 : (float) Math.pow(2, 10 * x);
	}

	public static float easeOutElastic(final float x) {
		final float c4 = PConstants.TAU / 3.0f;

		return x == 0 ? 0 : x == 1 ? 1 : (float) Math.pow(2, -10 * x) * (float) Math.sin((x * 10 - 0.75f) * c4) + 1;
	}

	// Mine! https://www.desmos.com/calculator/88fimxy2ox:
	public static float exponentialSine(final float x, final float theta) {
		return (float) Math.pow(x, Math.sin(theta));
	}

	// Mine! https://www.desmos.com/calculator/hgs8iom7sk:
	public static float exponentialSine(final float x) {
		return (float) Math.pow(x, Math.sin(x));
	}

}
