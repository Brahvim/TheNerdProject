package com.brahvim.nerd.framework.ecs;

import java.util.LinkedList;

public class NerdEcsSystemManager {

    private final LinkedList<NerdEcsSystem> SYSTEMS;

    public NerdEcsSystemManager() {
        this.SYSTEMS = new LinkedList<>();
    }

}
