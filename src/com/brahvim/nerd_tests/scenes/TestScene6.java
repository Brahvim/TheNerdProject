package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.processing_wrappers.FlyCamera;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PVector;

public class TestScene6 extends NerdScene {
    private FlyCamera CAMERA;
    private PVector playerPos, playerVel = new PVector(3, 2, 3);

    @Override
    protected void setup(SceneState p_state) {
        CAMERA = new FlyCamera(SKETCH);
        this.playerPos = CAMERA.getNerdCamera().pos; // Hah! Pointer trick...

        // Do not forget to call!
        // The camera won't be "auto-used" otherwise!!!
        SKETCH.setCamera(CAMERA);
    }

    @Override
    protected void draw() {
        SKETCH.background(0x006699);

        this.controlCamera();

        // Box in center:
        SKETCH.pushMatrix();
        SKETCH.translate(0, 165, 500);
        SKETCH.box(50);
        SKETCH.popMatrix();

        // Ground:
        SKETCH.translate(0, 200);
        SKETCH.box(2000, 20, 2000);
    }

    private void controlCamera() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            CAMERA.camAddent.mult(4);

        // region Gravity:
        this.playerPos.y += 0.98f;

        // region Don't-go-underneath check:
        if (this.playerPos.y > 135)
            // `if (this.playerPos.y > groundLevel - playerHeight)`
            // `(170 -35)`
            this.playerPos.y = 170;
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
