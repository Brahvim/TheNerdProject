package com.brahvim.nerd.rendering.cameras;

import java.util.Objects;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdBasicCameraBuilder} to get instances of
 *          {@link NerdBasicCamera}.
 */
public class NerdBasicCamera extends NerdAbstractCamera {

    protected PVector center, defaultCamCenter;

    protected NerdBasicCamera(final NerdSketch p_sketch) {
        super(p_sketch);
        this.center = new PVector(); // super.SKETCH.cx, super.SKETCH.cy);
    }

    @Override
    public void completeReset() {
        super.completeReset();

        if (this.defaultCamCenter == null)
            this.center.set(0, 0, 0);
        else
            this.center.set(this.defaultCamCenter);
    }

    @Override
    public void applyMatrix() {
        super.applyProjection();

        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.x, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
        // ...nope! I'll remove this! It causes the camera position to seem to change
        // when you resize the window!
        // Lesson learnt: **use this only if your camera never moves!**
    }

    public PVector getCenter() {
        return this.center;
    }

    public PVector getDefaultCamCenter() {
        return this.defaultCamCenter;
    }

    public void setCenter(final PVector p_vec) {
        this.center = p_vec;
    }

    public void setDefaultCamCenter(final PVector p_defaultCamCenter) {
        this.defaultCamCenter = Objects.requireNonNull(p_defaultCamCenter);
    }

    @Override
    public NerdBasicCamera clone() {
        final NerdBasicCamera toRet = new NerdBasicCamera(this.SKETCH);

        // region Copying camera parameters.
        toRet.up.set(this.up);
        toRet.pos.set(this.pos);
        toRet.center.set(this.center);

        toRet.far = this.far;
        toRet.fov = this.fov;
        toRet.near = this.near;

        toRet.script = this.script;
        // endregion

        return toRet;
    }

}
