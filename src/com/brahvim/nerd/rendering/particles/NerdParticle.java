package com.brahvim.nerd.rendering.particles;

import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.rendering.overtly_simple_physics.NerdEulerBody;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class NerdParticle extends NerdEulerBody {

    // region Fields.
    public float size = 40;
    public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

    // private final NerdScene.AutoDrawable CALLBACK_FOR_RENDERER;

    @SuppressWarnings("unused")
    private boolean visible = true, pvisible = true;
    private SineEase plopWave, fadeWave;
    private AlSource popSrc;
    // endregion

    public NerdParticle(NerdScene p_scene) {
        super(p_scene.SKETCH);

        super.pos.set(
                super.SKETCH.random(-super.SKETCH.qx, super.SKETCH.qx),
                super.SKETCH.random(-super.SKETCH.qy, super.SKETCH.qy),
                super.SKETCH.random(-250, 100));

        super.acc.set(
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

        this.plopWave = new SineEase(super.SKETCH);
        this.fadeWave = new SineEase(super.SKETCH);

        this.plopWave.inactValue = 1;
        this.fadeWave.inactValue = 1;
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

    public float getDtMult() {
        return super.dtMult;
    }

    public void setDtMult(float p_dtMult) {
        super.dtMult = p_dtMult;
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

    // region Transitions.
    public NerdParticle cutIn() {
        this.visible = true;
        return this;
    }

    public NerdParticle cutOut() {
        this.visible = false;
        return this;
    }

    public NerdParticle plopIn() {
        this.plopInImpl();
        return this;
    }

    public NerdParticle plopIn(AlBuffer<?> p_popAudioBuffer) {
        this.popSrc = new AlSource(super.SKETCH.AL, p_popAudioBuffer);
        this.popSrc.setPosition(PVector.mult(super.pos, 0.001f));
        this.popSrc.play();
        this.plopInImpl();
        return this;
    }

    private NerdParticle plopInImpl() {
        this.visible = true;
        this.plopWave.freqMult = 0.015f;
        this.plopWave.endWhenAngleIncrementsBy(90).start();
        return this;
    }

    public NerdParticle plopOut() {
        this.plopWave.endWhenAngleIncrementsBy(90).start(270, () -> {
            this.visible = false;
        });
        return this;
    }

    public NerdParticle fadeIn() {
        this.visible = true;
        this.fadeWave.endWhenAngleIncrementsBy(90).start();
        return this;
    }

    public NerdParticle fadeOut() {
        this.visible = false;
        this.fadeWave.endWhenAngleIncrementsBy(180).start(90);
        return this;
    }
    // endregion

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
            if (this.popSrc.isStopped())
                this.popSrc.dispose();

        // super.rot.add(0, 0,
        // (super.SKETCH.mouseX - super.SKETCH.pmouseX) * 0.001f);
        super.integrate();

        super.SKETCH.push();
        super.SKETCH.translate(super.pos);
        super.SKETCH.rotate(super.rot);
        super.SKETCH.fill(255);
        // super.SKETCH.stroke(this.strokeColor, 255 * this.fadeWave.get());
        // super.SKETCH.fill(this.fillColor, 255 * this.fadeWave.get());
        super.SKETCH.scale(this.size * this.plopWave.get());
        super.SKETCH.shape(p_shape);
        super.SKETCH.pop();

        this.pvisible = this.visible;
    }

}
