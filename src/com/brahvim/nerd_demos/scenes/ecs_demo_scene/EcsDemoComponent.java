package com.brahvim.nerd_demos.scenes.ecs_demo_scene;

import com.brahvim.nerd.framework.ecs.NerdEcsComponent;

public class EcsDemoComponent extends NerdEcsComponent {

	public float grey = 127;
	public String message = "...So you see, Nerd has an ECS!";

	@Override
	public void copyFieldsFrom(final NerdEcsComponent p_other) {
		final EcsDemoComponent other = (EcsDemoComponent) p_other;
		this.grey = other.grey;
		this.message = other.message;
	}

}
