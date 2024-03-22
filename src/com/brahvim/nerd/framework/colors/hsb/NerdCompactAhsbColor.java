package com.brahvim.nerd.framework.colors.hsb;

import com.brahvim.nerd.framework.colors.NerdCompactColor;

public class NerdCompactAhsbColor implements NerdAlphaHsbColor, NerdCompactColor {

    public int color;

    public NerdCompactAhsbColor(final int p_color) {
        this.color = p_color;
    }

    // region Getters.
    @Override
    public float getParam1() {
        return getHue();
    }

    @Override
    public float getParam2() {
        return getSaturation();
    }

    @Override
    public float getParam3() {
        return this.getBrightness();
    }

    public float getHue() {
        return ((this.color >> 16) & 0xFF) / 255.0f;
    }

    public float getSaturation() {
        return ((this.color >> 8) & 0xFF) / 255.0f;
    }

    @Override
    public float getBrightness() {
        return (this.color & 0xFF) / 255.0f;
    }

    @Override
    public int getAlpha() {
        return ((this.color >> 24) & 0xFF) / 255;
    }
    // endregion

    // region Setters.
    @Override
    public NerdCompactAhsbColor blackOut() {
        this.color = 0;
        return this;
    }

    @Override
    public NerdCompactAhsbColor whiteOut() {
        this.color = 0xFFFFFFFF;
        return this;
    }

    @Override
    public NerdCompactAhsbColor setGray(int p_gray) {

        return this;
    }

    @Override
    public NerdCompactAhsbColor makeOpaque() {
        this.color = (this.color & 0x00FFFFFF) | 0xFF000000;
        return this;
    }

    @Override
    public NerdCompactAhsbColor makeTransparent() {
        this.color = (this.color & 0x00FFFFFF);
        return this;
    }

    public NerdCompactAhsbColor setHue(float p_value) {
        int alpha = (this.color >>> 24) & 0xFF;
        int hue = (int) (p_value * 255) << 16;
        this.color = (this.color & 0xFF00FFFF) | hue;
        this.color |= alpha << 24;
        return this;
    }

    public NerdCompactAhsbColor setSaturation(float p_value) {
        int alpha = (this.color >>> 24) & 0xFF;
        int saturation = (int) (p_value * 255) << 8;
        this.color = (this.color & 0xFFFF00FF) | saturation;
        this.color |= alpha << 24;
        return this;
    }

    public NerdCompactAhsbColor setBrightness(float p_value) {
        int alpha = (this.color >>> 24) & 0xFF;
        int brightness = (int) (p_value * 255);
        this.color = (this.color & 0xFFFFFF00) | brightness;
        this.color |= alpha << 24;
        return this;
    }

    @Override
    public NerdCompactAhsbColor setParam1(float p_value) {
        return setHue(p_value);
    }

    @Override
    public NerdCompactAhsbColor setParam2(float p_value) {
        return setSaturation(p_value);
    }

    @Override
    public NerdCompactAhsbColor setParam3(float p_value) {
        return setBrightness(p_value);
    }
    // endregion

}
