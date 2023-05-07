package com.brahvim.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.lights.NerdAmbiLight;
import com.brahvim.nerd.framework.scenes.NerdScene;
import com.brahvim.nerd.framework.scenes.NerdSceneManager;
import com.brahvim.nerd.framework.scenes.NerdSceneState;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
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
    private SmoothCamera CAMERA;
    private CubeManager cubeMan;
    private NerdAmbiLight ambiLight;
    // endregion

    @Override
    protected synchronized void preload() {
        for (int i = 1; i != 5; i++)
            ASSETS.add(new OggBufferDataAsset(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(final NerdSceneState p_state) {
        MANAGER.SETTINGS.drawFirstCaller = NerdSceneManager.SceneManagerSettings.CallbackOrder.SCENE;
        // SCENE.addLayers(CinematicBars.class);
        this.calculateBgGrad();

        CAMERA = new SmoothCamera(SKETCH);
        CAMERA.fov = PApplet.radians(75);
        SKETCH.setCamera(this.CAMERA);

        final AlBuffer<?>[] alBuffers = new AlBuffer<?>[4];
        App.OPENAL.unitSize = Float.MAX_VALUE;
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
        this.cubeMan.draw();
    }

    // region My methods :)
    private void calculateBgGrad() {
        final int color1 = SKETCH.color(224, 152, 27), color2 = SKETCH.color(232, 81, 194);
        this.bgGrad = SKETCH.createImage(SKETCH.width, SKETCH.height, PConstants.RGB);

        for (int y = 0; y < this.bgGrad.height; y++)
            for (int x = 0; x < this.bgGrad.width; x++)
                this.bgGrad.pixels[x + y * this.bgGrad.width] = SKETCH.lerpColor(
                        color1, color2, PApplet.map(y, 0, this.bgGrad.height, 0, 1));
    }
    // endregion

    // region Event callbacks.
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
            WINDOW.cursorVisible = !WINDOW.cursorVisible;
            CAMERA.holdMouse = !CAMERA.holdMouse;
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        CAMERA.fov -= p_mouseEvent.getCount() * 0.1f;
        CAMERA.fov = PApplet.constrain(CAMERA.fov, 0, 130);
    }

    @Override
    public void fullscreenChanged(final boolean p_state) {
        System.out.printf("`DemoScene3::fullscreenChanged()`: `%s`.\n", p_state);
    }
    // endregion

}
