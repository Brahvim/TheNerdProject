package com.brahvim.nerd.framework.colors;

import java.util.Optional;

public final class NerdProcessingAlphaColor extends NerdProcessingColor {

    public float a;

    public void set(
            final NerdColorSpace p_colorSpace,
            final float p_1, final float p_2,
            final float p_3, final float p_alpha) {
        this.a = p_alpha;

        this.x = p_1;
        this.y = p_2;
        this.z = p_3;
        this.colorSpace = p_colorSpace;
    }

    @Override
    public Optional<Integer> toInt() {
        if (!(this.colorSpace instanceof NerdProcessingColorSpace))
            return Optional.empty();
        else {
            final int r = (int) (super.x * 255);
            final int g = (int) (super.y * 255);
            final int b = (int) (super.z * 255);
            final int alpha = (int) (this.a * 255);
            // return Optional.of((r << 16) | (g << 8) | b);
            return Optional.of((alpha << 24) | (r << 16) | (g << 8) | b);
        }
    }

    public void setRgb(final float p_red, final float p_green, final float p_blue, final float p_alpha) {
        super.colorSpace = NerdProcessingColorSpace.RGB;
        super.x = p_red;
        super.y = p_green;
        super.z = p_blue;

        this.a = p_alpha;
    }

    public void setHsb(final float p_hue, final float p_saturation, final float p_brightness, final float p_alpha) {
        super.colorSpace = NerdProcessingColorSpace.HSB;
        super.x = p_hue;
        super.y = p_saturation;
        super.z = p_brightness;

        this.a = p_alpha;
    }

}
