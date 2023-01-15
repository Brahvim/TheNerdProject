package com.brahvim.nerd_tests.examples.scene_comms.static_fields;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

public class StaticFieldCommScene1 extends NerdScene {
    public StaticFieldCommScene1(SceneKey p_key) {
        super(p_key);
    }

    // When this scene exits, pass that data!:
    @Override
    protected void onSceneExit() {
        StaticFieldCommScene2.takeSceneData(new Object());
    }

}
