package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PVector;

public class FlyCamera extends NerdCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final static float DEFAULT_MOUSE_SENSITIVITY = 0.2f;

    public float yaw, zoom, pitch;
    public PVector front = new PVector();
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    // endregion

    // region Constructors.
    public FlyCamera(Sketch p_sketch, NerdCamera p_camera) {
        super(p_sketch);

        super.up.set(p_camera.up);
        super.pos.set(p_camera.pos);
        super.center.set(p_camera.center);

        super.far = p_camera.far;
        super.fov = p_camera.fov;
        super.near = p_camera.near;

        super.script = p_camera.script;
    }

    public FlyCamera(Sketch p_sketch) {
        this(p_sketch, new NerdCamera(p_sketch));
    }
    // endregion

    // region From `NerdCamera`.
    @Override
    public void apply() {
        super.apply();
    }

    @Override
    public void applyMatrix() {
        if (super.SKETCH.mouseLeft)
            this.mouseUpdate();
        super.applyMatrix();

        // Was trying to fix that annoying `FlyCamera`-flips-at-origin bug.
        // this.SKETCH.translate(Sketch.FLOAT_HALF, Sketch.FLOAT_HALF);
        // ...didn't fix it :rofl:
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.SKETCH, super.clone());

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

    @Override
    public void resetCamParams() {
        this.yaw = 0;
        this.pitch = 0;
        this.front.set(0, 0, 0);
        this.mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;

        super.resetCamParams();
    }

    @Override
    public void resetSettings() {
        this.shouldConstrainPitch = true;
        super.resetSettings();
    }

    @Override
    public void runScript() {
        super.runScript();
    }

    @Override
    public void useProcessingDefaults() {
        super.useProcessingDefaults();
    }
    // endregion

    // region `public` methods specific to `FlyCamera`.
    public void moveX(float p_velX) {
        super.pos.add(
                PVector.mult(
                        PVector.cross(
                                this.front, super.up, null).normalize(),
                        p_velX));
    }

    public void moveY(float p_velY) {
        super.pos.y += p_velY;
        super.center.y += p_velY;
    }

    public void moveZ(float p_velZ) {
        super.pos.sub(PVector.mult(this.front, p_velZ));
    }
    // endregion

    protected void mouseUpdate() {
        // Update `yaw` and `pitch`:
        yaw += this.mouseSensitivity * (super.SKETCH.mouseX - super.SKETCH.pmouseX);
        pitch += this.mouseSensitivity * (super.SKETCH.pmouseY - super.SKETCH.mouseY); // Opposite!

        if (this.shouldConstrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        final float YAW_COS = PApplet.cos(PApplet.radians(yaw)),
                YAW_SIN = PApplet.sin(PApplet.radians(yaw)),
                PITCH_COS = PApplet.cos(PApplet.radians(pitch)),
                PITCH_SIN = PApplet.sin(PApplet.radians(pitch));

        // Calculate actual direction:
        this.front.set(
                -YAW_COS * PITCH_COS,
                PITCH_SIN,
                YAW_SIN * PITCH_COS).normalize();

        // Set it!:
        super.center.add(this.front);
    }

}
