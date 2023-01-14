package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;

public class TestScene4 extends NerdScene {
    public TestScene4(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void draw() {
        SKETCH.circle(SKETCH.mouseX, SKETCH.mouseY, 20);
    }

}
