package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.processing_wrappers.FlyCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PVector;

public class TestScene6 extends NerdScene {

    // region Fields.
    private FlyCamera CAMERA;
    private PVector playerVel = new PVector(3, 2, 3);

    private final float GRAVITY = 2;
    private final float PLAYER_START_Y = -1500;
    private final float PLAYER_MIN_Y = -200 - this.GRAVITY;
    // endregion

    @Override
    protected void setup(SceneState p_state) {
        if (SCENE.timesSceneWasLoaded() == 0)
            SKETCH.centerWindow();

        CAMERA = new FlyCamera(SKETCH);
        CAMERA.clearColor = 0x006699;
        SKETCH.setCamera(CAMERA); // Do not forget to do!
        CAMERA.pos.set(SKETCH.cx, this.PLAYER_START_Y, 160);

        // Do not forget to call!
        // The camera won't be "auto-used" otherwise!!!
    }

    @Override
    protected void draw() {
        this.controlCamera();
        System.out.println(CAMERA.pos);

        // region Actual rendering!
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

    private void controlCamera() {
        // region Gravity:
        // CAMERA.pos.y += this.GRAVITY;

        // Don't-go-underneath check:
        if (CAMERA.pos.y > this.PLAYER_MIN_Y)
            CAMERA.pos.y = this.PLAYER_MIN_Y;
        // endregion

        // region Key-press handling.
        // Increase speed when holding `Ctrl`:
        float velMultiplier = 1;

        if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            velMultiplier = 4;

        if (SKETCH.keyIsPressed(KeyEvent.VK_Q))
            CAMERA.moveY(velMultiplier * playerVel.y);

        if (SKETCH.keyIsPressed(KeyEvent.VK_E))
            CAMERA.moveY(this.GRAVITY * velMultiplier * -playerVel.y);

        // region `W`-`A`-`S`-`D` controls.
        if (SKETCH.keyIsPressed(KeyEvent.VK_W))
            CAMERA.moveZ(velMultiplier * -playerVel.z);

        if (SKETCH.keyIsPressed(KeyEvent.VK_A))
            CAMERA.moveX(velMultiplier * playerVel.x);

        if (SKETCH.keyIsPressed(KeyEvent.VK_S))
            CAMERA.moveZ(velMultiplier * playerVel.z);

        if (SKETCH.keyIsPressed(KeyEvent.VK_D))
            CAMERA.moveX(velMultiplier * -playerVel.x);
        // endregion
        // endregion

    }

}
