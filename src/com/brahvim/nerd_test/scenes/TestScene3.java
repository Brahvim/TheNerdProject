package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager.SceneInitializer;

public class TestScene3 extends Scene {
    public TestScene3(SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
    }

    @Override
    protected void draw() {
        SKETCH.circle(SKETCH.mouseX, SKETCH.mouseY, 20);
    }

}
