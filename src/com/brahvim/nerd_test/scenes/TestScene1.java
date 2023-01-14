package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager;
import com.brahvim.nerd_test.layers.BackgroundLayer;
import com.brahvim.nerd_test.layers.BoxAnimationLayer;
import com.brahvim.nerd_test.layers.RevolvingParticlesLayer;

import processing.core.PApplet;
import processing.core.PConstants;

public class TestScene1 extends NerdScene {
    public TestScene1(NerdSceneManager.SceneKey p_sceneKey) {
        super(p_sceneKey,
                // Yes, these work in order:
                BackgroundLayer.class,
                BoxAnimationLayer.class,
                RevolvingParticlesLayer.class);
    }

    @Override
    protected void draw() {
        SKETCH.text("Scene `1`!",
                SKETCH.cx, SKETCH.cy + PApplet.sin(SKETCH.millis() * 0.005f) * 25);
        // SKETCH.cx, SKETCH.cy + PApplet.sin(SKETCH.millis() * 0.0125f) * 25);
        SKETCH.currentCamera.pos.z = PApplet.abs(PApplet.sin(SKETCH.millis() *
                0.001f)) * 500;

        /*
         * if (SKETCH.frameCount % 5 == 0) {
         * SKETCH.glWindow.setPosition(0, 0);
         * SKETCH.glWindow.setSize(
         * 250 + (int) PApplet.abs((PApplet.sin(SKETCH.millis() * 0.001f) * 250)),
         * 250 + (int) PApplet.abs((PApplet.sin(SKETCH.millis() * 0.001f) * 250)));
         * }
         */

    }

    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(TestScene2.class);
        }
    }
}
