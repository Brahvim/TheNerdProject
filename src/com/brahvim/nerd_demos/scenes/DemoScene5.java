package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;

import processing.core.PApplet;

public class DemoScene5 extends NerdScene {

	private final String TEXT = String.format(
			"Welcome to `%s`!",
			this.getClass().getSimpleName());

	@Override
	protected void setup(final NerdSceneState p_state) {
		System.out.println(this.TEXT);
		GRAPHICS.getCurrentCamera().getPos().z = 500;
	}

	@Override
	protected void draw() {
		GRAPHICS.background(0x006699, PApplet.sin(super.getMillisSinceStart()));
		GRAPHICS.fill(233);

		GRAPHICS.pushMatrix();
		GRAPHICS.scale(2);
		GRAPHICS.text(this.TEXT, 0, 0);
		GRAPHICS.popMatrix();

		GRAPHICS.circle(SKETCH.mouseX - WINDOW.cx, SKETCH.mouseY - WINDOW.cy, 20);
	}

	@Override
	public void mouseClicked() {
		MANAGER.startScene(DemoScene4.class);
	}

}
