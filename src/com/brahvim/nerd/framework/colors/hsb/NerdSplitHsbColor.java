package com.brahvim.nerd.framework.colors.hsb;

import java.awt.Color;

import com.brahvim.nerd.framework.colors.NerdNoAlphaColor;
import com.brahvim.nerd.framework.colors.NerdSplitColor;
import com.brahvim.nerd.framework.colors.rgb.NerdRgbColor;

public class NerdSplitHsbColor implements NerdHsbColor, NerdNoAlphaColor, NerdSplitColor {

	public float hue, saturation = 255, brightness = 255;

	// region Constructors.
	public NerdSplitHsbColor() {
	}

	public NerdSplitHsbColor(final float p_hue) {
		this.hue = p_hue;
	}

	// Generic HSB constructor:
	public NerdSplitHsbColor(final NerdHsbColor p_hsbColor) {
		this.hue = p_hsbColor.getHue();
		this.saturation = p_hsbColor.getSaturation();
		this.brightness = p_hsbColor.getBrightness();
	}

	// Generic RGB constructor. Can't have specific ones here, 'cause the method
	// calls do pretty much the same work:
	public NerdSplitHsbColor(final NerdRgbColor p_rgbColor) {
		final float[] hsbValues = Color.RGBtoHSB(
				p_rgbColor.getRed(), p_rgbColor.getGreen(), p_rgbColor.getBlue(), null);

		this.hue = hsbValues[0];
		this.saturation = hsbValues[1];
		this.brightness = hsbValues[2];
	}

	// Copy-constructor:
	public NerdSplitHsbColor(final NerdSplitHsbColor p_splitHsbColor) {
		this.hue = p_splitHsbColor.hue;
		this.saturation = p_splitHsbColor.saturation;
		this.brightness = p_splitHsbColor.brightness;
	}

	// For compile-time performance gains:
	public NerdSplitHsbColor(final NerdSplitAhsbColor p_splitAhsbColor) {
		this.hue = p_splitAhsbColor.hue;
		this.saturation = p_splitAhsbColor.saturation;
		this.brightness = p_splitAhsbColor.brightness;
	}

	public NerdSplitHsbColor(final float p_hue, final float p_saturation) {
		this.hue = p_hue;
		this.saturation = p_saturation;
	}

	public NerdSplitHsbColor(final float p_hue, final float p_saturation, final float p_brightness) {
		this.hue = p_hue;
		this.saturation = p_saturation;
		this.brightness = p_brightness;
	}
	// endregion

	// VSCode just called it this and not "`extracted()`".
	// ???!
	@SuppressWarnings("unused")
	private void getDelta(final NerdRgbColor p_rgbColor) {
		// Normalize!:
		final float r = p_rgbColor.getRed() / 255.0f;
		final float b = p_rgbColor.getBlue() / 255.0f;
		final float g = p_rgbColor.getGreen() / 255.0f;

		// Max and min for some reason:
		final float max = Math.max(Math.max(r, g), b);
		final float min = Math.min(Math.min(r, g), b);

		// Calculate brightness:
		this.brightness = max;

		// Calculate saturation:
		this.saturation = max == 0 ? 0 : (max - min) / max;

		// Calculate hue:
		if (max == min) {
			this.hue = 0; // Achromatic component (gray).
			return;
		}

		final float delta = max - min;
		if (max == r)
			this.hue = (g - b) / delta + (g < b ? 6 : 0);
		else if (max == g)
			this.hue = (b - r) / delta + 2;
		else
			this.hue = (r - g) / delta + 4;

		this.hue /= 6;
	}

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
	// endregion

	// region Setters.
	@Override
	public NerdSplitHsbColor blackOut() {
		this.setGray(0);
		return this;
	}

	@Override
	public NerdSplitHsbColor whiteOut() {
		this.setGray(255);
		return this;
	}

	@Override
	public NerdSplitHsbColor setGray(final int p_gray) {
		this.saturation = 0;
		this.brightness = p_gray;
		return this;
	}

	@Override
	public NerdSplitHsbColor setHue(final float p_value) {
		this.hue = p_value;
		return this;
	}

	@Override
	public NerdSplitHsbColor setSaturation(final float p_value) {
		this.saturation = p_value;
		return this;
	}

	@Override
	public NerdSplitHsbColor setBrightness(final float p_value) {
		this.brightness = p_value;
		return this;
	}
	// endregion

}
