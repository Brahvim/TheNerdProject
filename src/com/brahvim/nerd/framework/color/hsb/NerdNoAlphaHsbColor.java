package com.brahvim.nerd.framework.color.hsb;

import com.brahvim.nerd.framework.color.NerdNoAlphaColor;
import com.brahvim.nerd.framework.color.rgb.NerdRgbColor;

public class NerdNoAlphaHsbColor implements NerdNoAlphaColor {

    public float hue, saturation = 255, brightness = 255;

    // region Constructors.
    public NerdNoAlphaHsbColor() {
    }

    public NerdNoAlphaHsbColor(final float p_hue) {
        this.hue = p_hue;
    }

    public NerdNoAlphaHsbColor(final NerdRgbColor p_rgbColor) {
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

    public NerdNoAlphaHsbColor(final float p_hue, final float p_saturation) {
        this.hue = p_hue;
        this.saturation = p_saturation;
    }

    public NerdNoAlphaHsbColor(final float p_hue, final float p_saturation, final float p_brightness) {
        this.hue = p_hue;
        this.saturation = p_saturation;
        this.brightness = p_brightness;
    }
    // endregion

    // region Getters.
    @Override
    public float getParam1() {
        return this.hue;
    }

    @Override
    public float getParam2() {
        return this.saturation;
    }

    @Override
    public float getParam3() {
        return this.brightness;
    }
    // endregion

    // region Setters.
    @Override
    public NerdNoAlphaHsbColor setParam1(final float p_value) {
        this.hue = p_value;
        return this;
    }

    @Override
    public NerdNoAlphaHsbColor setParam2(final float p_value) {
        this.saturation = p_value;
        return this;
    }

    @Override
    public NerdNoAlphaHsbColor setParam3(final float p_value) {
        this.brightness = p_value;
        return this;
    }
    // endregion

}
