package com.brahvim.nerd.api;

import processing.core.PConstants;
import processing.core.PVector;

public class CameraBuilder {
    private Camera build;
    private Sketch sketch;

    public CameraBuilder(Sketch p_sketch) {
        this.sketch = p_sketch; // Used by `setClearColor()`.
        this.build = new Camera(p_sketch);

        this.build.up.set(this.build.DEFAULT_CAM_UP);
        this.build.pos.set(this.build.DEFAULT_CAM_POS);
        this.build.center.set(this.build.DEFAULT_CAM_CENTER);
    }

    public Sketch getSketch() {
        return this.sketch;
    }

    public Camera build() {
        return this.build;
    }

    // region Vectors.
    // region Up vector.
    public CameraBuilder setUp(float p_x, float p_y) {
        this.build.up.set(p_x, p_y);
        return this;
    }

    public CameraBuilder setUp(float p_x, float p_y, float p_z) {
        this.build.up.set(p_x, p_y, p_z);
        return this;
    }

    public CameraBuilder setUp(PVector p_vec) {
        if (p_vec != null)
            this.build.up.set(p_vec);
        return this;
    }
    // endregion

    // region Position vector.
    public CameraBuilder setPos(float p_x, float p_y) {
        this.build.pos.set(p_x, p_y);
        return this;
    }

    public CameraBuilder setPos(float p_x, float p_y, float p_z) {
        this.build.pos.set(p_x, p_y, p_z);
        return this;
    }

    public CameraBuilder setPos(PVector p_vec) {
        if (p_vec != null)
            this.build.pos.set(p_vec);
        return this;
    }
    // endregion

    // region Position vector.
    public CameraBuilder setCenter(float p_x, float p_y) {
        this.build.center.set(p_x, p_y);
        return this;
    }

    public CameraBuilder setCenter(float p_x, float p_y, float p_z) {
        this.build.center.set(p_x, p_y, p_z);
        return this;
    }

    public CameraBuilder setCenter(PVector p_vec) {
        if (p_vec != null)
            this.build.center.set(p_vec);
        return this;
    }
    // endregion
    // endregion

    // region `float` values.
    public CameraBuilder setFar(float p_far) {
        this.build.far = p_far;
        return this;
    }

    public CameraBuilder setFov(float p_fov) {
        this.build.fov = p_fov;
        return this;
    }

    public CameraBuilder setNear(float p_fov) {
        this.build.near = p_fov;
        return this;
    }

    public CameraBuilder setMouseZ(float p_mouseZ) {
        this.build.mouseZ = p_mouseZ;
        return this;
    }
    // endregion

    // region `boolean`s.
    public CameraBuilder setAutoClear(boolean p_doAutoClear) {
        this.build.doAutoClear = p_doAutoClear;
        return this;
    }

    public CameraBuilder setScript(boolean p_doScript) {
        this.build.doScript = p_doScript;
        return this;
    }
    // endregion

    public CameraBuilder setScript(Camera.Script p_script) {
        this.build.script = p_script;
        return this;
    }

    // region `setClearColor()` overloads (they use `this.sketch`!).
    public CameraBuilder setClearColor(int p_color) {
        this.build.clearColor = p_color;
        return this;
    }

    public CameraBuilder setClearColor(float p_grey, float p_alpha) {
        this.build.clearColor = this.sketch.color(p_grey, p_alpha);
        return this;
    }

    public CameraBuilder setClearColor(float p_red, float p_green, float p_blue) {
        this.build.clearColor = this.sketch.color(p_red, p_green, p_blue);
        return this;
    }

    public CameraBuilder setClearColor(float p_red, float p_green, float p_blue, float p_alpha) {
        this.build.clearColor = this.sketch.color(p_red, p_green, p_blue, p_alpha);
        return this;
    }
    // endregion

    // region Projection settings.
    public CameraBuilder useOrtho() {
        this.build.projection = PConstants.ORTHOGRAPHIC;
        return this;
    }

    public CameraBuilder usePerspective() {
        this.build.projection = PConstants.PERSPECTIVE;
        return this;
    }
    // endregion

    /*
     * public CameraBuilder restart() {
     * this.build.script = null;
     * this.build.completeReset();
     * return this;
     * }
     */

}
