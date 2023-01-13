package com.brahvim.nerd_test;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.processing_wrapper.Sketch;

import processing.core.PVector;

// TODO: `NerdSceneAutoDrawable`! Each scene holds a reference of these.
//interface NerdSceneAutoDrawable { void draw(); } 

public class AnimatedCube /* implements NerdSceneAutoDrawable */ {
    public float size = 45;
    public PVector pos, vel, acc, rot;
    public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

    private final Sketch SKETCH;
    private boolean isVisible = true;
    private SineWave plopWave, fadeWave;

    public AnimatedCube(Sketch p_sketch) {
        this.SKETCH = p_sketch;
        this.pos = new PVector();
        this.vel = new PVector();
        this.acc = new PVector();
        this.rot = new PVector();

        this.plopWave = new SineWave(SKETCH);
        this.fadeWave = new SineWave(SKETCH);

        this.plopWave.inactValue = 1;
        this.fadeWave.inactValue = 1;
    }

    public AnimatedCube(Sketch p_sketch, PVector p_pos) {
        this(p_sketch);
        this.pos.set(p_pos.x, p_pos.y, p_pos.z);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

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
