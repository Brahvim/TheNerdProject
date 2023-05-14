package com.brahvim.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.cameras.NerdFpsCamera;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class DemoScene6 extends NerdScene {

	// region Fields.
	private NerdFpsCamera CAMERA;
	private final PVector playerVel = new PVector(10, 7, 10);

	private final float GRAVITY = 2;
	private final float PLAYER_START_Y = -1500;
	private final float PLAYER_MIN_Y = -200 - this.GRAVITY;
	// endregion

	@Override
	protected void setup(final NerdSceneState p_state) {
		// Loaded this scene for the first time? Do this!:
		if (this.SCENE.getTimesLoaded() == 0) {
			WINDOW.centerWindow();
			WINDOW.fullscreen = true;
		}

		// Need to do this!...:
		CAMERA = STATE.get("Camera", new NerdFpsCamera(SKETCH));
		CAMERA.setClearColor(0x006699);
		STATE.set("TimesLoaded", this.SCENE.getTimesLoaded());

		// Do not forget to do these!:
		WINDOW.cursorVisible = false;
		SKETCH.setCamera(CAMERA);
		// The camera won't be "auto-used" otherwise!!!

		// Give us a "starting position"!:
		CAMERA.getPos().set(WINDOW.cx, this.PLAYER_START_Y);
	}

	@Override
	protected void draw() {
		CAMERA.fov = PConstants.PI / 3 + 0.01f * SKETCH.mouseScroll;
		CAMERA.getPos().y = PApplet.sin(SKETCH.millis() * 0.001f) * 25;
		this.controlCamera();

		// region Actual rendering!
		SKETCH.translate(0, 800);

		// Box in center:
		SKETCH.pushMatrix();
		SKETCH.translate(0, 165, 0);
		SKETCH.box(50);
		SKETCH.popMatrix();

		// Ground:
		SKETCH.pushMatrix();
		SKETCH.translate(0, 200);
		SKETCH.box(2000, 20, 2000);
		SKETCH.popMatrix();
		// endregion

	}

	@Override
	public void keyPressed() {
		if (SKETCH.keyIsPressed(KeyEvent.VK_F)) {
			WINDOW.cursorVisible = !WINDOW.cursorVisible;
			CAMERA.holdMouse = !CAMERA.holdMouse;
		}

		if (SKETCH.keysPressed(KeyEvent.VK_CONTROL, KeyEvent.VK_R)) {
			CAMERA.completeReset();
			STATE.set("Camera", CAMERA);
			MANAGER.restartScene(STATE);
		}
	}

	private void controlCamera() {
		// region Gravity:
		CAMERA.getPos().y += this.GRAVITY;

		// Don't-go-underneath check:
		if (CAMERA.getPos().y > this.PLAYER_MIN_Y)
			CAMERA.getPos().y = this.PLAYER_MIN_Y;
		// endregion

		// region Key-press handling.
		// Increase speed when holding `Ctrl`:
		/* final */ float velMultiplier = 1;

		if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
			velMultiplier = 2;

		// region Roll.
		if (SKETCH.keyIsPressed(KeyEvent.VK_Z))
			CAMERA.getUp().x += velMultiplier * 0.01f;

		if (SKETCH.keyIsPressed(KeyEvent.VK_C))
			CAMERA.getUp().x += -velMultiplier * 0.01f;
		// endregion

		// region Elevation.
		if (SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
			CAMERA.moveY(this.GRAVITY * velMultiplier * -this.playerVel.y);

		if (SKETCH.keyIsPressed(KeyEvent.VK_SHIFT))
			CAMERA.moveY(velMultiplier * this.playerVel.y);
		// endregion

		// region `W`-`A`-`S`-`D` controls.
		if (SKETCH.keyIsPressed(KeyEvent.VK_W))
			CAMERA.moveZ(velMultiplier * -this.playerVel.z);

		if (SKETCH.keyIsPressed(KeyEvent.VK_A))
			CAMERA.moveX(velMultiplier * -this.playerVel.x);

		if (SKETCH.keyIsPressed(KeyEvent.VK_S))
			CAMERA.moveZ(velMultiplier * this.playerVel.z);

		if (SKETCH.keyIsPressed(KeyEvent.VK_D))
			CAMERA.moveX(velMultiplier * this.playerVel.x);
		// endregion
		// endregion
	}

}
