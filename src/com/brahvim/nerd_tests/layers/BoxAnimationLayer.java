package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.scene_api.NerdLayer;

import processing.core.PApplet;
import processing.core.PConstants;

public class BoxAnimationLayer extends NerdLayer {

    private SineEase squareAmpWave, camRotWave;

    @Override
    protected void setup() {
        this.squareAmpWave = new SineEase(this.SKETCH, 100 / 60_000.0f);
        this.squareAmpWave.absoluteValue = true;
        this.squareAmpWave.start();

        this.camRotWave = new SineEase(this.SKETCH, 50 / 60_000.0f);
        // this.camRotWave.absoluteValue = true;
        this.camRotWave.start();
    }

    @Override
    protected void draw() {
        final float squareAmpPure = this.squareAmpWave.get(),
                squareAmp = squareAmpPure * 150;

        // CAMERA.center.x = squareAmp / 2.5f;
        // CAMERA.pos.y = squareAmp / 1.5f;
        this.CAMERA.up.x = this.camRotWave.get() * PConstants.TAU;

        this.SKETCH.translate(
                PApplet.cos(this.SKETCH.millis() * 0.001f) * squareAmp,
                PApplet.sin(this.SKETCH.millis() * 0.001f) * squareAmp);
        this.SKETCH.rotateX(this.SKETCH.millis() * 0.001f);
        this.SKETCH.rotateY(this.SKETCH.millis() * 0.001f);
        this.SKETCH.box(45);
    }

}
