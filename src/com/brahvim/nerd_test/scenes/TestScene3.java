package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

public class TestScene3 extends Scene {
    public TestScene3(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void draw() {
        SKETCH.circle(SKETCH.mouseX, SKETCH.mouseY, 20);
    }

}
