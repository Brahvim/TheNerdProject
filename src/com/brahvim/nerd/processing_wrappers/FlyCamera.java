package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class FlyCamera extends NerdCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final static float DEFAULT_MOUSE_SENSITIVITY = 1;

    public float yaw, zoom, pitch;
    public PVector front = new PVector();
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    // endregion

    // region Constructors.
    public FlyCamera(Sketch p_sketch) {
        super(p_sketch);
    }

    public FlyCamera(NerdCamera p_camera) {
        super(p_camera);
    }
    // endregion

    // region From `NerdCamera`.
    @Override
    public void apply() {
        // #JIT_FTW!:
        this.clear();
        this.runScript();
        // switch (this.SKETCH.RENDERER) {
        // case PConstants.JAVA2D -> this.apply2dMatrix();
        // case PConstants.P3D, PConstants.P2D ->
        this.applyMatrix();
        // }
    }

    @Override
    public void applyMatrix() {
        // `FlyCamera` uses this...
        this.mouseUpdate();

        // Apply projection:
        switch (this.projection) {
            case PConstants.PERSPECTIVE:
                this.SKETCH.perspective(this.fov,
                        (float) this.SKETCH.width / (float) this.SKETCH.height,
                        this.near, this.far);
                break;
            case PConstants.ORTHOGRAPHIC:
                this.SKETCH.ortho(
                        -this.SKETCH.cx, this.SKETCH.cx,
                        -this.SKETCH.cy, this.SKETCH.cy,
                        this.near, this.far);
                break;
        }

        // Apply the camera matrix:
        this.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.pos.z + this.front.x, this.pos.y + this.front.y, this.pos.z + this.front.z,
                this.up.x, this.up.y, this.up.z);

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
    }

    @Override
    public void clear() {
        this.SKETCH.begin2d();
        // Removing this will not display the previous camera's view,
        // but still show clipping:
        this.SKETCH.camera();
        this.SKETCH.noStroke();
        this.SKETCH.fill(this.clearColor);
        this.SKETCH.rectMode(PConstants.CORNER);
        this.SKETCH.rect(0, 0, this.SKETCH.width, this.SKETCH.height);
        this.SKETCH.end2d();
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.clone());

        // region Copying settings over to `toRet`.
        toRet.yaw = this.yaw;
        toRet.zoom = this.zoom;
        toRet.pitch = this.pitch;
        toRet.front.set(this.front);

        toRet.mouseSensitivity = this.mouseSensitivity;
        toRet.shouldConstrainPitch = this.shouldConstrainPitch;
        // endregion

        return toRet;
    }

    @Override
    public void completeReset() {
        // Exactly what `NerdCamera` does.
        this.resetCamParams();
        this.resetSettings();
        // ...these two methods also wrap calls to `FlyCamera::CAMERA`.
    }

    public void resetCamParams() {
        this.yaw = 0;
        this.pitch = 0;
        this.front.set(0, 0, 0);
        this.mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;

        this.far = BasicCamera.DEFAULT_CAM_FAR;
        this.fov = BasicCamera.DEFAULT_CAM_FOV;
        this.near = BasicCamera.DEFAULT_CAM_NEAR;
    }

    public void resetSettings() {
        this.shouldConstrainPitch = true;

        // this.script = null;
        this.clearColor = 0;
        this.doScript = true;
        this.doAutoClear = true;
        this.mouseZ = NerdCamera.DEFAULT_CAM_MOUSE_Z;
        this.projection = PConstants.PERSPECTIVE;
    }

    @Override
    public void runScript() {
        super.runScript();
    }
    // endregion

    // region methods specific to `FlyCamera`.
    public void moveX(float p_velX) {
        PVector camRight = PVector.cross(this.front, super.up, null).normalize();
        super.pos.add(PVector.mult(camRight, p_velX));
    }

    public void moveY(float p_velY) {
        super.pos.y += p_velY;
    }

    public void moveZ(float p_velZ) {
        super.pos.add(PVector.mult(this.front, p_velZ));
    }

    protected void mouseUpdate() {
        // Update `this.yaw` and `this.pitch`:
        this.yaw += this.mouseSensitivity * (super.SKETCH.mouseX - super.SKETCH.pmouseX);
        this.pitch += this.mouseSensitivity * (super.SKETCH.mouseY - super.SKETCH.pmouseY); // ~~Opposite!~~

        if (this.shouldConstrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        final float COS_YAW = PApplet.cos(PApplet.radians(this.yaw)),
                SIN_YAW = PApplet.sin(PApplet.radians(this.yaw)),
                COS_PITCH = PApplet.cos(PApplet.radians(this.pitch)),
                SIN_PITCH = PApplet.sin(PApplet.radians(this.pitch));

        // Calculate `this.front`:
        this.front.set(
                COS_YAW * COS_PITCH,
                SIN_PITCH,
                SIN_YAW * COS_PITCH).normalize();

    }
    // endregion

}
