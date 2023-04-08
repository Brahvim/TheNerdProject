package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.rendering.cameras.FpsCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.layers.PauseMenuLayer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class TestScene6 extends NerdScene {

    // region Fields.
    private FpsCamera CAMERA;
    private final PVector playerVel = new PVector(10, 7, 10);

    private final float GRAVITY = 2;
    private final float PLAYER_START_Y = -1500;
    private final float PLAYER_MIN_Y = -200 - this.GRAVITY;
    // endregion

    @Override
    protected void setup(final SceneState p_state) {
        // Loaded this scene for the first time? Do this!:
        if (this.SCENE.getTimesLoaded() == 0) {
            this.SKETCH.centerWindow();
            this.SKETCH.fullscreen = true;
        }

        this.SCENE.addLayers(PauseMenuLayer.class);

        // Need to do this!...:
        this.CAMERA = this.STATE.get("Camera", new FpsCamera(this.SKETCH));
        this.CAMERA.setClearColor(0x006699);
        this.STATE.set("TimesLoaded", this.SCENE.getTimesLoaded());

        // Do not forget to do these!:
        this.SKETCH.cursorVisible = false;
        this.SKETCH.setCamera(this.CAMERA);
        // The camera won't be "auto-used" otherwise!!!

        // Give us a "starting position"!:
        this.CAMERA.pos.set(this.SKETCH.cx, this.PLAYER_START_Y);
    }

    @Override
    protected void draw() {
        this.CAMERA.fov = PConstants.PI / 3 + 0.01f * this.SKETCH.mouseScroll;
        this.CAMERA.pos.y = PApplet.sin(this.SKETCH.millis() * 0.001f) * 25;
        this.controlCamera();

        // region Actual rendering!
        this.SKETCH.translate(0, 800);

        // Box in center:
        this.SKETCH.pushMatrix();
        this.SKETCH.translate(0, 165, 0);
        this.SKETCH.box(50);
        this.SKETCH.popMatrix();

        // Ground:
        this.SKETCH.pushMatrix();
        this.SKETCH.translate(0, 200);
        this.SKETCH.box(2000, 20, 2000);
        this.SKETCH.popMatrix();
        // endregion

    }

    @Override
    public void keyPressed() {
        if (this.SKETCH.keyIsPressed(KeyEvent.VK_F)) {
            this.SKETCH.cursorVisible = !this.SKETCH.cursorVisible;
            this.CAMERA.holdMouse = !this.CAMERA.holdMouse;
        }

        if (this.SKETCH.keysPressed(KeyEvent.VK_CONTROL, KeyEvent.VK_R)) {
            this.CAMERA.completeReset();
            this.STATE.set("Camera", this.CAMERA);
            this.MANAGER.restartScene(this.STATE);
        }
    }

    private void controlCamera() {
        // region Gravity:
        this.CAMERA.pos.y += this.GRAVITY;

        // Don't-go-underneath check:
        if (this.CAMERA.pos.y > this.PLAYER_MIN_Y)
            this.CAMERA.pos.y = this.PLAYER_MIN_Y;
        // endregion

        // region Key-press handling.
        // Increase speed when holding `Ctrl`:
        /* final */ float velMultiplier = 1;

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            velMultiplier = 2;

        // region Roll.
        if (this.SKETCH.keyIsPressed(KeyEvent.VK_Z))
            this.CAMERA.up.x += velMultiplier * 0.01f;

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_C))
            this.CAMERA.up.x += -velMultiplier * 0.01f;
        // endregion

        // region Elevation.
        if (this.SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
            this.CAMERA.moveY(this.GRAVITY * velMultiplier * -this.playerVel.y);

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_SHIFT))
            this.CAMERA.moveY(velMultiplier * this.playerVel.y);
        // endregion

        // region `W`-`A`-`S`-`D` controls.
        if (this.SKETCH.keyIsPressed(KeyEvent.VK_W))
            this.CAMERA.moveZ(velMultiplier * -this.playerVel.z);

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_A))
            this.CAMERA.moveX(velMultiplier * -this.playerVel.x);

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_S))
            this.CAMERA.moveZ(velMultiplier * this.playerVel.z);

        if (this.SKETCH.keyIsPressed(KeyEvent.VK_D))
            this.CAMERA.moveX(velMultiplier * this.playerVel.x);
        // endregion
        // endregion
    }

}
