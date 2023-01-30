package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.processing_wrappers.FlyCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TestScene6 extends NerdScene {
    private FlyCamera CAMERA;

    @Override
    protected void setup(SceneState p_state) {
        CAMERA = new FlyCamera(SKETCH);
        SKETCH.setCamera(CAMERA);
    }

    @Override
    protected void draw() {

    }

}
