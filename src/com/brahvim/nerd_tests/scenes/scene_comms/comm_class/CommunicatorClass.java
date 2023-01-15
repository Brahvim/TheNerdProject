package com.brahvim.nerd_tests.scenes.scene_comms.comm_class;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd_tests.scenes.TestScene3;

public class CommunicatorClass {
    public final static CommunicatorClass INSTANCE = new CommunicatorClass();

    private CommunicatorClass() {
    }

    // Suppose we have a game with more than 4 scenes.
    // The game tracks how long it is being played for.

    // In scene 3, the player makes a choice, needed to be forwarded to scene 4.

    private Object scene3Choice;
    private Object timeSinceGameStarted;

    // region Methods for `canComeFromAnyScene`.
    public void setScene3Choice(Class<? extends NerdScene> p_sceneClass, Object p_data) {
        if (p_sceneClass != TestScene3.class)
            throw new IllegalArgumentException("Only `Scene3` can use this method!");

        this.scene3Choice = p_data;
    }

    public Object getScene3Choice() {
        // Perhaps also check if `TestScene4` is the one taking this?
        return this.scene3Choice;
    }
    // endregion

    // region Methods for `canOnlyComeFromScene3`.
    public Object getTimeSinceGameStarted() {
        return this.timeSinceGameStarted;
    }

    public void setTimeSinceGameStarted(Object p_data) {
        this.timeSinceGameStarted = p_data;
    }
    // endregion

}
