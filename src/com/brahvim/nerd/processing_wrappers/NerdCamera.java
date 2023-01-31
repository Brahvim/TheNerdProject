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

    // ...yeah, for some reason `PApplet::color()` fails.
    public float red, green, blue, alpha;
    public float fov = NerdCamera.DEFAULT_CAM_FOV,
            far = NerdCamera.DEFAULT_CAM_FAR,
            near = NerdCamera.DEFAULT_CAM_NEAR,
            mouseZ = NerdCamera.DEFAULT_CAM_MOUSE_Z,
            aspect /* `= 1`? */;

    public PVector up, pos;
    public int projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true;
    // endregion

    public NerdCamera(Sketch p_sketch) {
        this.SKETCH = p_sketch;

        this.up = new PVector(0, 1, 0);
        this.pos = new PVector(this.SKETCH.cx, this.SKETCH.cy,
                this.SKETCH.cy / PApplet.tan(PConstants.PI * 30 / 180));
        this.aspect = (float) this.SKETCH.width / (float) this.SKETCH.height;
    }

    public abstract void applyMatrix();

    // region Pre-implemented methods.
    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        this.applyMatrix();
    }

    public void clear() {
        this.SKETCH.alphaBg(this.red, this.green, this.blue, this.alpha);
    }

    public void completeReset() {
        this.resetParams();
        this.resetSettings();
    }

    public void resetParams() {
        // this.script = null;

        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.alpha = 255;

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

    // region `setClearColor()` overloads.
    public void setClearColor(int p_color) {
        this.red = this.SKETCH.red(p_color);
        this.green = this.SKETCH.green(p_color);
        this.blue = this.SKETCH.blue(p_color);
        this.alpha = 255; // I have to do this!
        // this.alpha = this.SKETCH.alpha(p_color);
    }

    public void setClearColor(float p_grey, float p_alpha) {
        this.red = p_grey;
        this.green = p_grey;
        this.blue = p_grey;
        this.alpha = p_alpha;
    }

    public void setClearColor(float p_red, float p_green, float p_blue) {
        this.red = p_red;
        this.green = p_green;
        this.blue = p_blue;
        this.alpha = 255;
    }

    public void setClearColor(float p_red, float p_green, float p_blue, float p_alpha) {
        this.red = p_red;
        this.green = p_green;
        this.blue = p_blue;
        this.alpha = p_alpha;
    }
    // endregion
    // endregion

}
