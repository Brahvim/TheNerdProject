package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

import processing.core.PApplet;

public class TestScene1 extends Scene {
    public TestScene1(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
        super.startLayer(TestLayer1.class);
        super.startLayer(TestLayer2.class);
    }

    @Override
    protected void draw() {
        SKETCH.text("Scene `1`!", SKETCH.cx, SKETCH.cy);
        SKETCH.currentCamera.pos.z = PApplet.abs(PApplet.sin(SKETCH.millis() * 0.001f)) * 500;

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
        MANAGER.setScene(TestScene2.class);
    }
}
