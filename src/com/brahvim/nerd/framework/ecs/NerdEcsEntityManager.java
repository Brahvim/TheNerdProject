package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.LinkedList;

public class NerdEcsEntityManager {

    protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
    protected final HashMap<String, NerdEcsEntity> STRING_MAP = new HashMap<>();
    protected final HashMap<Integer, NerdEcsEntity> HASHES_MAP = new HashMap<>();

    public NerdEcsEntityManager() {
    }

    protected void update() {
    }

}
