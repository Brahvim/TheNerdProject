package com.brahvim.nerd.math.easings_old;

// Thanks to `easings.net`!

public class NerdEasingFunctionsOld {

	private NerdEasingFunctionsOld() {
		throw new Error("Sorry, but `"
				+ this.getClass().getCanonicalName()
				+ "` is an uninstantiable, helper class.");
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

	// Mine! https://www.desmos.com/calculator/88fimxy2ox:
	public static float exponentialSine(final float x, final float theta) {
		return (float) Math.pow(x, Math.sin(theta));
	}

	// Mine! https://www.desmos.com/calculator/hgs8iom7sk:
	public static float exponentialSine(final float x) {
		return (float) Math.pow(x, Math.sin(x));
	}

}
