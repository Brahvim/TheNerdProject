package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.rendering.cameras.FlyCamera;
import com.brahvim.nerd.rendering.lights.NerdAmbiLight;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.AnimatedCube;
import com.brahvim.nerd_tests.CubeManager;
import com.brahvim.nerd_tests.CubeManager.Transition;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class TestScene3 extends NerdScene {

    // region Fields.
    private final int CUBES_PER_CLICK = 15;
    private final int CUBES_ADDED_EVERY_FRAME = 2;

    private PImage bgGrad;
    private FlyCamera CAMERA;
    private CubeManager cubeMan;
    private NerdAmbiLight ambiLight;
    // endregion

    @Override
    protected synchronized void preload() {
        for (int i = 1; i != 5; i++)
            ASSETS.add(OggBufferDataAsset.getLoader(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        FlyCamera.holdMouse = true;
        SKETCH.cursorVisible = false;
        CAMERA = new FlyCamera(SKETCH);
        SKETCH.setCamera(CAMERA);
        this.calculateBgGrad();

        this.cubeMan = new CubeManager(this);
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
        SKETCH.background(this.bgGrad);
        SKETCH.lights();
        this.ambiLight.apply();
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

    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(TestScene1.class);
            case PConstants.LEFT -> this.emitCubes(this.cubeMan.CUBES_PER_CLICK);
        }
    }

    @Override
    public void keyPressed() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
            this.cubeMan.removeAll();

        // for (int i = this.cubes.size() - 1; i != -1; i--) {
        // final AnimatedCube p = this.cubes.get(i);
        // p.getAudioSource().dispose();
        // p.plopOut();
        // this.cubes.remove(i);
        // }
    }

}
