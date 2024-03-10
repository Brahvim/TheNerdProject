package com.brahvim.nerd.framework.color.rgb;

import com.brahvim.nerd.framework.color.NerdColor;

public interface NerdRgbColor extends NerdColor {

    // region Getters.
    public int getRed();

    public int getGreen();

    public int getBlue();

    public int getIfGray();
    // endregion

    // Query:
    public boolean isGray();

    // region Setters.
    public NerdRgbColor blackOut();

    public NerdRgbColor whiteOut();

    public NerdRgbColor setGray(int gray);
    // endregion

}
