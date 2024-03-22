package com.brahvim.nerd.framework.colors.rgb;

import com.brahvim.nerd.framework.colors.NerdProcessingColor;
import com.brahvim.nerd.framework.colors.NerdProcessingColorSpace;

public interface NerdRgbColor extends NerdProcessingColor {

	// region Default implementations.
	@Override
	public default float getParam1() {
		return this.getRed();
	}

	@Override
	public default float getParam2() {
		return this.getGreen();
	}

	@Override
	public default float getParam3() {
		return this.getBlue();
	}

	@Override
	public default NerdRgbColor setParam1(final float value) {
		return this.setRed((int) value);
	}

	@Override
	public default NerdRgbColor setParam2(final float value) {
		return this.setGreen((int) value);
	}

	@Override
	public default NerdRgbColor setParam3(final float value) {
		return this.setBlue((int) value);
	}

	@Override
	public default NerdProcessingColorSpace getColorSpace() {
		return NerdProcessingColorSpace.RGB;
	}
	// endregion

	// region Getters.
	public int getRed();

	public int getGreen();

	public int getBlue();
	// endregion

	// region Setters.
	public NerdRgbColor setRed(int value);

	public NerdRgbColor setGreen(int value);

	public NerdRgbColor setBlue(int value);
	// endregion

}
