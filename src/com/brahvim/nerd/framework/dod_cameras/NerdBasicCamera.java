package com.brahvim.nerd.framework.dod_cameras;

import java.util.Objects;

import processing.core.PVector;

public class NerdBasicCamera extends NerdAbstractCamera {

    private PVector center;

    public PVector getCenter() {
        return this.center;
    }

    @Override
    public PVector setPosition(final PVector p_center) {
        final PVector toRet = this.center;
        this.center = Objects.requireNonNull(p_center);
        return toRet;
    }

}
