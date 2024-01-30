package com.brahvim.nerd.utils;

import processing.core.PVector;

public final class NerdPVectorUtils {

	private NerdPVectorUtils() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this);
	}

	// region Conversions.
	// We.. We don't care about the length of the array.
	// We just take out data and do what we would!:
	public static PVector fromArray(final int[] p_intArray) {
		return new PVector(p_intArray[0], p_intArray[1], p_intArray[2]);
	}

	public static PVector fromArray(final float[] p_floatArray) {
		return new PVector(p_floatArray[0], p_floatArray[1], p_floatArray[2]);
	}

	public static PVector fromArray(final double[] p_doubleArray) {
		return new PVector((float) p_doubleArray[0], (float) p_doubleArray[1], (float) p_doubleArray[2]);
	}
	// endregion

	public static PVector normalize(final PVector p_vec) {
		return new PVector(p_vec.x, p_vec.y, p_vec.z).normalize();
	}

	public static PVector rotate(final PVector p_vec, final float p_angleRad) {
		final PVector toRet = new PVector(p_vec.x, p_vec.y, p_vec.z);
		toRet.rotate(p_angleRad);
		return toRet;
	}

	public static PVector cross(final PVector p_a, final PVector p_b) {
		// PVector toRet = new PVector(p_a.x, p_a.y, p_a.z);
		// toRet.cross(p_b);
		// return toRet;
		return PVector.cross(p_b, p_a, null);
	}

	// region `vecLerp()` overloads.
	public static PVector vecLerp(final PVector p_from, final PVector p_to, final float p_lerpAmt) {
		return new PVector(p_from.x + (p_to.x - p_from.x) * p_lerpAmt, p_from.y + (p_to.y - p_from.y) * p_lerpAmt,
				p_from.z + (p_to.z - p_from.z) * p_lerpAmt);
	}

	public static void vecLerp(final PVector p_from, final PVector p_to, final float p_lerpAmt, PVector p_out) {
		if (p_out == null)
			p_out = new PVector();
		// ...this method remains unused in the engine. It's for users! :sparkles:

		p_out.set(p_from.x + (p_to.x - p_from.x) * p_lerpAmt, p_from.y + (p_to.y - p_from.y) * p_lerpAmt,
				p_from.z + (p_to.z - p_from.z) * p_lerpAmt);
	}
	// endregion

}
