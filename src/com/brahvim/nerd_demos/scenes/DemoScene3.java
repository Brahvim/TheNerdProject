package com.brahvim.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.rendering.cameras.FlyCamera;
import com.brahvim.nerd.rendering.lights.NerdAmbiLight;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_demos.App;
import com.brahvim.nerd_demos.CubeManager;
import com.brahvim.nerd_demos.scenes.scene1.DemoScene1;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class DemoScene3 extends NerdScene {

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
            ASSETS.add(new OggBufferDataAsset(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(final SceneState p_state) {
        MANAGER.settings.drawFirstCaller = SceneManager.SceneManagerSettings.CallbackOrder.SCENE;
        // SCENE.addLayers(CinematicBars.class);

        this.calculateBgGrad();
        CAMERA = new FlyCamera(SKETCH);
        CAMERA.fov = PApplet.radians(75);
        App.OPENAL.unitSize = Float.MAX_VALUE;
        SKETCH.setCamera(CAMERA);

        final AlBuffer<?>[] alBuffers = new AlBuffer<?>[4];
        for (int i = 1; i != 5; i++)
            alBuffers[i - 1] = ASSETS.get("Pop" + i).getData();

        this.cubeMan = new CubeManager(this, alBuffers);
        this.ambiLight = new NerdAmbiLight(
                SKETCH,
                new PVector(0, 0, 0),
                // new PVector(255, 255, 0) // Yellow
                // new PVector(224, 152, 27) // The orange at the top.
                // new PVector(228, 117, 111) // The color in the middle
                new PVector(232, 81, 194) // The pink at the bottom.
        );
    }

    @Override
    protected void draw() {
        // Faster in `draw()`:
        if (SKETCH.keysPressed(KeyEvent.VK_CONTROL, KeyEvent.VK_R)) {
            this.cubeMan.removeAll(); // REALLY helps the GC out!
            System.gc(); // Surprisingly, this is a useful hint to the GC.
            MANAGER.restartScene();
        }

        SKETCH.lights();
        SKETCH.background(this.bgGrad);
        this.ambiLight.apply();
        this.controlCamera();
        this.cubeMan.draw();
    }

    private void calculateBgGrad() {
        final int color1 = SKETCH.color(224, 152, 27), color2 = SKETCH.color(232, 81, 194);
        this.bgGrad = SKETCH.createImage(SKETCH.width, SKETCH.height, PConstants.RGB);

        for (int y = 0; y < this.bgGrad.height; y++)
            for (int x = 0; x < this.bgGrad.width; x++)
                this.bgGrad.pixels[x + y * this.bgGrad.width] = SKETCH.lerpColor(
                        color1, color2, PApplet.map(y, 0, this.bgGrad.height, 0, 1));
    }

    private void controlCamera() {
        // Increase speed when holding `Ctrl`:
        /* final */ float velMultiplier = 1;

        if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            velMultiplier = 2;

        // region Roll.
        if (SKETCH.keyIsPressed(KeyEvent.VK_Z))
            CAMERA.up.x += velMultiplier * 0.01f;

        if (SKETCH.keyIsPressed(KeyEvent.VK_C))
            CAMERA.up.x += -velMultiplier * 0.01f;
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
    }

    // region Input event callbacks.
    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(DemoScene1.class);
            case PConstants.CENTER -> this.cubeMan.removeAll();
            case PConstants.LEFT -> this.cubeMan.emitCubes(this.cubeMan.CUBES_PER_CLICK);
        }
    }

    @Override
    public void keyPressed() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_F)) {
            SKETCH.cursorVisible = !SKETCH.cursorVisible;
            CAMERA.holdMouse = !CAMERA.holdMouse;
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        CAMERA.fov -= p_mouseEvent.getCount() * 0.1f;
        CAMERA.fov = PApplet.constrain(CAMERA.fov, 0, 130);
    }
    // endregion

}
