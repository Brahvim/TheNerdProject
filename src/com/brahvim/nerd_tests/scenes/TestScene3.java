package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.rendering.cameras.FlyCamera;
import com.brahvim.nerd.rendering.lights.NerdAmbiLight;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.App;
import com.brahvim.nerd_tests.CubeManager;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class TestScene3 extends NerdScene {

    // region Fields.
    private PImage bgGrad;
    private FlyCamera CAMERA;
    private CubeManager cubeMan;
    private NerdAmbiLight ambiLight;

    private final float GRAVITY = 2;
    private final PVector playerVel = new PVector(10, 7, 10);
    // endregion

    @Override
    protected synchronized void preload() {
        for (int i = 1; i != 5; i++)
            this.ASSETS.add(OggBufferDataAsset.getLoader(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(final SceneState p_state) {
        this.calculateBgGrad();
        this.CAMERA = new FlyCamera(this.SKETCH);
        this.CAMERA.fov = PApplet.radians(75);
        App.AL.unitSize = 250.0f;
        this.SKETCH.setCamera(this.CAMERA);

        final AlBuffer<?>[] alBuffers = new AlBuffer<?>[4];
        for (int i = 1; i != 5; i++)
            alBuffers[i - 1] = this.ASSETS.get("Pop" + i).getData();

        this.cubeMan = new CubeManager(this, alBuffers);
        this.ambiLight = new NerdAmbiLight(
                this.SKETCH,
                new PVector(0, 0, 0),
                // new PVector(255, 255, 0) // Yellow
                // new PVector(224, 152, 27) // The orange at the top.
                // new PVector(228, 117, 111) // The color in the middle
                new PVector(232, 81, 194) // The pink at the bottom.
        );
    }

    @Override
    protected void draw() {
        if (this.SKETCH.keysPressed(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
            this.MANAGER.restartScene(this.STATE);

        this.SKETCH.lights();
        this.SKETCH.background(this.bgGrad);
        this.ambiLight.apply();
        this.controlCamera();
        this.cubeMan.draw();
    }

    private void calculateBgGrad() {
        final int color1 = this.SKETCH.color(224, 152, 27), color2 = this.SKETCH.color(232, 81, 194);
        this.bgGrad = this.SKETCH.createImage(this.SKETCH.width, this.SKETCH.height, PConstants.RGB);

        for (int y = 0; y < this.bgGrad.height; y++)
            for (int x = 0; x < this.bgGrad.width; x++)
                this.bgGrad.pixels[x + y * this.bgGrad.width] = this.SKETCH.lerpColor(
                        color1, color2, PApplet.map(y, 0, this.bgGrad.height, 0, 1));
    }

    // region Input event callbacks.
    @Override
    public void mouseClicked() {
        switch (this.SKETCH.mouseButton) {
            case PConstants.RIGHT -> this.MANAGER.startScene(TestScene1.class);
            case PConstants.CENTER -> this.cubeMan.removeAll();
            case PConstants.LEFT -> this.cubeMan.emitCubes(this.cubeMan.CUBES_PER_CLICK);
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        this.CAMERA.fov -= p_mouseEvent.getCount() * 0.1f;
        this.CAMERA.fov = PApplet.constrain(this.CAMERA.fov, 0, 130);
    }
    // endregion

    private void controlCamera() {
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
    }

}
