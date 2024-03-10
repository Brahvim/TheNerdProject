package com.brahvim.nerd.framework.color.rgba;

public class NerdSplitArgbColor extends NerdSplitRgbColor implements NerdArgbColor {

    public int alpha = 255;

    // region Constructors.
    public NerdSplitArgbColor() {
    }

    public NerdSplitArgbColor(final int p_gray) {
        super.red = p_gray;
        super.green = p_gray;
        super.blue = p_gray;
    }

    // Perhaps this will be faster without the casting?:
    public NerdSplitArgbColor(final NerdSplitRgbColor p_floatColor) {
        super.red = p_floatColor.red;
        super.green = p_floatColor.green;
        super.blue = p_floatColor.blue;
    }

    public NerdSplitArgbColor(final NerdSplitArgbColor p_floatColor) {
        super.red = p_floatColor.red;
        super.green = p_floatColor.green;
        super.blue = p_floatColor.blue;

        this.alpha = p_floatColor.alpha;
    }

    public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue) {
        super.red = p_red;
        super.green = p_green;
        super.blue = p_blue;
    }

    public NerdSplitArgbColor(final int p_red, final int p_green, final int p_blue, final int p_alpha) {
        super.red = p_red;
        super.green = p_green;
        super.blue = p_blue;

        this.alpha = p_alpha;
    }
    // endregion

    @Override
    public boolean isVisible() {
        return this.alpha != 0;
    }

    @Override
    public NerdSplitArgbColor blackOut() {
        super.red = 0;
        super.green = 0;
        super.blue = 0;

        return this;
    }

    @Override
    public NerdSplitArgbColor whiteOut() {
        super.red = 255;
        super.green = 255;
        super.blue = 255;

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

}
