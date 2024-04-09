package com.brahvim.nerd.framework.colors.rgb;

import java.awt.Color;

import com.brahvim.nerd.framework.colors.NerdCompactColor;
import com.brahvim.nerd.framework.colors.hsb.NerdHsbColor;

public class NerdCompactArgbColor implements NerdAlphaRgbColor, NerdCompactColor {

	public int color;

	// region Constructors.
	public NerdCompactArgbColor(final int p_color) {
		this.color = p_color;
	}

	// Generic HSB constructor:
	public NerdCompactArgbColor(final NerdHsbColor p_hsbColor) {
		this.color = Color.HSBtoRGB(
				p_hsbColor.getHue() / 255,
				p_hsbColor.getSaturation() / 255,
				p_hsbColor.getBrightness() / 255);
	}

	public NerdCompactArgbColor(final NerdSplitRgbColor p_splitRgbColor) {
		this.set(p_splitRgbColor.red, p_splitRgbColor.green, p_splitRgbColor.blue, 255);
	}

	public NerdCompactArgbColor(final NerdSplitArgbColor p_splitArgbColor) {
		this.set(p_splitArgbColor.red, p_splitArgbColor.green, p_splitArgbColor.blue, p_splitArgbColor.alpha);
	}

	// Copy-constructor:
	public NerdCompactArgbColor(final NerdCompactArgbColor p_compactArgbColor) {
		this.color = p_compactArgbColor.color;
	}

	public NerdCompactArgbColor(final int p_red, final int p_green, final int p_blue) {
		this.color = ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
	}
	// endregion

	// region Getters.
	@Override
	public int getRed() {
		return (this.color >> 16) & 0xFF;
	}

	@Override
	public int getBlue() {
		return this.color & 0xFF;
	}

	@Override
	public int getGreen() {
		return (this.color >> 8) & 0xFF;
	}

	@Override
	public int getAlpha() {
		return (this.color >>> 24) & 0xFF;
	}
	// endregion

	// region Setters.
	@Override
	public NerdCompactArgbColor setRed(final int p_red) {
		this.color = (this.color & 0xFF00FFFF) | ((p_red & 0xFF) << 16);
		return this;
	}

	@Override
	public NerdCompactArgbColor setGray(final int p_gray) {
		this.color = (p_gray & 0xFF) | ((p_gray & 0xFF) << 8) | ((p_gray & 0xFF) << 16);
		return this;
	}

	@Override
	public NerdCompactArgbColor setBlue(final int p_blue) {
		this.color = (this.color & 0xFFFFFF00) | (p_blue & 0xFF);
		return this;
	}

	@Override
	public NerdCompactArgbColor setAlpha(final int p_alpha) {
		this.color = (this.color & 0x00FFFFFF) | ((p_alpha & 0xFF) << 24);
		return this;
	}

	@Override
	public NerdCompactArgbColor setGreen(final int p_green) {
		this.color = (this.color & 0xFFFF00FF) | ((p_green & 0xFF) << 8);
		return this;
	}

	@Override
	public NerdCompactArgbColor setParam1(final float p_value) {
		this.setRed((int) p_value);
		return this;
	}

	@Override
	public NerdCompactArgbColor setParam2(final float p_value) {
		this.setGreen((int) p_value);
		return this;
	}

	@Override
	public NerdCompactArgbColor setParam3(final float p_value) {
		this.setBlue((int) p_value);
		return this;
	}

	@Override
	public NerdCompactArgbColor blackOut() {
		this.color = 0;
		return this;
	}

	@Override
	public NerdCompactArgbColor whiteOut() {
		this.color = Integer.MAX_VALUE;
		return this;
	}

	@Override
	public NerdCompactArgbColor makeOpaque() {
		this.setAlpha(255);
		return this;
	}

	@Override
	public NerdCompactArgbColor makeTransparent() {
		this.setAlpha(0);
		return this;
	}

	public NerdCompactArgbColor set(final int p_red, final int p_green, final int p_blue) {
		this.color = ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
		return this;
	}

	public NerdCompactArgbColor set(final byte p_red, final int p_green, final byte p_blue) {
		this.color = ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
		return this;
	}

	public NerdCompactArgbColor set(final int p_red, final int p_green, final int p_blue, final int p_alpha) {
		this.color = ((p_alpha & 0xFF) << 24) | ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
		return this;
	}

	public NerdCompactArgbColor set(final byte p_red, final byte p_green, final byte p_blue, final byte p_alpha) {
		this.color = ((p_alpha & 0xFF) << 24) | ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
		return this;
	}
	// endregion

}
