package com.brahvim.nerd.framework.colors.hsb;

import com.brahvim.nerd.framework.colors.NerdAlphaColor;
import com.brahvim.nerd.framework.colors.NerdCompactColor;

public class NerdCompactAhsbColor implements NerdAlphaHsbColor, NerdCompactColor {

    public int color;

    // region Constructors.
    public NerdCompactAhsbColor(final int p_color) {
        this.color = p_color;
    }

    public NerdCompactAhsbColor(final NerdSplitHsbColor p_splitAhsbColor) {
        this.setHue(p_splitAhsbColor.hue);
        this.setSaturation(p_splitAhsbColor.saturation);
        this.setBrightness(p_splitAhsbColor.brightness);
    }

    public NerdCompactAhsbColor(final NerdSplitAhsbColor p_splitAhsbColor) {
        this.setHue(p_splitAhsbColor.hue);
        this.setAlpha(p_splitAhsbColor.alpha);
        this.setSaturation(p_splitAhsbColor.saturation);
        this.setBrightness(p_splitAhsbColor.brightness);
    }

    // Copy-constructor:
    public NerdCompactAhsbColor(final NerdCompactAhsbColor p_compactAhsbColor) {
        this.color = p_compactAhsbColor.color;
    }
    // endregion

    // region Getters.
    @Override
    public float getParam1() {
        return this.getHue();
    }

    @Override
    public float getParam2() {
        return this.getSaturation();
    }

    @Override
    public float getParam3() {
        return this.getBrightness();
    }

    @Override
    public float getHue() {
        return ((this.color >> 16) & 0xFF) / 255.0f;
    }

    @Override
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
    public NerdCompactAhsbColor setGray(final int p_gray) {

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

    @Override
    public NerdCompactAhsbColor setHue(float p_value) {
        p_value = Math.max(0, Math.min(255, (int) p_value));
        final int alpha = (this.color >>> 24) & 0xFF;
        final int hue = (int) (p_value * 255) << 16;
        this.color = (this.color & 0xFF00FFFF) | hue;
        this.color |= alpha << 24;
        return this;
    }

    @Override
    public NerdAlphaColor setAlpha(int p_value) {
        p_value = Math.max(0, Math.min(255, (int) p_value));
        final int alpha = (int) p_value;
        this.color &= 0x00FFFFFF;
        this.color |= (alpha << 24);
        return this;
    }

    @Override
    public NerdCompactAhsbColor setSaturation(float p_value) {
        p_value = Math.max(0, Math.min(255, (int) p_value));
        final int alpha = (this.color >>> 24) & 0xFF;
        final int saturation = (int) (p_value * 255) << 8;
        this.color = (this.color & 0xFFFF00FF) | saturation;
        this.color |= alpha << 24;
        return this;
    }

    @Override
    public NerdCompactAhsbColor setBrightness(float p_value) {
        p_value = Math.max(0, Math.min(255, (int) p_value));
        final int alpha = (this.color >>> 24) & 0xFF;
        final int brightness = (int) (p_value * 255);
        this.color = (this.color & 0xFFFFFF00) | brightness;
        this.color |= alpha << 24;
        return this;
    }

    @Override
    public NerdCompactAhsbColor setParam1(final float p_value) {
        return this.setHue(p_value);
    }

    @Override
    public NerdCompactAhsbColor setParam2(final float p_value) {
        return this.setSaturation(p_value);
    }

    @Override
    public NerdCompactAhsbColor setParam3(final float p_value) {
        return this.setBrightness(p_value);
    }
    // endregion

}
