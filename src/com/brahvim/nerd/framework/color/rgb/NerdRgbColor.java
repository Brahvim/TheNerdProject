package com.brahvim.nerd.framework.color.rgb;

import com.brahvim.nerd.framework.color.NerdColor;
import com.brahvim.nerd.framework.color.NerdColorSpace;

public interface NerdRgbColor extends NerdColor {

    // region Default methods.
    @Override
    public default float getParam1() {
        return this.getRed();
    }

    @Override
    public default float getParam2() {
        return this.getGreen();
    }

    @Override
    public default float getParam3() {
        return this.getBlue();
    }

    @Override
    default NerdRgbColor setParam1(final float value) {
        return this.setRed((int) value);
    }

    @Override
    default NerdRgbColor setParam2(final float value) {
        return this.setGreen((int) value);
    }

    @Override
    default NerdRgbColor setParam3(final float value) {
        return this.setBlue((int) value);
    }

    // endregion

    // region Getters.
    public int getRed();

    public int getGreen();

    public int getBlue();

    public int getIfGray();

    @Override
    default NerdColorSpace getColorSpace() {
        return NerdColorSpace.RGB;
    }
    // endregion

    // Query:
    public boolean isGray();

    // region Setters.
    public NerdRgbColor blackOut();

    public NerdRgbColor whiteOut();

    public NerdRgbColor setGray(int gray);

    public NerdRgbColor setRed(int value);

    public NerdRgbColor setGreen(int value);

    public NerdRgbColor setBlue(int value);
    // endregion

}
