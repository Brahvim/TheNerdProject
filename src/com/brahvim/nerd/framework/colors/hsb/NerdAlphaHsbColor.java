package com.brahvim.nerd.framework.colors.hsb;

import com.brahvim.nerd.framework.colors.NerdAlphaColor;
import com.brahvim.nerd.framework.colors.rgb.NerdAlphaRgbColor;
import com.brahvim.nerd.framework.colors.rgb.NerdRgbColor;

public class NerdAlphaHsbColor implements NerdHsbColor, NerdAlphaColor {

    public float hue, saturation = 255, brightness = 255;
    public int alpha = 255;

    // region Constructors.
    public NerdAlphaHsbColor() {
    }

    public NerdAlphaHsbColor(final float p_hue) {
        this.hue = p_hue;
    }

    public NerdAlphaHsbColor(final NerdRgbColor p_rgbColor) {
        // Normalize!:
        final float r = p_rgbColor.getRed() / 255.0f;
        final float g = p_rgbColor.getGreen() / 255.0f;
        final float b = p_rgbColor.getBlue() / 255.0f;

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

    public NerdAlphaHsbColor(final NerdAlphaRgbColor p_argbColor) {
        this((NerdRgbColor) p_argbColor);
    }

    public NerdAlphaHsbColor(final NerdRgbColor p_rgbColor, final int p_alpha) {
        this(p_rgbColor);
        this.alpha = p_alpha;
    }

    public NerdAlphaHsbColor(final float p_hue, final float p_saturation) {
        this.hue = p_hue;
        this.saturation = p_saturation;
    }

    public NerdAlphaHsbColor(final float p_hue, final float p_saturation, final float p_brightness) {
        this.hue = p_hue;
        this.saturation = p_saturation;
        this.brightness = p_brightness;
    }

    public NerdAlphaHsbColor(final float p_hue, final float p_saturation, final float p_brightness, final int p_alpha) {
        this.hue = p_hue;
        this.saturation = p_saturation;
        this.brightness = p_brightness;

        this.alpha = p_alpha;
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
    public NerdAlphaHsbColor setHue(final float p_value) {
        this.hue = p_value;
        return this;
    }

    @Override
    public NerdAlphaHsbColor setSaturation(final float p_value) {
        this.saturation = p_value;
        return this;
    }

    @Override
    public NerdAlphaHsbColor setBrightness(final float p_value) {
        this.brightness = p_value;
        return this;
    }

    @Override
    public NerdAlphaColor makeOpaque() {
        this.alpha = 0;
        return this;
    }

    @Override
    public NerdAlphaColor makeTransparent() {
        this.alpha = 100;
        return this;
    }
    // endregion

}
