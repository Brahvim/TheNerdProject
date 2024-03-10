package com.brahvim.nerd.framework.color.rgb;

public class NerdSplitRgbColor implements NerdRgbColor {

    public int red, green, blue;

    // region Constructors.
    public NerdSplitRgbColor() {
    }

    public NerdSplitRgbColor(final int p_gray) {
        this.red = p_gray;
        this.green = p_gray;
        this.blue = p_gray;
    }

    public NerdSplitRgbColor(final NerdCompactArgbColor p_compactColor) {
        this.red = p_compactColor.getRed();
        this.green = p_compactColor.getGreen();
        this.blue = p_compactColor.getBlue();
    }

    public NerdSplitRgbColor(final NerdSplitRgbColor p_splitColor) {
        this.red = p_splitColor.red;
        this.green = p_splitColor.green;
        this.blue = p_splitColor.blue;
    }

    // Perhaps this will be faster without the casting?:
    public NerdSplitRgbColor(final NerdSplitArgbColor p_splitColor) {
        this.red = p_splitColor.red;
        this.green = p_splitColor.green;
        this.blue = p_splitColor.blue;
    }

    public NerdSplitRgbColor(final int p_red, final int p_green, final int p_blue) {
        this.red = p_red;
        this.green = p_green;
        this.blue = p_blue;
    }
    // endregion

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
    public NerdSplitRgbColor blackOut() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;

        return this;
    }

    @Override
    public NerdSplitRgbColor whiteOut() {
        this.red = 255;
        this.green = 255;
        this.blue = 255;

        return this;
    }

    @Override
    public NerdSplitRgbColor setGray(final int p_gray) {
        this.red = p_gray;
        this.green = p_gray;
        this.blue = p_gray;

        return this;
    }
    // endregion

}
