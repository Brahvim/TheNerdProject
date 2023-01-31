package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.scene_api.NerdLayer;

public class BackgroundLayer extends NerdLayer {

    private SineWave fadeWave;

    @Override
    protected void setup() {
        this.fadeWave = new SineWave(SKETCH, 0.001f);
        this.fadeWave.absoluteValue = true;
        this.fadeWave.start();
    }

    @Override
    protected void draw() {
        SKETCH.background(0x006699);
        CAMERA.setClearColor(0x006699);

        // CAN USE `this` with lambdas!
        // ...kinda' like `with()` from Python mode in Processing?:
        // SKETCH.in2d(() -> {
        // SKETCH.rectMode(PConstants.CORNER);
        // SKETCH.rect(0, 0, SKETCH.width, SKETCH.height);
        // });
    }

}
