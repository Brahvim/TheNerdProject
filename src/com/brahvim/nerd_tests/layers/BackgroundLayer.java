package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd.scene_api.NerdScene.LayerKey;

public class BackgroundLayer extends NerdLayer {
    private SineWave fadeWave;

    public BackgroundLayer(LayerKey p_initializer) {
        super(p_initializer);
    }

    @Override
    protected void setup() {
        this.fadeWave = new SineWave(SKETCH, 0.001f);
        this.fadeWave.absoluteValue = true;
        this.fadeWave.start();
    }

    @Override
    protected void draw() {
        SKETCH.background(0x006699);
        SKETCH.currentCamera.clearColor = SKETCH.lerpColor(
                SKETCH.color(0x006699), SKETCH.color(255),
                this.fadeWave.get()); // CAN USE `this` with lambdas!

        // ...kinda' like `with()` from Python mode in Processing?:
        // SKETCH.in2d(() -> {
        // SKETCH.rectMode(PConstants.CORNER);
        // SKETCH.rect(0, 0, SKETCH.width, SKETCH.height);
        // });
    }

}
