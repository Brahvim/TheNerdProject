package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

import processing.core.PApplet;
import processing.core.PConstants;

public class TestScene1 extends Scene {
    public TestScene1(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
        super.startLayer(BackgroundLayer.class);
        super.startLayer(BoxAnimationLayer.class);
        super.startLayer(MouseEllipseLayer.class);
    }

    @Override
    protected void draw() {
        SKETCH.text("Scene `1`!", SKETCH.cx, SKETCH.cy);
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
