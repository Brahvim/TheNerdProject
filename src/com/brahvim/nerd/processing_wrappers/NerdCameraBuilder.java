package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PConstants;
import processing.core.PVector;

public class NerdCameraBuilder {
    private NerdCam build;
    private final Sketch SKETCH;

    public NerdCameraBuilder(Sketch p_sketch) {
        this.SKETCH = p_sketch; // Used by `setClearColor()`.
        this.build = new NerdCam(p_sketch);
        this.build.useProcessingDefaults();

        // region My defaults:
        // this.defaultCamUp = new PVector(0, 1, 0);

        // this.defaultCamPos = new PVector(
        // this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f,
        // 600);

        // this.defaultCamCenter = new PVector(
        // this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f,
        // 0);
        // endregion

    }

    public NerdCam build() {
        return this.build;
    }

    // region Vectors.
    // region Default ones.
    // region Default up vector.
    public NerdCameraBuilder setDefaultUp(float p_x, float p_y) {
        this.build.defaultCamUp.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setDefaultUp(float p_x, float p_y, float p_z) {
        this.build.defaultCamUp.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setDefaultUp(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamUp.set(p_vec);
        return this;
    }
    // endregion

    // region Default position vector.
    public NerdCameraBuilder setDefaultPos(float p_x, float p_y) {
        this.build.defaultCamPos.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setDefaultPos(float p_x, float p_y, float p_z) {
        this.build.defaultCamPos.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setDefaultPos(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamPos.set(p_vec);
        return this;
    }
    // endregion

    // region Default center vector.
    public NerdCameraBuilder setDefaultCenter(float p_x, float p_y) {
        this.build.defaultCamCenter.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setDefaultCenter(float p_x, float p_y, float p_z) {
        this.build.defaultCamCenter.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setDefaultCenter(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamCenter.set(p_vec);
        return this;
    }
    // endregion
    // endregion

    // region Dynamic ones.
    // region Dynamic up vector.
    public NerdCameraBuilder setUp(float p_x, float p_y) {
        this.build.up.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setUp(float p_x, float p_y, float p_z) {
        this.build.up.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setUp(PVector p_vec) {
        if (p_vec != null)
            this.build.up.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic position vector.
    public NerdCameraBuilder setPos(float p_x, float p_y) {
        this.build.pos.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setPos(float p_x, float p_y, float p_z) {
        this.build.pos.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setPos(PVector p_vec) {
        if (p_vec != null)
            this.build.pos.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic center vector.
    public NerdCameraBuilder setCenter(float p_x, float p_y) {
        this.build.center.set(p_x, p_y);
        return this;
    }

    public NerdCameraBuilder setCenter(float p_x, float p_y, float p_z) {
        this.build.center.set(p_x, p_y, p_z);
        return this;
    }

    public NerdCameraBuilder setCenter(PVector p_vec) {
        if (p_vec != null)
            this.build.center.set(p_vec);
        return this;
    }
    // endregion
    // endregion
    // endregion

    // region `float` values.
    public NerdCameraBuilder setFar(float p_far) {
        this.build.far = p_far;
        return this;
    }

    public NerdCameraBuilder setFov(float p_fov) {
        this.build.fov = p_fov;
        return this;
    }

    public NerdCameraBuilder setNear(float p_fov) {
        this.build.near = p_fov;
        return this;
    }

    public NerdCameraBuilder setMouseZ(float p_mouseZ) {
        this.build.mouseZ = p_mouseZ;
        return this;
    }
    // endregion

    // region `boolean`s.
    public NerdCameraBuilder setAutoClear(boolean p_doAutoClear) {
        this.build.doAutoClear = p_doAutoClear;
        return this;
    }

    public NerdCameraBuilder setScript(boolean p_doScript) {
        this.build.doScript = p_doScript;
        return this;
    }
    // endregion

    public NerdCameraBuilder setScript(NerdCam.Script p_script) {
        this.build.script = p_script;
        return this;
    }

    // region `setClearColor()` overloads (they use `this.sketch`!).
    public NerdCameraBuilder setClearColor(int p_color) {
        this.build.clearColor = p_color;
        return this;
    }

    public NerdCameraBuilder setClearColor(float p_grey, float p_alpha) {
        this.build.clearColor = this.SKETCH.color(p_grey, p_alpha);
        return this;
    }

    public NerdCameraBuilder setClearColor(float p_red, float p_green, float p_blue) {
        this.build.clearColor = this.SKETCH.color(p_red, p_green, p_blue);
        return this;
    }

    public NerdCameraBuilder setClearColor(float p_red, float p_green, float p_blue, float p_alpha) {
        this.build.clearColor = this.SKETCH.color(p_red, p_green, p_blue, p_alpha);
        return this;
    }
    // endregion

    // region Projection settings.
    public NerdCameraBuilder useOrtho() {
        this.build.projection = PConstants.ORTHOGRAPHIC;
        return this;
    }

    public NerdCameraBuilder usePerspective() {
        this.build.projection = PConstants.PERSPECTIVE;
        return this;
    }
    // endregion

}
