package com.brahvim.nerd.framework.color.rgba;

public class NerdCompactArgbColor {

    public int color;

    // region Constructors.
    public NerdCompactArgbColor(final int p_color) {
        this.color = p_color;
    }

    public NerdCompactArgbColor(final NerdCompactArgbColor p_intColor) {
        this.color = p_intColor.color;
    }

    public NerdCompactArgbColor(final NerdSplitRgbColor p_floatColor) {
        this.set(p_floatColor.red, p_floatColor.green, p_floatColor.blue, 255);
    }

    public NerdCompactArgbColor(final NerdSplitArgbColor p_floatColor) {
        this.set(p_floatColor.red, p_floatColor.green, p_floatColor.blue, p_floatColor.alpha);
    }

    public NerdCompactArgbColor(final int p_red, final int p_green, final int p_blue) {
        this.color = ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
    }
    // endregion

    // region Getters.
    public int getRed() {
        return (this.color >> 16) & 0xFF;
    }

    public int getGreen() {
        return (this.color >> 8) & 0xFF;
    }

    public int getBlue() {
        return this.color & 0xFF;
    }

    public int getAlpha() {
        return (this.color >>> 24) & 0xFF;
    }
    // endregion

    // region Setters.
    public NerdCompactArgbColor setRed(final int p_red) {
        this.color = (this.color & 0xFF00FFFF) | ((p_red & 0xFF) << 16);
        return this;
    }

    public NerdCompactArgbColor setGreen(final int p_green) {
        this.color = (this.color & 0xFFFF00FF) | ((p_green & 0xFF) << 8);
        return this;
    }

    public NerdCompactArgbColor setBlue(final int p_blue) {
        this.color = (this.color & 0xFFFFFF00) | (p_blue & 0xFF);
        return this;
    }

    public NerdCompactArgbColor setAlpha(final int p_alpha) {
        this.color = (this.color & 0x00FFFFFF) | ((p_alpha & 0xFF) << 24);
        return this;
    }

    public NerdCompactArgbColor set(final int p_red, final int p_green, final int p_blue) {
        this.color = ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
        return this;
    }

    public NerdCompactArgbColor set(final int p_red, final int p_green, final int p_blue, final int p_alpha) {
        this.color = ((p_alpha & 0xFF) << 24) | ((p_red & 0xFF) << 16) | ((p_green & 0xFF) << 8) | (p_blue & 0xFF);
        return this;
    }
    // endregion

    /** @return {@code -1} if this color isn't gray, else the gray value. */
    public int getIfGray() {
        return this.isGray() ? this.getRed() : -1;
    }

    public boolean isGray() {
        return (this.getRed() == this.getGreen()) && (this.getGreen() == this.getBlue());
    }

    public NerdCompactArgbColor blackOut() {
        this.color = 0;
        return this;
    }

    public NerdCompactArgbColor whiteOut() {
        this.color = Integer.MAX_VALUE;
        return this;
    }

    public NerdCompactArgbColor setGray(final int p_gray) {
        this.color = (p_gray & 0xFF) | ((p_gray & 0xFF) << 8) | ((p_gray & 0xFF) << 16);
        return this;
    }

}
