package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.brahvim.nerd.math.collision.CollisionAlgorithms;
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

    // region Fields.
    private final int CUBES_PER_CLICK = 5;
    private final int CUBES_ADDED_EVERY_FRAME = 2;

    private int cubesToAdd;
    private PShape boxShape;
    private NerdAmbiLight ambiLight;
    private ArrayList<Particle> cubes = new ArrayList<>();
    // endregion Fields.

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

        // for (int i = 0; i != this.CUBES_ADDED_EVERY_FRAME; i++)
        // if (this.cubesToAdd < 0)
        // break;
        // else
        // this.cubesToAdd--;

        if (this.cubesToAdd != 0) // Optimization
            for (int i = 0; i != this.CUBES_ADDED_EVERY_FRAME; i++) {
                if (this.cubesToAdd == 0)
                    break;
                this.cubesToAdd--;
                AlBuffer<?> randomPop = ASSETS.get("Pop" + (int) SKETCH.random(1, 4)).getData();
                this.cubes.add(new Particle(SCENE).plopIn(randomPop));
            }

        for (int i = this.cubes.size() - 1; i != -1; i--) {
            final Particle p = this.cubes.get(i);
            final PVector screenPos = SKETCH.screenVec(p.getPos());
            final float twiceTheSize = p.size * 2;

            if (!CollisionAlgorithms.ptRect(
                    screenPos.x, screenPos.y,
                    -twiceTheSize * 2, -twiceTheSize * 2,

                    SKETCH.width + twiceTheSize * 2,
                    SKETCH.height + twiceTheSize * 2)) {
                p.getAudioSource().dispose();
                this.cubes.remove(i);
            }
        }

        for (Particle p : this.cubes)
            if (p != null)
                p.draw(this.boxShape);

        SKETCH.in2d(() -> {
            SKETCH.circle(SKETCH.mouse.x, SKETCH.mouse.y, 5);
        });
    }

    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(TestScene1.class);
            case PConstants.LEFT -> this.cubesToAdd += this.CUBES_PER_CLICK;
        }
    }

    @Override
    public void keyPressed() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
            for (int i = this.cubes.size() - 1; i != -1; i--) {
                final Particle p = this.cubes.get(i);
                p.getAudioSource().dispose();
                p.plopOut();
                this.cubes.remove(i);
            }
    }

}
