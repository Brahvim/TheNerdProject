package com.brahvim.nerd_tests;

import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Bubble extends SimpleEulerBody
/* implements NerdScene.AutoDrawableInstance */ {

    // region Fields.
    public float size = 40;
    public PVector pos, vel, acc, rot;
    public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

    private final Sketch SKETCH;
    private final NerdScene SCENE;
    // private final NerdScene.AutoDrawable CALLBACK_FOR_RENDERER;

    @SuppressWarnings("unused")
    private boolean visible = true, pvisible = true;
    private SineEase plopWave, fadeWave;
    private AlSource popSrc;
    // endregion

    public Bubble(NerdScene p_scene) {
        this.SCENE = p_scene;
        this.SKETCH = this.SCENE.SKETCH;

        super.pos.set(
                SKETCH.random(-SKETCH.qx, SKETCH.qx),
                SKETCH.random(-SKETCH.qy, SKETCH.qy),
                SKETCH.random(-250, 100));

        super.acc.set(
                SKETCH.random(-0.01f, 0.01f),
                SKETCH.random(-0.01f, 0.01f));

        super.rot.set(
                SKETCH.random(PConstants.TAU),
                SKETCH.random(PConstants.TAU),
                SKETCH.random(PConstants.TAU));

        super.rotAcc.set(
                SKETCH.random(-0.0001f, 0.0001f),
                SKETCH.random(-0.0001f, 0.0001f),
                SKETCH.random(-0.0001f, 0.0001f));

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

        this.plopWave = new SineEase(SKETCH);
        this.fadeWave = new SineEase(SKETCH);

        this.plopWave.inactValue = 1;
        this.fadeWave.inactValue = 1;
    }

    // region Transitions.
    public Bubble cutIn() {
        this.visible = true;
        return this;
    }

    public Bubble cutOut() {
        this.visible = false;
        return this;
    }

    public Bubble plopIn() {
        this.plopInImpl();
        return this;
    }

    public Bubble plopIn(AlBuffer<?> p_popAudioBuffer) {
        this.popSrc = new AlSource(SKETCH.AL, p_popAudioBuffer);
        this.popSrc.play();
        this.plopInImpl();
        return this;
    }

    private Bubble plopInImpl() {
        this.visible = true;
        this.plopWave.freqMult = 0.015f;
        this.plopWave.endWhenAngleIncrementsBy(90).start();
        return this;
    }

    public Bubble plopOut() {
        this.plopWave.endWhenAngleIncrementsBy(180).start(90, () -> {
            this.visible = false;
        });
        return this;
    }

    public Bubble fadeIn() {
        this.visible = true;
        this.fadeWave.endWhenAngleIncrementsBy(90).start();
        return this;
    }

    public Bubble fadeOut() {
        this.visible = false;
        this.fadeWave.endWhenAngleIncrementsBy(180).start(90);
        return this;
    }
    // endregion

    public boolean isVisible() {
        return this.visible;
    }

    public void draw(PShape p_shape) {
        if (!this.visible)
            return;

        if (this.plopWave.active)
            if (this.popSrc.isStopped())
                this.popSrc.dispose();

        super.rotVel.add(0, 0,
                (SKETCH.mouse.x - SKETCH.pmouse.x) * 0.001f);
        super.integrate();

        SKETCH.pushMatrix();
        SKETCH.pushStyle();

        SKETCH.translate(super.pos);
        SKETCH.rotate(super.rot);

        // SKETCH.stroke(this.strokeColor, 255 * this.fadeWave.get());
        // SKETCH.fill(this.fillColor, 255 * this.fadeWave.get());
        SKETCH.fill(255);
        SKETCH.scale(this.size * this.plopWave.get());
        SKETCH.shape(p_shape);

        SKETCH.popStyle();
        SKETCH.popMatrix();

        this.pvisible = this.visible;
    }

}
