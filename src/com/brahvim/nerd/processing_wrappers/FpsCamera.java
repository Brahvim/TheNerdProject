package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public class FpsCamera extends FlyCamera {
    public FpsCamera(Sketch p_sketch) {
        super(p_sketch);
    }

    @Override
    public void applyMatrix() {
        // float originalY = super.pos.y;
        super.applyMatrix();
        super.pos.y = 0;
    }

}
