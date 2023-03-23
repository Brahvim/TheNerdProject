package com.brahvim.nerd.processing_wrappers.cameras;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public class FpsCamera extends FlyCamera {
    private float height;

    public FpsCamera(Sketch p_sketch) {
        super(p_sketch);
    }

    @Override
    public void applyMatrix() {
        super.pos.y = this.height;
        super.applyMatrix();
    }

    @Override
    public void moveY(float p_velY) {
        this.height += p_velY;
    }

}
