package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.rendering.lights.NerdAmbiLight;
import com.brahvim.nerd.rendering.particles.Particle;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class TestScene3 extends NerdScene {

    private PShape boxShape;
    private NerdAmbiLight ambiLight;
    private ArrayList<Particle> cubes = new ArrayList<>();

    @Override
    protected synchronized void preload() {
        for (int i = 1; i != 5; i++)
            ASSETS.add(OggBufferDataAsset.getLoader(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        CAMERA.pos.z = 350;
        this.boxShape = SKETCH.createShape(PConstants.BOX, 1);
        this.boxShape.setStrokeWeight(0.28f);
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
        // Background:
        SKETCH.in2d(() -> {
            SKETCH.beginShape(PConstants.QUAD);

            SKETCH.fill(224, 152, 27);
            SKETCH.vertex(-SKETCH.cx, -SKETCH.cy);

            SKETCH.vertex(SKETCH.cx, -SKETCH.cy);

            SKETCH.fill(232, 81, 194);
            SKETCH.vertex(SKETCH.cx, SKETCH.cy);

            SKETCH.vertex(-SKETCH.cx, SKETCH.cy);
            SKETCH.endShape();
        });

        // region Lighting!
        SKETCH.lights();
        this.ambiLight.apply();
        // SKETCH.pointLight(255, 255, 0, 0, 0, 1);

        // Spot light attempt:
        /*
         * SKETCH.spotLight(
         * // Color:
         * 224, 152, 27, // (The orange at the top!)
         * // 255, 255, 0, // A 'pure' Yellow!
         * 
         * // Position:
         * 0, 0, -500, // (We're in the center already! Back off a little..)
         * 
         * // Unit direction vector:
         * 0, 0, 1, // (Towards the viewer!)
         * 
         * // Angle and concentration!:
         * 0, 10_000);
         */
        // endregion

        // ...Does nothing!:
        // CAMERA.pos.x += (SKETCH.mouse.x - SKETCH.pmouse.x) * 0.1f;

        for (Particle c : this.cubes)
            if (c != null)
                c.draw(this.boxShape);
    }

    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(TestScene1.class);

            case PConstants.LEFT -> {
                AlBuffer<?> randomPop = ASSETS.get("Pop" + (int) SKETCH.random(1, 4)).getData();
                this.cubes.add(new Particle(SCENE).plopIn(randomPop));
            }
        }
    }

    @Override
    public void keyPressed() {
        if (SKETCH.onlyKeyPressedIs(KeyEvent.VK_SPACE))
            MANAGER.restartScene();
    }

}
