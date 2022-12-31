package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

public class TestScene2 extends Scene {
    public TestScene2(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
        super.startLayer(TestLayer1.class);
        super.startLayer(TestLayer3.class);
    }

    @Override
    protected void draw() {
        SKETCH.text("Scene `2`!", SKETCH.cx, SKETCH.cy);
    }

    @Override
    public void mouseClicked() {
        MANAGER.startPreviousScene();
    }
}
