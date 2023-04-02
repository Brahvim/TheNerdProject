package com.brahvim.nerd_tests;

import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class AnimatedCube extends TestEulerBody {

    // region Fields.
    public float size = 40;
    public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

    // private final NerdScene.AutoDrawable CALLBACK_FOR_RENDERER;

    private boolean visible = true;
    private SineEase plopWave;
    private AlSource popSrc;
    // endregion

    public AnimatedCube(NerdScene p_scene) {
        super(p_scene.SKETCH);

        super.pos.set(
                super.SKETCH.random(-super.SKETCH.qx, super.SKETCH.qx),
                super.SKETCH.random(-super.SKETCH.qy, super.SKETCH.qy),
                super.SKETCH.random(-300, 300));

        super.acc.set(
                super.SKETCH.random(-0.01f, 0.01f),
                super.SKETCH.random(-0.01f, 0.01f),
                super.SKETCH.random(-0.01f, 0.01f));

        super.rot.set(
                super.SKETCH.random(PConstants.TAU),
                super.SKETCH.random(PConstants.TAU),
                super.SKETCH.random(PConstants.TAU));

        super.rotAcc.set(
                super.SKETCH.random(-0.0001f, 0.0001f),
                super.SKETCH.random(-0.0001f, 0.0001f),
                super.SKETCH.random(-0.0001f, 0.0001f));

        this.plopWave = new SineEase(super.SKETCH);
        this.plopWave.inactValue = 1;
    }

    // region Getters and setters for superclass stuff!:
    public float getFrict() {
        return super.frict;
    }

    public void setFrict(float p_frict) {
        super.frict = p_frict;
    }

    public float getRotFrict() {
        return super.rotFrict;
    }

    public void setRotFrict(float p_rotFrict) {
        super.rotFrict = p_rotFrict;
    }

    public PVector getPos() {
        return super.pos;
    }

    public PVector getVel() {
        return super.vel;
    }

    public PVector getAcc() {
        return super.acc;
    }

    public PVector getRot() {
        return super.rot;
    }

    public PVector getRotVel() {
        return super.rotVel;
    }

    public PVector getRotAcc() {
        return super.rotAcc;
    }
    // endregion

    public AnimatedCube plopIn(final AlBuffer<?> p_popAudioBuffer) {
        if (p_popAudioBuffer != null)
            this.popSrc = new AlSource(super.SKETCH.AL, p_popAudioBuffer);

        this.popSrc.setPosition(PVector.mult(super.pos, 0.001f));
        this.visible = true;
        this.popSrc.play();

        this.plopWave.freqMult = 0.015f;
        this.plopWave.endWhenAngleIncrementsBy(90).start();
        return this;
    }

    public AnimatedCube plopOut() {
        this.plopWave.endWhenAngleIncrementsBy(90).start(270, () -> {
            this.visible = false;
        });
        return this;
    }

    public AlSource getAudioSource() {
        return this.popSrc;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void draw(PShape p_shape) {
        if (!this.visible)
            return;

        if (this.plopWave.active)
            if (this.popSrc != null)
                if (this.popSrc.isStopped())
                    this.popSrc.dispose();

        super.integrate();
        super.SKETCH.push();
        super.SKETCH.fill(255);

        super.SKETCH.translate(super.pos);
        super.SKETCH.rotate(super.rot);
        super.SKETCH.scale(this.size * this.plopWave.get());
        super.SKETCH.shape(p_shape);
        super.SKETCH.pop();
    }

}
