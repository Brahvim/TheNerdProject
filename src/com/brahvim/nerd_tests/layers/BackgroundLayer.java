package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.scene_api.NerdLayer;

public class BackgroundLayer extends NerdLayer {

    private SineEase fadeWave;

    @Override
    protected void setup() {
        this.fadeWave = new SineEase(this.SKETCH, 0.001f);
        this.fadeWave.absoluteValue = true;
        this.fadeWave.start();
    }

    @Override
    protected void draw() {
        this.SKETCH.background(0x006699);
        this.CAMERA.setClearColor(0x006699);

        // CAN USE `this` with lambdas!
        // ...kinda' like `with()` from Python mode in Processing?:
        // SKETCH.in2d(() -> {
        // SKETCH.rectMode(PConstants.CORNER);
        // SKETCH.rect(0, 0, SKETCH.width, SKETCH.height);
        // });
    }

}
