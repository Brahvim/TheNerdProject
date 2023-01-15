package com.brahvim.nerd_tests;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PVector;

public class AnimatedCube /* implements NerdScene.AutoDrawableInstance */ {
    // region Fields.
    public float size = 45;
    public PVector pos, vel, acc, rot;
    public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

    private final Sketch SKETCH;
    private final NerdScene SCENE;
    private boolean isVisible = true;
    private SineWave plopWave, fadeWave;
    // private final NerdScene.AutoDrawable CALLBACK_FOR_RENDERER;
    // endregion

    public AnimatedCube(NerdScene p_scene) {
        this.SCENE = p_scene;
        this.SKETCH = this.SCENE.getSketch();

        this.pos = new PVector();
        this.vel = new PVector();
        this.acc = new PVector();
        this.rot = new PVector();

        // final AnimatedCube CUBE = this;
        // this.SCENE.createAutoDrawable(this);
        /*
         * this.CALLBACK_FOR_RENDERER = this.SCENE.new AutoDrawable() {
         *
         * @Override
         * public void draw() {
         * CUBE.draw();
         * }
         * };
         */

        this.plopWave = new SineWave(SKETCH);
        this.fadeWave = new SineWave(SKETCH);

        this.plopWave.inactValue = 1;
        this.fadeWave.inactValue = 1;
    }

    public AnimatedCube(NerdScene p_scene, PVector p_pos) {
        this(p_scene);
        this.pos.set(p_pos.x, p_pos.y, p_pos.z);
    }

    // region Transitions.
    public void cutIn() {
        this.isVisible = true;
    }

    public void cutOut() {
        this.isVisible = false;
    }

    public void plopIn() {
        this.isVisible = true;
        this.plopWave.endWhenAngleIs(90).start();
    }

    public void plopOut() {
        this.plopWave.endWhenAngleIs(180).start(90, () -> {
            this.isVisible = false;
        });
    }

    public void fadeIn() {
        this.isVisible = true;
        this.fadeWave.endWhenAngleIs(90).start();
    }

    public void fadeOut() {
        this.isVisible = false;
        this.fadeWave.endWhenAngleIs(180).start(90);
    }
    // endregion

    public boolean isVisible() {
        return this.isVisible;
    }

    public void draw() {
        if (!this.isVisible)
            return;

        SKETCH.pushMatrix();
        SKETCH.pushStyle();

        SKETCH.translate(this.pos);
        SKETCH.rotate(this.rot);

        SKETCH.stroke(this.strokeColor, 255 * this.fadeWave.get());
        SKETCH.fill(this.fillColor, 255 * this.fadeWave.get());
        SKETCH.box(this.size * this.plopWave.get());

        SKETCH.popStyle();
        SKETCH.popMatrix();
    }

}
