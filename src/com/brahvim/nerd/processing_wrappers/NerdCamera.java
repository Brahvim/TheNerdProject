package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public abstract class NerdCamera {

    // region Interface `NerdCamera.Script`
    @FunctionalInterface
    public static interface Script {
        public void onCamUpdate(NerdCamera p_cam);
    }
    // endregion

    // region Fields.
    public final static float DEFAULT_CAM_FOV = PApplet.radians(60),
            DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10000, DEFAULT_CAM_MOUSE_Z = 25;

    public final Sketch SKETCH;
    public NerdCamera.Script script;

    public int clearColor = 0, projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;

    public PVector pos, up;
    public float fov = BasicCamera.DEFAULT_CAM_FOV,
            far = BasicCamera.DEFAULT_CAM_FAR,
            near = BasicCamera.DEFAULT_CAM_NEAR,
            mouseZ = BasicCamera.DEFAULT_CAM_MOUSE_Z;
    // endregion

    // region Constructors.
    public NerdCamera(Sketch p_sketch) {
        this.SKETCH = p_sketch;

        this.up = new PVector();
        this.pos = new PVector();
    }

    public NerdCamera(NerdCamera p_camera) {
        this.SKETCH = p_camera.SKETCH;

        this.doScript = p_camera.doScript;
        this.doAutoClear = p_camera.doAutoClear;

        this.clearColor = p_camera.clearColor;
        this.projection = p_camera.projection;

        this.far = p_camera.far;
        this.fov = p_camera.fov;
        this.near = p_camera.near;
        this.mouseZ = p_camera.mouseZ;

        this.up = new PVector();
        this.pos = new PVector();

        this.script = p_camera.script;
    }
    // endregion

    public void runScript() {
        if (this.script != null && this.doScript)
            this.script.onCamUpdate(this);
    }

    public NerdCamera clone() {
        NerdCamera toRet = new BasicCamera(this.SKETCH);

        toRet.up.set(this.up);
        toRet.pos.set(this.pos);

        toRet.far = this.far;
        toRet.fov = this.fov;
        toRet.near = this.near;

        toRet.script = this.script;

        return toRet;
    }

    public void resetCamParams() {
        this.far = BasicCamera.DEFAULT_CAM_FAR;
        this.fov = BasicCamera.DEFAULT_CAM_FOV;
        this.near = BasicCamera.DEFAULT_CAM_NEAR;
    }

    public void resetSettings() {
        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = BasicCamera.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    // region `abstract` methods.
    public abstract void apply();

    public abstract void applyMatrix();

    public abstract void clear();

    public abstract void completeReset();
    // endregion

}
