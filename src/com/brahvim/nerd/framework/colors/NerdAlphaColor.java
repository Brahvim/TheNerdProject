package com.brahvim.nerd.framework.colors;

public interface NerdAlphaColor extends NerdColor {

    // Getter:
    public int getAlpha();

    // Query:
    public default boolean isVisible() {
        return this.getAlpha() != 0;
    }

    // region Setters.
    public NerdAlphaColor makeOpaque();

    public NerdAlphaColor makeTransparent();
    // endregion

}
