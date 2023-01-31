package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PConstants;
import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdCameraBuilder} to get instances of
 *          {@link BasicCamera}.
 */
public class BasicCamera extends NerdCamera {
    public PVector center, defaultCamUp, defaultCamPos, defaultCamCenter;

    public BasicCamera(Sketch p_sketch) {
        super(p_sketch);

        super.up = new PVector(0, 1, 0);
        super.pos = new PVector(0, 0, 0);
        this.center = new PVector(0, 0, 0);
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
    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        switch (this.SKETCH.RENDERER) {
            case PConstants.JAVA2D -> this.apply2dMatrix();
            case PConstants.P3D, PConstants.P2D -> this.applyMatrix();
        }
    }

    @Override
    public void clear() {
        this.SKETCH.begin2d();
        // Removing this will not display the previous camera's view,
        // but still show clipping:
        this.SKETCH.camera();
        this.SKETCH.noStroke();
        this.SKETCH.fill(this.clearColor);
        this.SKETCH.rectMode(PConstants.CORNER);
        this.SKETCH.rect(0, 0, this.SKETCH.width, this.SKETCH.height);
        this.SKETCH.end2d();
    }

    @Override
    public void applyMatrix() {
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
                break;
        }

        // Apply the camera matrix:
        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.z, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
    }

    public void apply2dMatrix() {
        this.SKETCH.translate(this.center);
        this.SKETCH.translate(0, PVector.dot(this.center, this.up), 0); // :woozy_face:
    }
    // endregion

    // region Copy and reset!
    @Override
    public void completeReset() {
        this.resetCamParams();
        this.resetSettings();
    }

    @Override
    public void resetCamParams() {
        super.resetCamParams();

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

    @Override
    public BasicCamera clone() {
        BasicCamera toRet = new BasicCamera(this.SKETCH);

        toRet.up.set(this.up);
        toRet.pos.set(this.pos);
        toRet.center.set(this.center);

        toRet.far = this.far;
        toRet.fov = this.fov;
        toRet.near = this.near;

        toRet.script = this.script;
        return toRet;
    }
    // endregion

}
