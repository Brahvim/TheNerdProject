package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PConstants;
import processing.core.PVector;

public class BasicCameraBuilder {
    private BasicCamera build;
    private final Sketch SKETCH;

    public BasicCameraBuilder(Sketch p_sketch) {
        this.SKETCH = p_sketch; // Used by `setClearColor()`.
        this.build = new BasicCamera(p_sketch);
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

    public BasicCamera build() {
        return this.build;
    }

    // region Vectors.
    // region Default ones.
    // region Default up vector.
    public BasicCameraBuilder setDefaultUp(float p_x, float p_y) {
        this.build.defaultCamUp.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setDefaultUp(float p_x, float p_y, float p_z) {
        this.build.defaultCamUp.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setDefaultUp(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamUp.set(p_vec);
        return this;
    }
    // endregion

    // region Default position vector.
    public BasicCameraBuilder setDefaultPos(float p_x, float p_y) {
        this.build.defaultCamPos.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setDefaultPos(float p_x, float p_y, float p_z) {
        this.build.defaultCamPos.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setDefaultPos(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamPos.set(p_vec);
        return this;
    }
    // endregion

    // region Default center vector.
    public BasicCameraBuilder setDefaultCenter(float p_x, float p_y) {
        this.build.defaultCamCenter.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setDefaultCenter(float p_x, float p_y, float p_z) {
        this.build.defaultCamCenter.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setDefaultCenter(PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamCenter.set(p_vec);
        return this;
    }
    // endregion
    // endregion

    // region Dynamic ones.
    // region Dynamic up vector.
    public BasicCameraBuilder setUp(float p_x, float p_y) {
        this.build.up.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setUp(float p_x, float p_y, float p_z) {
        this.build.up.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setUp(PVector p_vec) {
        if (p_vec != null)
            this.build.up.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic position vector.
    public BasicCameraBuilder setPos(float p_x, float p_y) {
        this.build.pos.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setPos(float p_x, float p_y, float p_z) {
        this.build.pos.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setPos(PVector p_vec) {
        if (p_vec != null)
            this.build.pos.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic center vector.
    public BasicCameraBuilder setCenter(float p_x, float p_y) {
        this.build.center.set(p_x, p_y);
        return this;
    }

    public BasicCameraBuilder setCenter(float p_x, float p_y, float p_z) {
        this.build.center.set(p_x, p_y, p_z);
        return this;
    }

    public BasicCameraBuilder setCenter(PVector p_vec) {
        if (p_vec != null)
            this.build.center.set(p_vec);
        return this;
    }
    // endregion
    // endregion
    // endregion

    // region `float` values.
    public BasicCameraBuilder setFar(float p_far) {
        this.build.far = p_far;
        return this;
    }

    public BasicCameraBuilder setFov(float p_fov) {
        this.build.fov = p_fov;
        return this;
    }

    public BasicCameraBuilder setNear(float p_fov) {
        this.build.near = p_fov;
        return this;
    }

    public BasicCameraBuilder setMouseZ(float p_mouseZ) {
        this.build.mouseZ = p_mouseZ;
        return this;
    }
    // endregion

    // region `boolean`s.
    public BasicCameraBuilder setAutoClear(boolean p_doAutoClear) {
        this.build.doAutoClear = p_doAutoClear;
        return this;
    }

    public BasicCameraBuilder setScript(boolean p_doScript) {
        this.build.doScript = p_doScript;
        return this;
    }
    // endregion

    public BasicCameraBuilder setScript(NerdCamera.Script p_script) {
        this.build.script = p_script;
        return this;
    }

    // region `setClearColor()` overloads (they use `this.sketch`!).
    public BasicCameraBuilder setClearColor(int p_color) {
        this.build.red = this.SKETCH.red(p_color);
        this.build.green = this.SKETCH.green(p_color);
        this.build.blue = this.SKETCH.blue(p_color);
        this.build.alpha = 255; // I have to do this!
        // this.alpha = this.SKETCH.alpha(p_color);
        return this;
    }

    public BasicCameraBuilder setClearColor(float p_grey, float p_alpha) {
        this.build.red = p_grey;
        this.build.green = p_grey;
        this.build.blue = p_grey;
        this.build.alpha = p_alpha;
        return this;
    }

    public BasicCameraBuilder setClearColor(float p_red, float p_green, float p_blue) {
        this.build.red = p_red;
        this.build.green = p_green;
        this.build.blue = p_blue;
        this.build.alpha = 255;
        return this;
    }

    public BasicCameraBuilder setClearColor(float p_red, float p_green, float p_blue, float p_alpha) {
        this.build.red = p_red;
        this.build.green = p_green;
        this.build.blue = p_blue;
        this.build.alpha = p_alpha;
        return this;
    }
    // endregion

    // region Projection settings.
    public BasicCameraBuilder useOrtho() {
        this.build.projection = PConstants.ORTHOGRAPHIC;
        return this;
    }

    public BasicCameraBuilder usePerspective() {
        this.build.projection = PConstants.PERSPECTIVE;
        return this;
    }
    // endregion

}
