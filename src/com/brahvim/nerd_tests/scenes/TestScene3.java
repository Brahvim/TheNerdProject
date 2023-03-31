package com.brahvim.nerd_tests.scenes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.brahvim.nerd.math.collision.CollisionAlgorithms;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.rendering.lights.NerdAmbiLight;
import com.brahvim.nerd.rendering.particles.NerdParticle;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class TestScene3 extends NerdScene {

    // region Fields.
    private final int CUBES_PER_CLICK = 5;
    private final float REVOLVE_RADIUS = 45;
    private final int CUBES_ADDED_EVERY_FRAME = 2;

    private PImage bgGrad;
    private int cubesToAdd;
    private PShape boxShape;
    // private FlyCamera CAMERA;
    private float REVOLVE_ANGLE;
    private NerdAmbiLight ambiLight;
    private ArrayList<NerdParticle> cubes = new ArrayList<>();
    // endregion

    @Override
    protected synchronized void preload() {
        for (int i = 1; i != 5; i++)
            ASSETS.add(OggBufferDataAsset.getLoader(), "data/Pops/Pop" + i + ".ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        // CAMERA = new FlyCamera(SKETCH);
        CAMERA.pos.set(0, 0, 350);
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
        // SLOWEST!:
        /*
         * for (int x = 0; x < SKETCH.width; x++) {
         * for (int y = 0; y < SKETCH.height; y++) {
         * SKETCH.set(x, y, this.SKETCH.lerpColor(
         * SKETCH.color(224, 152, 27),
         * SKETCH.color(232, 81, 194),
         * PApplet.map(y, 0, SKETCH.height, 0, 1)));
         * }
         * }
         */

        // Somewhat fast (`bgGrad` is an `int[]`):
        /*
         * if (this.bgGrad == null)
         * this.calculateBgGrad();
         * 
         * SKETCH.loadPixels();
         * System.arraycopy(this.bgGrad, 0, SKETCH.pixels, 0, this.bgGrad.length);
         * SKETCH.updatePixels();
         */

        // Fastest custom background method using geometry - affected by the camera!..:
        /*
         * SKETCH.in2d(() -> {
         * SKETCH.beginShape(PConstants.QUAD);
         * 
         * SKETCH.fill(224, 152, 27);
         * SKETCH.vertex(-SKETCH.cx, -SKETCH.cy);
         * 
         * SKETCH.vertex(SKETCH.cx, -SKETCH.cy);
         * 
         * SKETCH.fill(232, 81, 194);
         * SKETCH.vertex(SKETCH.cx, SKETCH.cy);
         * 
         * SKETCH.vertex(-SKETCH.cx, SKETCH.cy);
         * SKETCH.endShape();
         * });
         */

        // **Fastest!:** (Remember, images are hardware accelerated!):
        if (this.bgGrad == null)
            this.calculateBgGrad();
        SKETCH.background(this.bgGrad);

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

        this.REVOLVE_ANGLE += (super.SKETCH.mouseX - super.SKETCH.pmouseX) * 0.001f;
        CAMERA.pos.set(
                PApplet.sin(this.REVOLVE_ANGLE) * this.REVOLVE_RADIUS, 0,
                PApplet.cos(this.REVOLVE_ANGLE) * this.REVOLVE_RADIUS);

        if (this.cubesToAdd != 0)
            for (int i = 0; i != this.CUBES_ADDED_EVERY_FRAME; i++) {
                if (this.cubesToAdd == 0)
                    break;
                this.cubesToAdd--;
                AlBuffer<?> randomPop = ASSETS.get("Pop" + (int) SKETCH.random(1, 4)).getData();
                this.cubes.add(new NerdParticle(SCENE).plopIn(randomPop));
            }

        for (int i = this.cubes.size() - 1; i != -1; i--) {
            final NerdParticle p = this.cubes.get(i);
            // final float twiceTheSize = p.size * 2;
            // final PVector screenPos = SKETCH.screenVec(p.getPos());

            if (!CollisionAlgorithms.ptRect(
                    // screenPos.x, screenPos.y,
                    p.getPos().x, p.getPos().y,

                    // -twiceTheSize * 2, -twiceTheSize * 2,
                    // SKETCH.width + twiceTheSize * 2,
                    // SKETCH.height + twiceTheSize * 2

                    -5000, -5000, 5000, 5000)) {
                // Don't deallocate manually! Get `ParticleMan` (`ParticleManager`)!
                // ...and add stuff to da particles ._.
                p.getAudioSource().dispose();
                this.cubes.remove(i);
            }
        }

        for (NerdParticle p : this.cubes)
            if (p != null)
                p.draw(this.boxShape);

        // SKETCH.in2d(() -> {
        // SKETCH.circle(SKETCH.mouse.x, SKETCH.mouse.y, 5);
        // });
    }

    @Override
    public void resized() {
        this.calculateBgGrad();
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
            case PConstants.LEFT -> this.cubesToAdd += this.CUBES_PER_CLICK;
        }
    }

    @Override
    public void keyPressed() {
        if (SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
            for (int i = this.cubes.size() - 1; i != -1; i--) {
                final NerdParticle p = this.cubes.get(i);
                p.getAudioSource().dispose();
                p.plopOut();
                this.cubes.remove(i);
            }
    }

}
