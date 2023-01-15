package com.brahvim.nerd_tests.examples.scene_comms.static_fields;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

public class StaticFieldCommScene2 extends NerdScene {
    // This field will store the received data.
    @SuppressWarnings("unused")
    private static Object receivedData;

    public StaticFieldCommScene2(SceneKey p_key) {
        super(p_key);
    }

    // ...this method takes in, that data!
    public static void takeSceneData(Object p_data) {
        StaticFieldCommScene2.receivedData = p_data;
    }

}
