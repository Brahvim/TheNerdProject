package com.brahvim.nerd.processing_wrapper;

import processing.core.PConstants;
import processing.core.PVector;

public class NerdCameraBuilder {
    private NerdCam build;
    private final Sketch sketch;

    public NerdCameraBuilder(Sketch p_sketch) {
        this.sketch = p_sketch; // Used by `setClearColor()`.
        this.build = new NerdCam(p_sketch);

        this.build.up.set(this.build.defaultCamUp);
        this.build.pos.set(this.build.defaultCamPos);
        this.build.center.set(this.build.defaultCamCenter);
    }

    public NerdCam build() {
        return this.build;
    }

    // region Vectors.
    // region Up vector.
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

    // region Position vector.
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

    // region Position vector.
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
        this.build.clearColor = this.sketch.color(p_grey, p_alpha);
        return this;
    }

    public NerdCameraBuilder setClearColor(float p_red, float p_green, float p_blue) {
        this.build.clearColor = this.sketch.color(p_red, p_green, p_blue);
        return this;
    }

    public NerdCameraBuilder setClearColor(float p_red, float p_green, float p_blue, float p_alpha) {
        this.build.clearColor = this.sketch.color(p_red, p_green, p_blue, p_alpha);
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
