package com.brahvim.nerd.framework.cameras;

import java.util.function.Consumer;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PConstants;
import processing.core.PVector;

public class NerdBasicCameraBuilder {

    private final NerdBasicCamera build;
    private final NerdSketch SKETCH;

    public NerdBasicCameraBuilder(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch; // Used by `setClearColor()`.
        this.build = new NerdBasicCamera(p_sketch);

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

    public NerdBasicCamera build() {
        return this.build;
    }

    // region Vectors.
    // region Default ones.
    // region Default up vector.
    public NerdBasicCameraBuilder setDefaultUp(final float p_x, final float p_y) {
        this.build.defaultCamUp.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultUp(final float p_x, final float p_y, final float p_z) {
        this.build.defaultCamUp.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultUp(final PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamUp.set(p_vec);
        return this;
    }
    // endregion

    // region Default position vector.
    public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y) {
        this.build.defaultCamPos.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y, final float p_z) {
        this.build.defaultCamPos.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultPos(final PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamPos.set(p_vec);
        return this;
    }
    // endregion

    // region Default center vector.
    public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y) {
        this.build.defaultCamCenter.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y, final float p_z) {
        this.build.defaultCamCenter.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setDefaultCenter(final PVector p_vec) {
        if (p_vec != null)
            this.build.defaultCamCenter.set(p_vec);
        return this;
    }
    // endregion
    // endregion

    // region Dynamic ones.
    // region Dynamic up vector.
    public NerdBasicCameraBuilder setUp(final float p_x, final float p_y) {
        this.build.up.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setUp(final float p_x, final float p_y, final float p_z) {
        this.build.up.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setUp(final PVector p_vec) {
        if (p_vec != null)
            this.build.up.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic position vector.
    public NerdBasicCameraBuilder setPos(final float p_x, final float p_y) {
        this.build.pos.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setPos(final float p_x, final float p_y, final float p_z) {
        this.build.pos.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setPos(final PVector p_vec) {
        if (p_vec != null)
            this.build.pos.set(p_vec);
        return this;
    }
    // endregion

    // region Dynamic center vector.
    public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y) {
        this.build.center.set(p_x, p_y);
        return this;
    }

    public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y, final float p_z) {
        this.build.center.set(p_x, p_y, p_z);
        return this;
    }

    public NerdBasicCameraBuilder setCenter(final PVector p_vec) {
        if (p_vec != null)
            this.build.center.set(p_vec);
        return this;
    }
    // endregion
    // endregion
    // endregion

    // region `float` values.
    public NerdBasicCameraBuilder setFar(final float p_far) {
        this.build.far = p_far;
        return this;
    }

    public NerdBasicCameraBuilder setFov(final float p_fov) {
        this.build.fov = p_fov;
        return this;
    }

    public NerdBasicCameraBuilder setNear(final float p_fov) {
        this.build.near = p_fov;
        return this;
    }

    public NerdBasicCameraBuilder setMouseZ(final float p_mouseZ) {
        this.build.mouseZ = p_mouseZ;
        return this;
    }
    // endregion

    // region `boolean`s.
    public NerdBasicCameraBuilder setAutoClear(final boolean p_doAutoClear) {
        this.build.doAutoClear = p_doAutoClear;
        return this;
    }

    public NerdBasicCameraBuilder setScript(final boolean p_doScript) {
        this.build.doScript = p_doScript;
        return this;
    }
    // endregion

    public NerdBasicCameraBuilder setScript(final Consumer<NerdAbstractCamera> p_script) {
        this.build.script = p_script;
        return this;
    }

    // region `setClearColor()` overloads (they use `this.sketch`!).
    public NerdBasicCameraBuilder setClearColor(final int p_color) {
        this.build.clearColorParam1 = this.SKETCH.red(p_color);
        this.build.clearColorParam2 = this.SKETCH.green(p_color);
        this.build.clearColorParam3 = this.SKETCH.blue(p_color);
        this.build.clearColorParamAlpha = 255; // I have to do this!
        // this.alpha = this.SKETCH.alpha(p_color);
        return this;
    }

    public NerdBasicCameraBuilder setClearColor(final float p_grey, final float p_alpha) {
        this.build.clearColorParam1 = p_grey;
        this.build.clearColorParam2 = p_grey;
        this.build.clearColorParam3 = p_grey;
        this.build.clearColorParamAlpha = p_alpha;
        return this;
    }

    public NerdBasicCameraBuilder setClearColor(final float p_v1, final float p_v2, final float p_v3) {
        this.build.clearColorParam1 = p_v1;
        this.build.clearColorParam2 = p_v2;
        this.build.clearColorParam3 = p_v3;
        this.build.clearColorParamAlpha = 255;
        return this;
    }

    public NerdBasicCameraBuilder setClearColor(final float p_v1, final float p_v2, final float p_v3,
            final float p_alpha) {
        this.build.clearColorParam1 = p_v1;
        this.build.clearColorParam2 = p_v2;
        this.build.clearColorParam3 = p_v3;
        this.build.clearColorParamAlpha = p_alpha;
        return this;
    }
    // endregion

    // region Projection settings.
    public NerdBasicCameraBuilder useOrtho() {
        this.build.projection = PConstants.ORTHOGRAPHIC;
        return this;
    }

    public NerdBasicCameraBuilder usePerspective() {
        this.build.projection = PConstants.PERSPECTIVE;
        return this;
    }
    // endregion

}
