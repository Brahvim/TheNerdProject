package com.brahvim.nerd.framework.colors;

import java.awt.Color;
import java.util.Objects;
import java.util.Optional;

public class NerdProcessingColor {

    public float x, y, z;

    protected NerdColorSpace colorSpace = NerdProcessingColorSpace.RGB;

    /**
     * Assumptions:
     * <ul>
     * <li>{@link NerdProcessingColorSpace} was used,
     * <li>Color-mode range is {@code [0, 0, 0]} to {@code [255, 255, 255]}.
     * </ul>
     */
    public Optional<Integer> toInt() {
        if (!(this.colorSpace instanceof NerdProcessingColorSpace))
            return Optional.empty();
        else
            return Optional.of(((byte) this.x << 16) | ((byte) this.y << 8) | (byte) this.z);
    }

    public final NerdProcessingColor toRgb() {
        final int rgb = Color.HSBtoRGB(this.x, this.y, this.z);

        this.x = (rgb >> 16) & 0xFF;
        this.y = (rgb >> 8) & 0xFF;
        this.z = rgb & 0xFF;

        return this;
    }

    public final NerdProcessingColor toHsb() {
        final float[] hsb = Color.RGBtoHSB((int) this.x, (int) this.y, (int) this.z, new float[3]);

        this.x = hsb[0];
        this.y = hsb[1];
        this.z = hsb[2];

        return this;
    }

    public final NerdColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public final NerdColorSpace setColorSpace(final NerdColorSpace p_colorSpace) {
        final NerdColorSpace toRet = this.colorSpace;
        this.colorSpace = Objects.requireNonNull(p_colorSpace);
        return toRet;
    }

    public final void setRgb(final float p_red, final float p_green, final float p_blue) {
        this.colorSpace = NerdProcessingColorSpace.RGB;
        this.x = p_red;
        this.y = p_green;
        this.z = p_blue;
    }

    public final void setHsb(final float p_hue, final float p_saturation, final float p_brightness) {
        this.colorSpace = NerdProcessingColorSpace.HSB;
        this.x = p_hue;
        this.y = p_saturation;
        this.z = p_brightness;
    }

    public final void set(final NerdColorSpace p_colorSpace, final float p_1, final float p_2, final float p_3) {
        this.x = p_1;
        this.y = p_2;
        this.z = p_3;
        this.colorSpace = p_colorSpace;
    }

}
