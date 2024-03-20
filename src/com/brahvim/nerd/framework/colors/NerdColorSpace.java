package com.brahvim.nerd.framework.colors;

import processing.core.PConstants;

public enum NerdColorSpace {

	// There's no `ARGB` since there's no `AHSB` either!
	RGB(PConstants.RGB),
	HSB(PConstants.HSB);

	// region Class stuff.
	private final int processingConstant;

	private NerdColorSpace(final int p_processingConstant) {
		this.processingConstant = p_processingConstant;
	}

	public int getPConstant() {
		return this.processingConstant;
	}
	// endregion

}
