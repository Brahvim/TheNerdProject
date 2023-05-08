package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.LinkedList;

public class NerdEcsComponentManager {

    protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
    protected final HashMap<Class<? extends NerdEcsComponent>, NerdEcsComponent> CLASS_MAP = new HashMap<>();

    public NerdEcsComponentManager() {
    }

    protected void update() {
    }

}
