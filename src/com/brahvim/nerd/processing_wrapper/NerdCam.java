package com.brahvim.nerd.processing_wrapper;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * @apiNote Please use {@linkplain NerdCameraBuilder} to get a camera instance.
 */
public class NerdCam {
    // region Interface `Camera.Script`.
    // Needed to be able to clone cameras!
    @FunctionalInterface
    public interface Script {
        public void onCamUpdate(NerdCam p_cam);
    }
    // endregion

    // region Fields.
    public final static float DEFAULT_CAM_FOV = PApplet.radians(60),
            DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10000, DEFAULT_CAM_MOUSE_Z = 25;

    public NerdCam.Script script;

    public PVector pos, center, up;
    public PVector defaultCamUp, defaultCamPos, defaultCamCenter;
    public float fov = NerdCam.DEFAULT_CAM_FOV,
            far = NerdCam.DEFAULT_CAM_FAR,
            near = NerdCam.DEFAULT_CAM_NEAR,
            mouseZ = NerdCam.DEFAULT_CAM_MOUSE_Z;

    public int clearColor = 0, projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;

    private final Sketch SKETCH;
    // endregion

    public NerdCam(Sketch p_sketch) {
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
        this.applyMatrix();
    }

    public void runScript() {
        if (this.script != null && this.doScript)
            this.script.onCamUpdate(this);
    }

    public void clear() {
        this.SKETCH.begin2d();
        // Removing this will not display the previous camera's view,
        // but still show clipping:
        this.SKETCH.camera();
        this.SKETCH.rectMode(PConstants.CORNER);
        this.SKETCH.noStroke();
        this.SKETCH.fill(this.clearColor);
        this.SKETCH.rect(0, 0, this.SKETCH.width, this.SKETCH.height);
        this.SKETCH.end2d();
    }

    public void applyMatrix() {
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

        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.z, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);

        this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
    }
    // endregion

    // region Copy and reset!
    public void reset() {
        this.up.set(this.defaultCamUp);
        this.pos.set(this.defaultCamPos);
        this.center.set(this.defaultCamCenter);
        this.far = NerdCam.DEFAULT_CAM_FAR;
        this.fov = NerdCam.DEFAULT_CAM_FOV;
        this.near = NerdCam.DEFAULT_CAM_NEAR;
    }

    public void completeReset() {
        this.reset();
        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = NerdCam.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    public NerdCam copy() {
        NerdCam toRet = new NerdCam(this.SKETCH);

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
