package com.brahvim.nerd.framework.colors.hsb;

import java.awt.Color;

import com.brahvim.nerd.framework.colors.NerdSplitColor;
import com.brahvim.nerd.framework.colors.rgb.NerdAlphaRgbColor;
import com.brahvim.nerd.framework.colors.rgb.NerdRgbColor;

public class NerdSplitAhsbColor implements NerdAlphaHsbColor, NerdSplitColor {

	public float hue, saturation = 255, brightness = 255;
	public int alpha = 255;

	// region Constructors.
	public NerdSplitAhsbColor() {
	}

	public NerdSplitAhsbColor(final float p_hue) {
		this.hue = p_hue;
	}

	public NerdSplitAhsbColor(final NerdRgbColor p_rgbColor) {
		final float[] components = Color.RGBtoHSB(
				p_rgbColor.getRed(), p_rgbColor.getGreen(), p_rgbColor.getBlue(), new float[3]);

		this.hue = components[0];
		this.saturation = components[1];
		this.brightness = components[2];
	}

	public NerdSplitAhsbColor(final NerdHsbColor p_hsbColor) {
		this.hue = p_hsbColor.getHue();
		this.saturation = p_hsbColor.getSaturation();
		this.brightness = p_hsbColor.getBrightness();
	}

	public NerdSplitAhsbColor(final NerdSplitHsbColor p_color) {
		this.hue = p_color.hue;
		this.saturation = p_color.saturation;
		this.brightness = p_color.brightness;
	}

	// Copy-constructor:
	public NerdSplitAhsbColor(final NerdSplitAhsbColor p_color) {
		this.hue = p_color.hue;
		this.alpha = p_color.alpha;
		this.saturation = p_color.saturation;
		this.brightness = p_color.brightness;
	}

	public NerdSplitAhsbColor(final NerdAlphaHsbColor p_ahsbColor) {
		this.hue = p_ahsbColor.getHue();
		this.alpha = p_ahsbColor.getAlpha();
		this.saturation = p_ahsbColor.getSaturation();
		this.brightness = p_ahsbColor.getBrightness();
	}

	public NerdSplitAhsbColor(final NerdAlphaRgbColor p_argbColor) {
		final float[] components = Color.RGBtoHSB(
				p_argbColor.getRed(), p_argbColor.getGreen(), p_argbColor.getBlue(), new float[3]);

		this.hue = components[0];
		this.saturation = components[1];
		this.brightness = components[2];
	}

	public NerdSplitAhsbColor(final NerdCompactAhsbColor p_compactAhsbColor) {
		this.hue = p_compactAhsbColor.getHue();
		this.alpha = p_compactAhsbColor.getAlpha();
		this.saturation = p_compactAhsbColor.getSaturation();
		this.brightness = p_compactAhsbColor.getBrightness();
	}

	public NerdSplitAhsbColor(final NerdRgbColor p_rgbColor, final int p_alpha) {
		final float[] components = Color.RGBtoHSB(
				p_rgbColor.getRed(), p_rgbColor.getGreen(), p_rgbColor.getBlue(), new float[3]);

		this.alpha = p_alpha;
		this.hue = components[0];
		this.saturation = components[1];
		this.brightness = components[2];
	}

	// Using individual `float`s:

	public NerdSplitAhsbColor(final float p_hue, final float p_saturation) {
		this.hue = p_hue;
		this.saturation = p_saturation;
	}

	public NerdSplitAhsbColor(final float p_hue, final float p_saturation, final float p_brightness) {
		this.hue = p_hue;
		this.saturation = p_saturation;
		this.brightness = p_brightness;
	}

	public NerdSplitAhsbColor(final float p_hue, final float p_saturation, final float p_brightness,
			final int p_alpha) {
		this.hue = p_hue;
		this.alpha = p_alpha;
		this.saturation = p_saturation;
		this.brightness = p_brightness;
	}
	// endregion

	// region Getters.
	@Override
	public float getHue() {
		return this.hue;
	}

	@Override
	public float getSaturation() {
		return this.saturation;
	}

	@Override
	public float getBrightness() {
		return this.brightness;
	}

	@Override
	public int getAlpha() {
		return this.alpha;
	}
	// endregion

	// region Setters.
	@Override
	public NerdSplitAhsbColor setHue(final float p_value) {
		this.hue = p_value;
		return this;
	}

	@Override
	public NerdSplitAhsbColor setAlpha(final int p_value) {
		this.alpha = p_value;
		return this;
	}

	@Override
	public NerdSplitAhsbColor setSaturation(final float p_value) {
		this.saturation = p_value;
		return this;
	}

	@Override
	public NerdSplitAhsbColor setBrightness(final float p_value) {
		this.brightness = p_value;
		return this;
	}

	@Override
	public NerdSplitAhsbColor makeOpaque() {
		this.alpha = 0;
		return this;
	}

	@Override
	public NerdSplitAhsbColor makeTransparent() {
		this.alpha = 100;
		return this;
	}

	@Override
	public NerdSplitAhsbColor blackOut() {
		this.saturation = 0;
		this.brightness = 0;
		return this;
	}

	@Override
	public NerdSplitAhsbColor whiteOut() {
		return this;
	}

	@Override
	public NerdSplitAhsbColor setGray(final int p_gray) {
		this.saturation = 0;
		this.brightness = p_gray;
		return this;
	}
	// endregion

}
