package com.brahvim.nerd_demos.effect_layers;

import com.brahvim.nerd.papplet_wrapper.NerdLayer;

public class CinematicBars extends NerdLayer {

    public float rectHeight = 40;

    @Override
    protected void draw() {
        SKETCH.fill(0);

        SKETCH.begin2d();

        SKETCH.rect(0, this.rectHeight, SKETCH.width, this.rectHeight);

        // Need to re-calc WHENEVER `this.rectHeight` changes, not just window size!:
        // (And no, I won't use a getter.)
        // SKETCH.rect(0, SKETCH.height - this.rectHeight, SKETCH.width,
        // this.rectHeight);

        SKETCH.end2d();
    }

    // @Override
    // protected void draw() {
    // SKETCH.fill(0);
    // SKETCH.begin2d();
    // SKETCH.rect(0, 0, SKETCH.width, this.rectHeight);
    // SKETCH.rect(0, SKETCH.height - this.rectHeight, SKETCH.width, SKETCH.height);
    // SKETCH.end2d();
    // }

}
