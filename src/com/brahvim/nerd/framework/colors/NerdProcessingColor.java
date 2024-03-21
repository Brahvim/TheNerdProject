package com.brahvim.nerd.framework.colors;

import java.util.Objects;

public class NerdProcessingColor {

    public float x, y, z;

    private NerdProcessingColorSpace colorSpace = NerdProcessingColorSpace.RGB;

    public NerdProcessingColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public NerdProcessingColorSpace setColorSpace(final NerdProcessingColorSpace p_colorSpace) {
        final NerdProcessingColorSpace toRet = this.colorSpace;
        this.colorSpace = Objects.requireNonNull(p_colorSpace);
        return toRet;
    }

}
