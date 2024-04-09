package com.brahvim.nerd.framework.colors.rgb;

import java.awt.Color;

import com.brahvim.nerd.framework.colors.NerdSplitColor;
import com.brahvim.nerd.framework.colors.hsb.NerdCompactAhsbColor;
import com.brahvim.nerd.framework.colors.hsb.NerdHsbColor;

public class NerdSplitArgbColor implements NerdAlphaRgbColor, NerdSplitColor {

	public int red, green, blue;
	public int alpha = 255;

	// region Constructors.
	public NerdSplitArgbColor() {
	}

	public NerdSplitArgbColor(final int p_gray) {
		this.red = p_gray;
		this.blue = p_gray;
		this.green = p_gray;
	}

	public NerdSplitArgbColor(final NerdRgbColor p_rgbColor) {
		this.red = p_rgbColor.getRed();
		this.blue = p_rgbColor.getBlue();
		this.green = p_rgbColor.getGreen();
	}

	public NerdSplitArgbColor(final NerdHsbColor p_hsbColor) {
		final Color rgbColor = new Color(Color.HSBtoRGB(
				p_hsbColor.getHue() / 255,
				p_hsbColor.getSaturation() / 255,
				p_hsbColor.getBrightness() / 255));

		this.red = rgbColor.getRed();
		this.blue = rgbColor.getBlue();
		this.green = rgbColor.getGreen();
	}

	// Perhaps this will be faster without the casting?:
	public NerdSplitArgbColor(final NerdSplitRgbColor p_splitRgbColor) {
		this.red = p_splitRgbColor.red;
		this.blue = p_splitRgbColor.blue;
		this.green = p_splitRgbColor.green;
	}

	// Copy-constructor:
	public NerdSplitArgbColor(final NerdSplitArgbColor p_splitArgbColor) {
		this.red = p_splitArgbColor.red;
		this.blue = p_splitArgbColor.blue;
		this.green = p_splitArgbColor.green;
		this.alpha = p_splitArgbColor.alpha;
	}

	public NerdSplitArgbColor(final NerdCompactArgbColor p_compactArgbColor) {
		this.red = p_compactArgbColor.getRed();
		this.blue = p_compactArgbColor.getBlue();
		this.green = p_compactArgbColor.getGreen();
	}

	public NerdSplitArgbColor(final NerdCompactAhsbColor p_compactAhsbColor) {
		final Color rgbColor = new Color(Color.HSBtoRGB(
				p_compactAhsbColor.getHue() / 255,
				p_compactAhsbColor.getSaturation() / 255,
				p_compactAhsbColor.getBrightness() / 255));

		this.red = rgbColor.getRed();
		this.blue = rgbColor.getBlue();
		this.green = rgbColor.getGreen();
	}

	public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue) {
		this.red = p_red;
		this.blue = p_blue;
		this.green = p_green;
	}

	public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue, final int p_alpha) {
		this.red = p_red;
		this.blue = p_blue;
		this.green = p_green;
		this.alpha = p_alpha;
	}
	// endregion

	// region Getters.
	@Override
	public int getRed() {
		return this.red;
	}

	@Override
	public int getAlpha() {
		return this.alpha;
	}

	@Override
	public int getGreen() {
		return this.green;
	}

	@Override
	public int getBlue() {
		return this.blue;
	}
	// endregion

	// region Setters.
	@Override
	public NerdSplitArgbColor setAlpha(final int p_value) {
		this.alpha = p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor blackOut() {
		this.red = 0;
		this.blue = 0;
		this.green = 0;

		return this;
	}

	@Override
	public NerdSplitArgbColor whiteOut() {
		this.red = 255;
		this.blue = 255;
		this.green = 255;

		return this;
	}

	@Override
	public NerdSplitArgbColor makeOpaque() {
		this.alpha = 0;
		return this;
	}

	@Override
	public NerdSplitArgbColor makeTransparent() {
		this.alpha = 255;
		return this;
	}

	@Override
	public NerdSplitArgbColor setGray(final int p_gray) {
		this.red = p_gray;
		this.blue = p_gray;
		this.green = p_gray;

		return this;
	}

	@Override
	public NerdSplitArgbColor setRed(final int p_value) {
		this.red = p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor setGreen(final int p_value) {
		this.green = p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor setBlue(final int p_value) {
		this.blue = p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor setParam1(final float p_value) {
		this.red = (int) p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor setParam2(final float p_value) {
		this.green = (int) p_value;
		return this;
	}

	@Override
	public NerdSplitArgbColor setParam3(final float p_value) {
		this.blue = (int) p_value;
		return this;
	}
	// endregion

}
