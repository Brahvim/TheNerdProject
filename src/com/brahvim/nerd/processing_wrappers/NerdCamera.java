package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdCameraBuilder} to get instances of
 *          {@link NerdCamera}.
 */
public class NerdCamera {
    // region Interface `Camera.Script`.
    // Needed to be able to clone cameras!
    @FunctionalInterface
    public interface Script {
        public void onCamUpdate(NerdCamera p_cam);
    }
    // endregion

    // region Fields.
    public final static float DEFAULT_CAM_FOV = PApplet.radians(60),
            DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10000, DEFAULT_CAM_MOUSE_Z = 25;
    public final Sketch SKETCH;

    public NerdCamera.Script script;

    public PVector pos, center, up;
    public PVector defaultCamUp, defaultCamPos, defaultCamCenter;
    public float fov = NerdCamera.DEFAULT_CAM_FOV,
            far = NerdCamera.DEFAULT_CAM_FAR,
            near = NerdCamera.DEFAULT_CAM_NEAR,
            mouseZ = NerdCamera.DEFAULT_CAM_MOUSE_Z;

    public int clearColor = 0, projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;
    // endregion

    public NerdCamera(Sketch p_sketch) {
        this.SKETCH = p_sketch;

        this.up = new PVector(0, 1, 0);
        this.pos = new PVector(0, 0, 0);
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
    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        switch (this.SKETCH.RENDERER) {
            case PConstants.JAVA2D -> this.apply2dMatrix();
            case PConstants.P3D, PConstants.P2D -> this.applyMatrix();
        }
    }

    public void clear() {
        // Removing this will not display the previous camera's view,
        // but still show clipping:

        this.SKETCH.camera();
        this.SKETCH.background(0x006699);

        // this.SKETCH.begin2d();
        // this.SKETCH.camera();
        // this.SKETCH.noStroke();
        // this.SKETCH.fill(this.clearColor);
        // this.SKETCH.rectMode(PConstants.CORNER);
        // this.SKETCH.rect(0, 0, this.SKETCH.width, this.SKETCH.height);
        // this.SKETCH.end2d();
    }

    public void runScript() {
        if (this.script != null && this.doScript)
            this.script.onCamUpdate(this);
    }

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
        }

        // Apply the camera matrix:
        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.z, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // this.SKETCH.translate(Sketch.FLOAT_HALF, Sketch.FLOAT_HALF);
        this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
    }

    public void apply2dMatrix() {
        this.SKETCH.translate(this.center);
        this.SKETCH.translate(0, PVector.dot(this.center, this.up), 0); // :woozy_face:
    }
    // endregion

    // region Copy and reset!
    public void completeReset() {
        this.resetCamParams();
        this.resetSettings();
    }

    public void resetCamParams() {
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

        this.far = NerdCamera.DEFAULT_CAM_FAR;
        this.fov = NerdCamera.DEFAULT_CAM_FOV;
        this.near = NerdCamera.DEFAULT_CAM_NEAR;
    }

    // Does not call `NerdCamera::resetCamParams()` so `FpsCamera` can work.
    public void resetSettings() {
        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = NerdCamera.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    public NerdCamera clone() {
        NerdCamera toRet = new NerdCamera(this.SKETCH);

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
