package com.brahvim.nerd.framework.color.rgb;

public interface NerdArgbColor extends NerdRgbColor {

    // Getter:
    public int getAlpha();

    // Query:
    public boolean isVisible();

    // region Setters.
    public NerdArgbColor makeOpaque();

    public NerdArgbColor makeTransparent();
    // endregion

}
