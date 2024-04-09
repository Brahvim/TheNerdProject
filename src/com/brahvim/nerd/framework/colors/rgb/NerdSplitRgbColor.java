package com.brahvim.nerd.framework.colors.rgb;

import java.awt.Color;

import com.brahvim.nerd.framework.colors.NerdNoAlphaColor;
import com.brahvim.nerd.framework.colors.NerdSplitColor;
import com.brahvim.nerd.framework.colors.hsb.NerdHsbColor;

public class NerdSplitRgbColor implements NerdRgbColor, NerdNoAlphaColor, NerdSplitColor {

	public int red, green, blue;

	// region Constructors.
	public NerdSplitRgbColor() {
	}

	public NerdSplitRgbColor(final int p_gray) {
		this.red = p_gray;
		this.blue = p_gray;
		this.green = p_gray;
	}

	public NerdSplitRgbColor(final NerdHsbColor p_hsbColor) {
		final Color rgbColor = new Color(Color.HSBtoRGB(
				p_hsbColor.getHue() / 255,
				p_hsbColor.getSaturation() / 255,
				p_hsbColor.getBrightness() / 255));

		this.red = rgbColor.getRed();
		this.blue = rgbColor.getBlue();
		this.green = rgbColor.getGreen();
	}

	public NerdSplitRgbColor(final NerdCompactArgbColor p_compactArgbColor) {
		this.red = p_compactArgbColor.getRed();
		this.blue = p_compactArgbColor.getBlue();
		this.green = p_compactArgbColor.getGreen();
	}

	// Copy-constructor:
	public NerdSplitRgbColor(final NerdSplitRgbColor p_splitRgbColor) {
		this.red = p_splitRgbColor.red;
		this.blue = p_splitRgbColor.blue;
		this.green = p_splitRgbColor.green;
	}

	// Perhaps this will be faster without the casting?:
	public NerdSplitRgbColor(final NerdSplitArgbColor p_splitColor) {
		this.red = p_splitColor.red;
		this.blue = p_splitColor.blue;
		this.green = p_splitColor.green;
	}

	public NerdSplitRgbColor(final int p_red, final int p_green, final int p_blue) {
		this.red = p_red;
		this.blue = p_blue;
		this.green = p_green;
	}
	// endregion

	// region Getters.
	@Override
	public int getRed() {
		return this.red;
	}

	@Override
	public int getBlue() {
		return this.blue;
	}

	@Override
	public int getGreen() {
		return this.green;
	}
	// endregion

	// region Setters.

	@Override
	public NerdSplitRgbColor blackOut() {
		this.red = 0;
		this.green = 0;
		this.blue = 0;

		return this;
	}

	@Override
	public NerdSplitRgbColor whiteOut() {
		this.red = 255;
		this.green = 255;
		this.blue = 255;

		return this;
	}

	@Override
	public NerdSplitRgbColor setGray(final int p_gray) {
		this.red = p_gray;
		this.green = p_gray;
		this.blue = p_gray;

		return this;
	}

	@Override
	public NerdSplitRgbColor setRed(final int p_value) {
		this.red = p_value;
		return this;
	}

	@Override
	public NerdSplitRgbColor setBlue(final int p_value) {
		this.blue = p_value;
		return this;
	}

	@Override
	public NerdSplitRgbColor setGreen(final int p_value) {
		this.green = p_value;
		return this;
	}

	@Override
	public NerdSplitRgbColor setParam1(final float p_value) {
		this.red = (int) p_value;
		return this;
	}

	@Override
	public NerdSplitRgbColor setParam2(final float p_value) {
		this.green = (int) p_value;
		return this;
	}

	@Override
	public NerdSplitRgbColor setParam3(final float p_value) {
		this.blue = (int) p_value;
		return this;
	}
	// endregion

}
