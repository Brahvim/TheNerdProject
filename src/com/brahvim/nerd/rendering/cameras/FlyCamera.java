package com.brahvim.nerd.rendering.cameras;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class FlyCamera extends NerdAbstractCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final static float DEFAULT_MOUSE_SENSITIVITY = 0.2f;
    public volatile static boolean pholdMouse, holdMouse = true;

    public float yaw, zoom, pitch;
    public PVector front = new PVector(), defaultCamFront = new PVector();
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    // endregion

    // region Construction.
    public FlyCamera(Sketch p_sketch) {
        super(p_sketch);
        this.front = super.pos.copy();
        this.defaultCamFront = this.front.copy();
    }

    public FlyCamera(Sketch p_sketch, PVector p_defaultFront) {
        super(p_sketch);
        this.front.set(p_defaultFront);
        this.defaultCamFront.set(p_defaultFront);
    }
    // endregion

    // region From `NerdCamera`.
    @Override
    public void apply() {
        super.apply();

        if (FlyCamera.holdMouse && super.SKETCH.focused)
            super.SKETCH.ROBOT.mouseMove(
                    super.SKETCH.displayWidthHalf,
                    super.SKETCH.displayHeightHalf);
    }

    @Override
    public void applyMatrix() {
        super.applyProjection();
        this.mouseTransform();

        // Apply the camera matrix:
        super.SKETCH.camera(
                super.pos.x, super.pos.y, super.pos.z,

                // Camera center point:
                this.front.x + super.pos.x,
                this.front.y + super.pos.y,
                this.front.z + super.pos.z,

                super.up.x, super.up.y, super.up.z);
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.SKETCH);

        // region Copying settings over to `toRet`.
        toRet.up = new PVector(super.up.x, super.up.x, super.up.z);
        toRet.pos = new PVector(super.pos.x, super.pos.x, super.pos.z);
        toRet.front = new PVector(this.front.x, this.front.x, this.front.z);

        toRet.far = super.far;
        toRet.fov = super.fov;
        toRet.near = super.near;

        toRet.script = this.script;

        toRet.yaw = this.yaw;
        toRet.zoom = this.zoom;
        toRet.pitch = this.pitch;

        toRet.mouseSensitivity = this.mouseSensitivity;
        toRet.shouldConstrainPitch = this.shouldConstrainPitch;
        // endregion

        return toRet;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        if (this.defaultCamFront == null)
            this.front.set(super.pos);
        else
            this.front.set(this.defaultCamFront);

        this.yaw = this.pitch = 0;
    }

    @Override
    public void resetSettings() {
        super.resetSettings();
        this.shouldConstrainPitch = true;
        this.mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    }
    // endregion

    public void useProcessingDefaults() {
        // Default camera values in Processing.
        // From [https://processing.org/reference/camera_.html].
        final float WIDTH_HALF = super.SKETCH.cx, HEIGHT_HALF = super.SKETCH.cy;

        super.defaultCamUp = new PVector(0, 1, 0);
        super.defaultCamPos = new PVector(
                WIDTH_HALF, HEIGHT_HALF,
                HEIGHT_HALF / (float) Math.tan(PConstants.PI * 30 / 180));
        this.defaultCamFront = new PVector(WIDTH_HALF, HEIGHT_HALF);
    }

    // region Methods specific to `FlyCamera`.
    // region Movement.
    public void moveX(float p_velX) {
        super.pos.add(
                PVector.mult(
                        PVector.cross(
                                this.front, super.up, null).normalize(),
                        p_velX));
    }

    public void moveY(float p_velY) {
        super.pos.y += p_velY;
    }

    public void moveZ(float p_velZ) {
        super.pos.sub(PVector.mult(this.front, p_velZ));
    }
    // endregion

    // region Rolling.
    public void roll(float p_roll) {
        super.up.x += p_roll;
    }
    // endregion

    protected void mouseTransform() {
        // region Update `yaw` and `pitch`:
        if (FlyCamera.holdMouse) {
            this.yaw += this.mouseSensitivity
                    * (super.SKETCH.GLOBAL_MOUSE_POINT.x - super.SKETCH.displayWidthHalf);
            this.pitch += this.mouseSensitivity
                    * (super.SKETCH.GLOBAL_MOUSE_POINT.y - super.SKETCH.displayHeightHalf);
        } else {
            this.yaw += this.mouseSensitivity * (super.SKETCH.mouseX - super.SKETCH.pmouseX);
            this.pitch += this.mouseSensitivity * (super.SKETCH.mouseY - super.SKETCH.pmouseY); // Remember! Opposite!
        }
        // endregion

        if (this.shouldConstrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        final float YAW_COS = PApplet.cos(PApplet.radians(this.yaw)),
                YAW_SIN = PApplet.sin(PApplet.radians(this.yaw)),
                PITCH_COS = PApplet.cos(PApplet.radians(this.pitch)),
                PITCH_SIN = PApplet.sin(PApplet.radians(this.pitch));

        // Calculate actual direction:
        this.front.set(
                YAW_COS * PITCH_COS,
                PITCH_SIN,
                YAW_SIN * PITCH_COS).normalize();
    }
    // endregion

}
