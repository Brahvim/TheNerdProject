package com.brahvim.nerd.rendering.cameras;

import java.util.function.Consumer;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

// `abstract` because it is supposed to be extended everytime.
public abstract class NerdAbstractCamera {

    // region Fields.
    public static final float DEFAULT_CAM_FOV = PApplet.radians(60),
            DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10_000, DEFAULT_CAM_MOUSE_Z = 25;

    public final Sketch SKETCH;
    public Consumer<NerdAbstractCamera> script; // Smart users will write complete classes for these.

    // ...yeah, for some reason `PApplet::color()` fails.
    public float clearColorParam1, clearColorParam2, clearColorParam3, clearColorParamAlpha;
    public float fov = NerdAbstractCamera.DEFAULT_CAM_FOV,
            far = NerdAbstractCamera.DEFAULT_CAM_FAR,
            near = NerdAbstractCamera.DEFAULT_CAM_NEAR,
            mouseZ = NerdAbstractCamera.DEFAULT_CAM_MOUSE_Z,
            aspect /* `= 1`? */;

    public PVector pos = new PVector(), up = new PVector(0, 1, 0);
    public PVector defaultCamUp, defaultCamPos;
    public int projection = PConstants.PERSPECTIVE;
    public boolean doScript = true, doAutoClear = true, doAutoAspect = true;
    // endregion

    public NerdAbstractCamera(Sketch p_sketch) {
        this.SKETCH = p_sketch;
        // this.up = new PVector(0, 1, 0);
        // this.defaultCamUp = this.up.copy();

        // this.pos = new PVector(this.SKETCH.cx, this.SKETCH.cy,
        // this.SKETCH.cy / PApplet.tan(PConstants.PI * 30 / 180));
        // this.defaultCamPos = this.pos.copy();

        // this.useDefaultsFromProcessing();
    }

    public abstract void applyMatrix();

    public void applyProjection() {
        if (this.SKETCH.RENDERER != PConstants.P3D)
            return;

        if (this.doAutoAspect)
            // It probably is faster not to perform this check.
            // if (!(this.SKETCH.pwidth == this.SKETCH.width
            // || this.SKETCH.pheight == this.SKETCH.height))
            // A simple divide instruction is enough!
            this.aspect = (float) this.SKETCH.width / (float) this.SKETCH.height;

        // Apply projection:
        switch (this.projection) {
            case PConstants.PERSPECTIVE -> this.SKETCH.perspective(
                    this.fov, this.aspect, this.near, this.far);

            case PConstants.ORTHOGRAPHIC -> this.SKETCH.ortho(
                    -this.SKETCH.cx, this.SKETCH.cx,
                    -this.SKETCH.cy, this.SKETCH.cy,
                    this.near, this.far);

            default -> throw new UnsupportedOperationException(
                    "`NerdCamera::projection` can only be either" +
                            "`PConstants.PERSPECTIVE` or `PConstants.ORTHOGRAPHIC`.");
        }
    }

    // region Pre-implemented methods.
    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        this.applyMatrix();
    }

    public void clear() {
        this.SKETCH.alphaBg(
                this.clearColorParam1, this.clearColorParam2,
                this.clearColorParam3, this.clearColorParamAlpha);
    }

    public void completeReset() {
        // region Parameters and `NerdAbstractCamera`-only vectors.
        this.clearColorParam1 = 0;
        this.clearColorParam2 = 0;
        this.clearColorParam3 = 0;
        this.clearColorParamAlpha = 255;

        if (this.defaultCamUp == null)
            this.up.set(0, 1, 0);
        else
            this.up.set(this.defaultCamUp);

        if (this.defaultCamPos == null)
            this.pos.set(0, 0, 0);
        else
            this.pos.set(this.defaultCamPos);
        // endregion

        // region Settings.
        this.projection = PConstants.PERSPECTIVE;
        this.doScript = true;
        this.doAutoClear = true;
        this.far = BasicCamera.DEFAULT_CAM_FAR;
        this.fov = BasicCamera.DEFAULT_CAM_FOV;
        this.near = BasicCamera.DEFAULT_CAM_NEAR;
        this.mouseZ = BasicCamera.DEFAULT_CAM_MOUSE_Z;
        // endregion
    }

    public void runScript() {
        if (this.script != null && this.doScript)
            this.script.accept(this);
    }

    // region `setClearColor()` overloads.
    public void setClearColor(int p_color) {
        this.clearColorParam1 = this.SKETCH.red(p_color);
        this.clearColorParam2 = this.SKETCH.green(p_color);
        this.clearColorParam3 = this.SKETCH.blue(p_color);
        this.clearColorParamAlpha = 255; // I have to do this!
        // this.alpha = this.SKETCH.alpha(p_color);
    }

    public void setClearColor(float p_grey, float p_alpha) {
        this.clearColorParam1 = p_grey;
        this.clearColorParam2 = p_grey;
        this.clearColorParam3 = p_grey;
        this.clearColorParamAlpha = p_alpha;
    }

    public void setClearColor(float p_v1, float p_v2, float p_v3) {
        this.clearColorParam1 = p_v1;
        this.clearColorParam2 = p_v2;
        this.clearColorParam3 = p_v3;
        this.clearColorParamAlpha = 255;
    }

    public void setClearColor(float p_v1, float p_v2, float p_v3, float p_alpha) {
        this.clearColorParam1 = p_v1;
        this.clearColorParam2 = p_v2;
        this.clearColorParam3 = p_v3;
        this.clearColorParamAlpha = p_alpha;
    }
    // endregion
    // endregion

}
