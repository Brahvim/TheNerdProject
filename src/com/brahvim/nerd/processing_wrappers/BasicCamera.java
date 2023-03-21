package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PConstants;
import processing.core.PVector;

/**
 * @apiNote Please use a {@link BasicCameraBuilder} to get instances of
 *          {@link BasicCamera}.
 */
public class BasicCamera extends NerdCamera {

    // region Fields.
    public PVector center;
    public PVector defaultCamUp, defaultCamPos, defaultCamCenter;
    // endregion

    protected BasicCamera(Sketch p_sketch) {
        super(p_sketch);
        this.center = new PVector(super.SKETCH.cx, super.SKETCH.cy, 0);
    }

    public void useProcessingDefaults() {
        // Default camera values in Processing.
        // From [https://processing.org/reference/camera_.html].
        final float WIDTH_HALF = this.SKETCH.cx, HEIGHT_HALF = this.SKETCH.cy;
        this.defaultCamUp = new PVector(0, 1, 0);
        this.defaultCamPos = new PVector(
                WIDTH_HALF, HEIGHT_HALF,
                HEIGHT_HALF / (float) Math.tan(PConstants.PI * 30 / 180));
        this.defaultCamCenter = new PVector(WIDTH_HALF, HEIGHT_HALF);
    }

    // region Camera runtime.
    @Override
    public void applyMatrix() {
        switch (this.SKETCH.RENDERER) {
            case PConstants.JAVA2D:
                this.apply2dMatrix();
                return;
            // case PConstants.P3D, PConstants.P2D -> this.applyMatrix();
        }

        // Apply projection:
        switch (this.projection) {
            case PConstants.PERSPECTIVE:
                this.SKETCH.perspective(this.fov,
                        (float) this.SKETCH.width / (float) this.SKETCH.height,
                        this.near, this.far);
                break;
            case PConstants.ORTHOGRAPHIC:
                this.SKETCH.ortho(
                        -this.SKETCH.cx, this.SKETCH.cx,
                        -this.SKETCH.cy, this.SKETCH.cy,
                        this.near, this.far);
        }

        // region Apply the camera matrix:
        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.x, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);
        // endregion

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
        // ...nope! I'll remove this! It causes the camera position to seem to change
        // when you resize the window!
        // Lesson learnt: **use this only if your camera never moves!**
    }

    @Deprecated
    public void apply2dMatrix() {
        this.SKETCH.translate(this.center);
        this.SKETCH.translate(0, PVector.dot(this.center, this.up), 0); // :woozy_face:
    }
    // endregion

    // region Copy and reset!
    @Override
    public void resetParams() {
        super.resetParams();

        if (this.defaultCamUp == null)
            this.up.set(0, 0, 0);
        else
            this.up.set(this.defaultCamUp);

        if (this.defaultCamPos == null)
            this.pos.set(0, 0, 0);
        else
            this.pos.set(this.defaultCamPos);

        if (this.defaultCamCenter == null)
            this.center.set(0, 0, 0);
        else
            this.center.set(this.defaultCamCenter);
    }

    public BasicCamera clone() {
        BasicCamera toRet = new BasicCamera(this.SKETCH);

        // region Copying default vectors.
        toRet.defaultCamUp = new PVector(
                this.defaultCamUp.x,
                this.defaultCamUp.y,
                this.defaultCamUp.z);

        toRet.defaultCamPos = new PVector(
                this.defaultCamPos.x,
                this.defaultCamPos.y,
                this.defaultCamPos.z);

        toRet.defaultCamCenter = new PVector(
                this.defaultCamCenter.x,
                this.defaultCamCenter.y,
                this.defaultCamCenter.z);
        // endregion

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
    // endregion

}
