package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public class FpsCamera extends FlyCamera {
    public float height;

    public FpsCamera(Sketch p_sketch) {
        super(p_sketch);
    }

    @Override
    public void applyMatrix() {
        // float originalY = super.pos.y;
        // super.pos.y = this.height;
        super.applyMatrix();
    }

}
