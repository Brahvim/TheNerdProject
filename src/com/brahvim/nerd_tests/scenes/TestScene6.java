package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.processing_wrappers.FlyCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PVector;

public class TestScene6 extends NerdScene {
    private FlyCamera CAMERA;
    private PVector playerPos, playerVel = new PVector(3, 2, 3);
    private final float PLAYER_START_Y = -190;
    private final float PLAYER_MIN_Y = 198;
    private final float GRAVITY = 2;
    // private final float GRAVITY = 9.8f;

    @Override
    protected void setup(SceneState p_state) {
        CAMERA = new FlyCamera(SKETCH);
        SKETCH.setCamera(CAMERA);
        CAMERA.getNerdCamera().clearColor = 0x006699;
        CAMERA.getNerdCamera().doAutoClear = true;
        this.playerPos = CAMERA.getNerdCamera().pos; // Hah! Pointer trick...

        this.playerPos.set(SKETCH.cx, this.PLAYER_START_Y, 160);

        // Do not forget to call!
        // The camera won't be "auto-used" otherwise!!!
    }

    @Override
    protected void draw() {
        this.controlCamera();

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
    }

    private void controlCamera() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            CAMERA.camAddent.mult(4);

        // region Gravity:
        this.playerPos.y += this.GRAVITY;

        System.out.println(this.playerPos.y);

        // region Don't-go-underneath check:
        if (this.playerPos.y < this.PLAYER_MIN_Y)
            this.playerPos.y = this.PLAYER_MIN_Y;
        // `if (this.playerPos.y > groundLevel - playerHeight)`
        // `(170 -35)`
        // endregion
        // endregion

        // region Key-press handling.
        // if (keyIsPressed(KeyEvent.VK_W))
        // camAddent.add(mult(camFront, playerVel.z));
        // if (keyIsPressed(KeyEvent.VK_A))
        // camAddent.add(normalize(cross(camFront, up)).mult(-playerVel.x));
        // if (keyIsPressed(KeyEvent.VK_S))
        // camAddent.add(mult(camFront, -playerVel.z));
        // if (keyIsPressed(KeyEvent.VK_D))
        // camAddent.add(normalize(cross(camFront, up)).mult(playerVel.x));

        if (SKETCH.keyIsPressed(KeyEvent.VK_W))
            CAMERA.moveZ(playerVel.z);

        if (SKETCH.keyIsPressed(KeyEvent.VK_A))
            CAMERA.moveX(-playerVel.x);

        if (SKETCH.keyIsPressed(KeyEvent.VK_S))
            CAMERA.moveZ(-playerVel.z);

        if (SKETCH.keyIsPressed(KeyEvent.VK_D))
            CAMERA.moveX(playerVel.x);
        // endregion

    }

}
