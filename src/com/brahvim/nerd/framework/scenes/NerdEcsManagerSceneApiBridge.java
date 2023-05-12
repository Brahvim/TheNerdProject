package com.brahvim.nerd.framework.scenes;

import com.brahvim.nerd.framework.ecs.NerdEcsManager;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManagerSceneApiBridge extends NerdEcsManager {

    /* `package` */ NerdEcsManagerSceneApiBridge(
            final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
        super(p_sketch, p_systems);
    }

    @Override
    public void runUpdates() {
        super.runUpdates();
    }

}
