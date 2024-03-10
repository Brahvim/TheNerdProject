package com.brahvim.nerd.framework.color.rgba;

public interface NerdRgbColor {

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
