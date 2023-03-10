package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.scene_api.NerdLayer;

import processing.core.PApplet;
import processing.core.PConstants;

public class BoxAnimationLayer extends NerdLayer {

    private SineWave squareAmpWave, camRotWave;

    @Override
    protected void setup() {
        this.squareAmpWave = new SineWave(SKETCH, 100 / 60_000.0f);
        this.squareAmpWave.absoluteValue = true;
        this.squareAmpWave.start();

        this.camRotWave = new SineWave(SKETCH, 50 / 60_000.0f);
        // this.camRotWave.absoluteValue = true;
        this.camRotWave.start();
    }

    @Override
    protected void draw() {
        float squareAmpPure = this.squareAmpWave.get(),
                squareAmp = squareAmpPure * 150;

        // CAMERA.center.x = squareAmp / 2.5f;
        // CAMERA.pos.y = squareAmp / 1.5f;
        CAMERA.up.x = this.camRotWave.get() * PConstants.TAU;

        SKETCH.translate(
                SKETCH.cx + PApplet.cos(SKETCH.millis() * 0.001f) * squareAmp,
                SKETCH.cy + PApplet.sin(SKETCH.millis() * 0.001f) * squareAmp);
        SKETCH.rotateX(SKETCH.millis() * 0.001f);
        SKETCH.rotateY(SKETCH.millis() * 0.001f);
        SKETCH.box(45);
    }

}
