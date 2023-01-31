package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public abstract class NerdCamera {
    // region Interface `NerdCamera.Script`.
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
    public float fov = NerdCamera.DEFAULT_CAM_FOV,
            far = NerdCamera.DEFAULT_CAM_FAR,
            near = NerdCamera.DEFAULT_CAM_NEAR,
            mouseZ = NerdCamera.DEFAULT_CAM_MOUSE_Z,
            aspect /* `= 1`? */;

    public int clearColor = 0, projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;

    public PVector up, pos;
    // endregion

    public NerdCamera(Sketch p_sketch) {
        this.SKETCH = p_sketch;

        this.up = new PVector(0, 1, 0);
        this.pos = new PVector(this.SKETCH.cx, this.SKETCH.cy,
                this.SKETCH.cy / PApplet.tan(PConstants.PI * 30 / 180));
        this.aspect = (float) this.SKETCH.width / (float) this.SKETCH.height;
    }

    public abstract void applyMatrix();

    // region Pre-implmented methods.
    public void clear() {
        // this.SKETCH.background(this.clearColor);

        this.SKETCH.begin2d();
        // Removing this will not display the previous camera's view,
        // but still show clipping:
        this.SKETCH.camera();
        this.SKETCH.noStroke();

        this.SKETCH.fill(
                this.SKETCH.red(this.clearColor),
                this.SKETCH.green(this.clearColor),
                this.SKETCH.blue(this.clearColor),
                this.SKETCH.alpha(this.clearColor));

        this.SKETCH.rectMode(PConstants.CORNER);
        this.SKETCH.rect(0, 0, this.SKETCH.width, this.SKETCH.height);
        this.SKETCH.end2d();
    }

    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        this.applyMatrix();
    }

    public void completeReset() {
        this.resetParams();
        this.resetSettings();
    }

    public void resetParams() {
        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = BasicCamera.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    public void resetSettings() {
        this.far = BasicCamera.DEFAULT_CAM_FAR;
        this.fov = BasicCamera.DEFAULT_CAM_FOV;
        this.near = BasicCamera.DEFAULT_CAM_NEAR;
    }

    public void runScript() {
        if (this.script != null && this.doScript)
            this.script.onCamUpdate(this);
    }
    // endregion

}
