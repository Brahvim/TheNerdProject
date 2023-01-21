package com.brahvim.nerd_test.scenes.scene_comm.static_fields;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;

public class CommScene2 extends NerdScene {
    private static SceneCommData communicatedData;

    public CommScene2(SceneKey p_key) {
        super(p_key);
    }

    public static void takeSceneData(SceneCommData p_data) {
        CommScene2.communicatedData = p_data;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void onSceneExit() {
    }

}
