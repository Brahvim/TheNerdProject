package com.brahvim.nerd.framework.ecs;

public class NerdEcsManager {

    public final NerdEcsEntityManager ENTITY_MAN;
    public final NerdEcsComponentManager COMPONENT_MAN;

    public NerdEcsManager() {
        this.ENTITY_MAN = new NerdEcsEntityManager();
        this.COMPONENT_MAN = new NerdEcsComponentManager();
    }

    protected void updateAll() {
    }

    protected void updateComponents() {
        this.COMPONENT_MAN.update();
    }

    protected void updateEntities() {
        this.ENTITY_MAN.update();
    }

}
