package com.brahvim.nerd_demos.scenes.ecs_demo_scene;

import java.io.File;

import com.brahvim.nerd.framework.ecs.NerdEcsEntity;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;

import processing.core.PConstants;
import processing.event.MouseEvent;

public class EcsDemoScene extends NerdScene {

	private static final File SAVE_FILE = new File("data/Nerd EcsDemoScene Serial Data.sav");
	private EcsDemoComponent component;
	private NerdEcsEntity entity;

	@Override
	protected void setup(final NerdSceneState p_state) {
		ECS.setSystemsOrder(EcsDemoSystem.class);

		this.entity = ECS.createEntity("Test!");
		this.component = this.entity.attachComponent(EcsDemoComponent.class);
		// this.component.message = "Yo! This message didn't need"
		// + "to be changed but I did it anyway, haha.";

		for (int i = 0; i < 5; i++)
			ECS.createEntity();

		ECS.forEachEntity(e -> System.out.println(ECS.getEntityName(e)));
	}

	@Override
	protected void draw() {
		if (this.component != null)
			GRAPHICS.background(this.component.grey);
		else
			GRAPHICS.background(0);
	}

	@Override
	public void mouseClicked() {
		// Entity addition and removal test:
		// switch (INPUT.mouseButton) {
		// case PConstants.LEFT -> {
		// // Add the component:
		// if (!this.entity.hasComponent(this.component)) {
		// this.component = this.entity.attachComponent(EcsDemoComponent.class);
		// System.out.println("Component added.");
		// }
		// }

		// case PConstants.RIGHT -> {
		// // Remove the component:
		// if (this.entity.hasComponent(this.component)) {
		// this.entity.removeComponent(this.component.getClass());
		// this.component = null;
		// System.out.println("Component removed.");
		// }
		// }
		// }

		// ECS serialization test:
		switch (INPUT.mouseButton) {
			case PConstants.LEFT -> ECS.loadState(EcsDemoScene.SAVE_FILE);
			case PConstants.RIGHT -> ECS.saveState(EcsDemoScene.SAVE_FILE);
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.entity.hasComponent(this.component, c -> c.grey += p_mouseEvent.getCount() * 3.5f);
	}

}
