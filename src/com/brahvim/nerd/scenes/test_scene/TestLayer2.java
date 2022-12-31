package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.api.SineWave;
import com.brahvim.nerd.scene_api.Layer;
import com.brahvim.nerd.scene_api.Scene.LayerInitializer;

import processing.core.PApplet;

public class TestLayer2 extends Layer {
    SineWave squareAmpWave;

    public TestLayer2(LayerInitializer p_initializer) {
        super(p_initializer);

        this.squareAmpWave = new SineWave(SKETCH, 100 / 60_000.0f);
        this.squareAmpWave.absoluteValue = true;
        this.squareAmpWave.start();
    }

    @Override
    protected void draw() {
        float squareAmp = squareAmpWave.get() * 150;

        SKETCH.currentCamera.center.x = squareAmp / 2.5f;
        SKETCH.currentCamera.pos.y = squareAmp / 1.5f;

        SKETCH.translate(
                SKETCH.cx + PApplet.cos(SKETCH.millis() * 0.001f) * squareAmp,
                SKETCH.cy + PApplet.sin(SKETCH.millis() * 0.001f) * squareAmp);
        SKETCH.rotateX(SKETCH.millis() * 0.001f);
        SKETCH.rotateY(SKETCH.millis() * 0.001f);
        SKETCH.box(45);
    }

}
