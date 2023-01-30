package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.processing_wrappers.FpsCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TestScene6 extends NerdScene {

    private FpsCamera camera;

    @Override
    protected void setup(SceneState p_state) {
        this.camera = new FpsCamera(SKETCH);
    }

    @Override
    protected void draw() {
        
    }

}
