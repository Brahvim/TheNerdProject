package com.brahvim.nerd.framework.colors;

public interface NerdProcessingColor extends NerdColor {

    public float getParam1();

    public float getParam2();

    public float getParam3();

    @Override
    public NerdProcessingColorSpace getColorSpace();

    public NerdColor setParam1(float value);

    public NerdColor setParam2(float value);

    public NerdColor setParam3(float value);

}
