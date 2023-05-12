package com.brahvim.nerd_demos.scenes.ecs_demo_scene;

import com.brahvim.nerd.framework.ecs.NerdEcsEntity;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scenes.NerdScene;
import com.brahvim.nerd.framework.scenes.NerdSceneState;

public class EcsDemoScene extends NerdScene {

    private NerdEcsEntity entity;

    @Override
    protected void setup(NerdSceneState p_state) {
        this.ECS.setSystemsOrder(new NerdEcsSystem<?>[] { new EcsDemoSystem() });
        this.entity = ECS.createEntity();
        final EcsDemoComponent demoComponent = this.entity.attachComponent(EcsDemoComponent.class);
        // demoComponent.message = "Yo! This message didn't need to be changed but I did
        // it anyway, haha.";
    }

    @Override
    public void mouseClicked() {
        System.out.println("`EcsDemoScene::mouseClicked()`");
    }

}
