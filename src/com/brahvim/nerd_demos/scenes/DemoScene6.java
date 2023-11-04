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
	private NerdFpsCamera myCamera;
	private final PVector playerVel = new PVector(10, 7, 10);

	private static final float GRAVITY = 2;
	private static final float PLAYER_START_Y = -1500;
	private static final float PLAYER_MIN_Y = -200 - DemoScene6.GRAVITY;
	// endregion

	@Override
	protected void setup(final NerdSceneState p_state) {
		// Loaded this scene for the first time? Do this!:
		if (this.SCENE.getTimesLoaded() == 0) {
			WINDOW.centerWindow();
			WINDOW.fullscreen = true;
		}

		// Need to do this!...:
		myCamera = STATE.get("Camera", new NerdFpsCamera(GRAPHICS));
		myCamera.setClearColor(0x006699);
		STATE.set("TimesLoaded", this.SCENE.getTimesLoaded());

		// Do not forget to do these!:
		WINDOW.cursorVisible = false;
		GRAPHICS.setCurrentCamera(myCamera);
		// The camera won't be "auto-used" otherwise!!!

		// Give us a "starting position"!:
		myCamera.getPos().set(WINDOW.cx, DemoScene6.PLAYER_START_Y);
	}

	@Override
	protected void draw() {
		myCamera.fov = PConstants.PI / 3 + 0.01f * INPUT.mouseScroll;
		myCamera.getPos().y = PApplet.sin(SKETCH.millis() * 0.001f) * 25;
		this.controlCamera();

		// region Actual rendering!
		GRAPHICS.translate(0, 800);

		// Box in center:
		GRAPHICS.pushMatrix();
		GRAPHICS.translate(0, 165, 0);
		GRAPHICS.box(50);
		GRAPHICS.popMatrix();

		// Ground:
		GRAPHICS.pushMatrix();
		GRAPHICS.translate(0, 200);
		GRAPHICS.box(2000, 20, 2000);
		GRAPHICS.popMatrix();
		// endregion

	}

	@Override
	public void keyPressed() {
		if (INPUT.keyIsPressed(KeyEvent.VK_F)) {
			WINDOW.cursorVisible = !WINDOW.cursorVisible;
			myCamera.holdMouse = !myCamera.holdMouse;
		}

		if (INPUT.keysPressed(KeyEvent.VK_CONTROL, KeyEvent.VK_R)) {
			myCamera.completeReset();
			STATE.set("Camera", myCamera);
			MANAGER.restartScene(STATE);
		}
	}

	private void controlCamera() {
		// region Gravity:
		myCamera.getPos().y += DemoScene6.GRAVITY;

		// Don't-go-underneath check:
		if (myCamera.getPos().y > DemoScene6.PLAYER_MIN_Y)
			myCamera.getPos().y = DemoScene6.PLAYER_MIN_Y;
		// endregion

		// region Key-press handling.
		// Increase speed when holding `Ctrl`:
		/* final */ float velMultiplier = 1;

		if (INPUT.keyIsPressed(KeyEvent.VK_CONTROL))
			velMultiplier = 2;

		// region Roll.
		if (INPUT.keyIsPressed(KeyEvent.VK_Z))
			myCamera.getUp().x += velMultiplier * 0.01f;

		if (INPUT.keyIsPressed(KeyEvent.VK_C))
			myCamera.getUp().x += -velMultiplier * 0.01f;
		// endregion

		// region Elevation.
		if (INPUT.keyIsPressed(KeyEvent.VK_SPACE))
			myCamera.moveY(DemoScene6.GRAVITY * velMultiplier * -this.playerVel.y);

		if (INPUT.keyIsPressed(KeyEvent.VK_SHIFT))
			myCamera.moveY(velMultiplier * this.playerVel.y);
		// endregion

		// region `W`-`A`-`S`-`D` controls.
		if (INPUT.keyIsPressed(KeyEvent.VK_W))
			myCamera.moveZ(velMultiplier * -this.playerVel.z);

		if (INPUT.keyIsPressed(KeyEvent.VK_A))
			myCamera.moveX(velMultiplier * -this.playerVel.x);

		if (INPUT.keyIsPressed(KeyEvent.VK_S))
			myCamera.moveZ(velMultiplier * this.playerVel.z);

		if (INPUT.keyIsPressed(KeyEvent.VK_D))
			myCamera.moveX(velMultiplier * this.playerVel.x);
		// endregion
		// endregion
	}

}
