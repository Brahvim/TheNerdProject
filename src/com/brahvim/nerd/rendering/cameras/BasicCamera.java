package com.brahvim.nerd.rendering.cameras;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

/**
 * @apiNote Please use a {@link BasicCameraBuilder} to get instances of
 *          {@link BasicCamera}.
 */
public class BasicCamera extends NerdAbstractCamera {

    public PVector center, defaultCamCenter;

    protected BasicCamera(Sketch p_sketch) {
        super(p_sketch);
        this.center = new PVector(); // super.SKETCH.cx, super.SKETCH.cy);
    }

    // public void useProcessingDefaults() {
    // float WIDTH_HALF = this.SKETCH.cx, HEIGHT_HALF = this.SKETCH.cy;
    // this.defaultCamUp = new PVector(0, 1, 0);
    // this.defaultCamPos = new PVector(
    // WIDTH_HALF, HEIGHT_HALF,
    // HEIGHT_HALF / (float) Math.tan(PConstants.PI * 30 / 180));
    // this.defaultCamCenter = new PVector(WIDTH_HALF, HEIGHT_HALF);
    // }

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

    @Override
    public BasicCamera clone() {
        BasicCamera toRet = new BasicCamera(this.SKETCH);

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
