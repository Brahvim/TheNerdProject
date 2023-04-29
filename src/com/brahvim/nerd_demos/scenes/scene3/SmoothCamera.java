package com.brahvim.nerd_demos.scenes.scene3;

import java.util.Objects;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.rendering.cameras.FlyCamera;

import processing.core.PVector;

public class SmoothCamera extends FlyCamera {

    private PVector acc = new PVector(), vel = new PVector();

    // region Construction.
    public SmoothCamera(final Sketch p_sketch) {
        super(p_sketch);
    }

    public SmoothCamera(final Sketch p_sketch, final PVector p_defaultFront) {
        super(p_sketch, p_defaultFront);
    }
    // endregion

    // region Getters and setters.
    public PVector getVel() {
        return this.vel;
    }

    public PVector getAcc() {
        return this.acc;
    }

    public PVector setVel(final PVector p_vec) {
        return this.vel = Objects.requireNonNull(p_vec);
    }

    public PVector setAcc(final PVector p_vec) {
        return this.acc = Objects.requireNonNull(p_vec);
    }
    // endregion

}
