package com.brahvim.nerd.framework.colors.rgb;

public class NerdSplitArgbColor implements NerdAlphaRgbColor {

    public int red, green, blue;
    public int alpha = 255;

    // region Constructors.
    public NerdSplitArgbColor() {
    }

    public NerdSplitArgbColor(final int p_gray) {
        this.red = p_gray;
        this.green = p_gray;
        this.blue = p_gray;
    }

    // Perhaps this will be faster without the casting?:
    public NerdSplitArgbColor(final NerdSplitRgbColor p_floatColor) {
        this.red = p_floatColor.red;
        this.green = p_floatColor.green;
        this.blue = p_floatColor.blue;
    }

    public NerdSplitArgbColor(final NerdSplitArgbColor p_floatColor) {
        this.red = p_floatColor.red;
        this.green = p_floatColor.green;
        this.blue = p_floatColor.blue;

        this.alpha = p_floatColor.alpha;
    }

    public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue) {
        this.red = p_red;
        this.green = p_green;
        this.blue = p_blue;
    }

    public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue, final int p_alpha) {
        this.red = p_red;
        this.green = p_green;
        this.blue = p_blue;

        this.alpha = p_alpha;
    }
    // endregion

    // Getter:
    @Override
    public int getAlpha() {
        return this.alpha;
    }

    // region Getters.
    @Override
    public int getRed() {
        return this.red;
    }

    @Override
    public int getGreen() {
        return this.green;
    }

    @Override
    public int getBlue() {
        return this.blue;
    }

    /** @return {@code -1} if this color isn't gray, else the gray value. */
    @Override
    public int getIfGray() {
        return this.isGray() ? this.red : -1;
    }
    // endregion

    // Query:
    @Override
    public boolean isGray() {
        return this.red == this.green && this.green == this.blue;
    }

    // region Setters.
    @Override
    public NerdSplitArgbColor blackOut() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;

        return this;
    }

    @Override
    public NerdSplitArgbColor whiteOut() {
        this.red = 255;
        this.green = 255;
        this.blue = 255;

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
        this.green = p_gray;
        this.blue = p_gray;

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
