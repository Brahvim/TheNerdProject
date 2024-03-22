package com.brahvim.nerd.framework.colors;

public interface NerdProcessingColor extends NerdColor {

    public float getParam1();

    public float getParam2();

    public float getParam3();

    /** @return {@code -1} if this color isn't gray, else the gray value. */
    public default float getIfGray() {
        return this.isGray() ? this.getParam1() : -1;
    }

    @Override
    public NerdProcessingColorSpace getColorSpace();

    // Query:
    public default boolean isGray() {
        final float param2 = this.getParam2();
        return this.getParam1() == param2
                && param2 == this.getParam3();
    }

    public NerdProcessingColor blackOut();

    public NerdProcessingColor whiteOut();

    public NerdProcessingColor setGray(int gray);

    public NerdProcessingColor setParam1(float value);

    public NerdProcessingColor setParam2(float value);

    public NerdProcessingColor setParam3(float value);

}
