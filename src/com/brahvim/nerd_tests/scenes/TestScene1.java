package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.layers.BackgroundLayer;
import com.brahvim.nerd_tests.layers.BoxAnimationLayer;
import com.brahvim.nerd_tests.layers.RevolvingParticlesLayer;

import processing.core.PApplet;
import processing.core.PConstants;

public class TestScene1 extends NerdScene {
    @Override
    protected void setup(SceneState p_state) {
        if (SCENE.timesSceneWasLoaded() == 0)
            SKETCH.centerWindow();

        SCENE.startLayers(
                // Yes, these are started in order:
                BackgroundLayer.class,
                BoxAnimationLayer.class,
                RevolvingParticlesLayer.class);
    }

    @Override
    protected void draw() {
        SKETCH.text("Scene `1`!",
                SKETCH.cx, SKETCH.cy + PApplet.sin(SCENE.millisSinceStart() * 0.005f) * 25);
        // SKETCH.cx, SKETCH.cy + PApplet.sin(MANAGER.sinceSceneStarted() * 0.0125f) *
        // 25);
        CAMERA.pos.z = PApplet.abs(PApplet.sin(SCENE.millisSinceStart() *
                0.001f)) * 500;

        /*
         * if (SKETCH.frameCount % 5 == 0) {
         * SKETCH.glWindow.setPosition(0, 0);
         * SKETCH.glWindow.setSize(
         * 250 + (int) PApplet.abs((PApplet.sin(MANAGER.sinceSceneStarted() * 0.001f) *
         * 250)),
         * 250 + (int) PApplet.abs((PApplet.sin(MANAGER.sinceSceneStarted() * 0.001f) *
         * 250)));
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
