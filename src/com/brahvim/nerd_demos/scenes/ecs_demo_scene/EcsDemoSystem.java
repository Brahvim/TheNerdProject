package com.brahvim.nerd_demos.scenes.ecs_demo_scene;

import java.util.HashSet;

import com.brahvim.nerd.framework.ecs.NerdEcsSystem;

public class EcsDemoSystem extends NerdEcsSystem<EcsDemoComponent> {

    @Override
    public void mouseClicked(HashSet<EcsDemoComponent> p_components) {
        for (final EcsDemoComponent c : p_components)
            System.out.println(c.message);
    }

}