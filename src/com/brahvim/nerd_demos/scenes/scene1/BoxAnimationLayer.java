package com.brahvim.nerd_demos.scenes.scene1;

import com.brahvim.nerd.framework.scenes.NerdLayer;
import com.brahvim.nerd.math.easings.built_in_easings.NerdSineEase;

import processing.core.PApplet;
import processing.core.PConstants;

public class BoxAnimationLayer extends NerdLayer {

    private NerdSineEase squareAmpWave, camRotWave;

    @Override
    protected void setup() {
        this.squareAmpWave = new NerdSineEase(SKETCH, 100 / 60_000.0f);
        this.squareAmpWave.absoluteValue = true;
        this.squareAmpWave.start();

        this.camRotWave = new NerdSineEase(SKETCH, 50 / 60_000.0f);
        // this.camRotWave.absoluteValue = true;
        this.camRotWave.start();
    }

    @Override
    protected void draw() {
        final float squareAmpPure = this.squareAmpWave.get(),
                squareAmp = squareAmpPure * 150;

        // CAMERA.center.x = squareAmp / 2.5f;
        // CAMERA.pos.y = squareAmp / 1.5f;
        CAMERA.getUp().x = this.camRotWave.get() * PConstants.TAU;

        SKETCH.translate(
                PApplet.cos(SKETCH.millis() * 0.001f) * squareAmp,
                PApplet.sin(SKETCH.millis() * 0.001f) * squareAmp);
        SKETCH.rotateX(SKETCH.millis() * 0.001f);
        SKETCH.rotateY(SKETCH.millis() * 0.001f);
        SKETCH.box(45);
    }

}
