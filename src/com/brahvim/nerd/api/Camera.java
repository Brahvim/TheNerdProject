package com.brahvim.nerd.api;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * @apiNote Please use {@linkplain CameraBuilder} to get a camera instance.
 */
public class Camera {
    // region Interface `Camera.Script`.
    @FunctionalInterface
    public interface Script {
        public void onCamUpdate(Camera p_cam);
    }
    // endregion

    // region Fields.
    // region Constants.
    public final static float DEFAULT_CAM_FOV = PApplet.radians(60),
            DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10000, DEFAULT_CAM_MOUSE_Z = 25;

    public final PVector DEFAULT_CAM_UP, DEFAULT_CAM_POS, DEFAULT_CAM_CENTER;
    // endregion

    public Camera.Script script;

    public PVector pos, center, up;
    public float fov = Camera.DEFAULT_CAM_FOV,
            far = Camera.DEFAULT_CAM_FAR,
            near = Camera.DEFAULT_CAM_NEAR,
            mouseZ = Camera.DEFAULT_CAM_MOUSE_Z;

    public int clearColor = 0, projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;

    private Sketch parentSketch;
    // endregion

    public Camera(Sketch p_sketch) {
        this.parentSketch = p_sketch;

        this.DEFAULT_CAM_UP = new PVector(0, 1, 0);

        this.DEFAULT_CAM_POS = new PVector(
                this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f, 300);

        this.DEFAULT_CAM_CENTER = new PVector(
                this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f, 0);
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
        this.parentSketch.begin2d();
        // Removing this will not display the previous camera's view,
        // but still show clipping:
        this.parentSketch.camera();
        this.parentSketch.rectMode(PConstants.CORNER);
        this.parentSketch.noStroke();
        this.parentSketch.fill(this.clearColor);
        // this.parentSketch.rect(-width * 2.5f, -height * 2.5f, width * 7.5f, height *
        // 7.5f);
        this.parentSketch.rect(0, 0, this.parentSketch.width, this.parentSketch.height);
        this.parentSketch.end2d();
    }

    public void applyMatrix() {
        switch (this.projection) {
            case PConstants.PERSPECTIVE:
                this.parentSketch.perspective(this.fov,
                        (float) this.parentSketch.width / (float) this.parentSketch.height,
                        this.near, this.far);
                break;
            case PConstants.ORTHOGRAPHIC:
                this.parentSketch.ortho(
                        -this.parentSketch.cx, this.parentSketch.cx,
                        -this.parentSketch.cy, this.parentSketch.cy,
                        this.near, this.far);
        }

        this.parentSketch.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.center.z, this.center.y, this.center.z,
                this.up.x, this.up.y, this.up.z);

        // this.parentSketch.translate(-this.parentSketch.cx, -this.parentSketch.cy);
    }
    // endregion

    // region Copy and reset!
    public void reset() {
        this.up.set(this.DEFAULT_CAM_UP);
        this.pos.set(this.DEFAULT_CAM_POS);
        this.center.set(this.DEFAULT_CAM_CENTER);
        this.far = Camera.DEFAULT_CAM_FAR;
        this.fov = Camera.DEFAULT_CAM_FOV;
        this.near = Camera.DEFAULT_CAM_NEAR;
    }

    public void completeReset() {
        this.reset();
        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = Camera.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    public Camera copy() {
        Camera ret = new Camera(this.parentSketch);

        ret.up.set(this.up);
        ret.pos.set(this.pos);
        ret.center.set(this.center);

        ret.far = this.far;
        ret.fov = this.fov;
        ret.near = this.near;

        ret.script = this.script;
        return ret;
    }
    // endregion

}
