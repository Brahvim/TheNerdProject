package com.brahvim.nerd.processing_wrappers;

import java.awt.Point;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class FlyCamera extends NerdCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final static float DEFAULT_MOUSE_SENSITIVITY = 0.2f;
    public volatile static Point mousePositionBeforeLock;
    public volatile static boolean pholdPointer, holdPointer = true;

    public PVector front;
    public float yaw, zoom, pitch;
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    // endregion

    // region Construction.
    public FlyCamera(Sketch p_sketch) {
        super(p_sketch);
        this.front = new PVector(super.SKETCH.cx, super.SKETCH.cy, 0);
    }
    // endregion

    // region From `NerdCamera`.
    @Override
    public void apply() {
        super.apply();

        if (FlyCamera.holdPointer && super.SKETCH.focused) {
            // region `if (FlyCamera.mousePositionBeforeLock == null)`,
            if (FlyCamera.mousePositionBeforeLock == null)
                FlyCamera.mousePositionBeforeLock = new Point(
                        super.SKETCH.displayWidthHalf,
                        super.SKETCH.displayHeightHalf);
            // endregion

            super.SKETCH.ROBOT.mouseMove(
                    FlyCamera.mousePositionBeforeLock.x,
                    FlyCamera.mousePositionBeforeLock.y);
        }

    }

    @Override
    public void applyMatrix() {
        this.mouseUpdate();

        // Apply projection:
        switch (this.projection) {
            case PConstants.PERSPECTIVE:
                super.SKETCH.perspective(this.fov,
                        (float) super.SKETCH.width / (float) super.SKETCH.height,
                        this.near, this.far);
                break;
            case PConstants.ORTHOGRAPHIC:
                super.SKETCH.ortho(
                        -super.SKETCH.cx, super.SKETCH.cx,
                        -super.SKETCH.cy, super.SKETCH.cy,
                        this.near, this.far);
        }

        // Apply the camera matrix:
        super.SKETCH.camera(
                this.pos.x, this.pos.y, this.pos.z,
                this.front.x + this.pos.x, this.front.y + this.pos.y, this.front.z + this.pos.z,
                this.up.x, this.up.y, this.up.z);

        // Translate! People probably still prefer things on the top left corner `P3D`
        // ...even if it could mean translating twice in some cases, it's alright!
        // super.SKETCH.translate(-super.SKETCH.cx, -super.SKETCH.cy);
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.SKETCH);

        // region Copying settings over to `toRet`.
        toRet.up = new PVector(this.up.x, this.up.x, this.up.z);
        toRet.pos = new PVector(this.pos.x, this.pos.x, this.pos.z);
        toRet.front = new PVector(this.front.x, this.front.x, this.front.z);

        toRet.far = this.far;
        toRet.fov = this.fov;
        toRet.near = this.near;

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
        this.yaw = 0;
        this.pitch = 0;
        this.front.set(0, 0, 0);
        this.mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    }

    @Override
    public void resetSettings() {
        super.resetSettings();
        this.shouldConstrainPitch = true;
    }
    // endregion

    // region Methods specific to `FlyCamera`.
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

    protected void mouseUpdate() {
        // region Update `yaw` and `pitch`:
        if (!FlyCamera.pholdPointer && FlyCamera.holdPointer) {

            // ...note of encouragement I left for myself back when the following line
            // was `FlyCamera.mousePositionBeforeLock = super.SKETCH.GLOBAL_MOUSE_POINT;`:
            /*
             * """
             * Don't worry, the GC is still going to do its job fine.
             * It doesn't have any JIT that would be 'disturbed' by doing this!
             * You're allocating TWO less `float`s by doing this! :joy:
             * """
             */

            FlyCamera.mousePositionBeforeLock.setLocation(super.SKETCH.GLOBAL_MOUSE_POINT);
            // ...buuuut assignment didn't work!
        }

        if (FlyCamera.holdPointer) {
            if (FlyCamera.mousePositionBeforeLock == null) {
                this.yaw += this.mouseSensitivity * super.SKETCH.GLOBAL_MOUSE_POINT.x;
                this.pitch += this.mouseSensitivity * super.SKETCH.GLOBAL_MOUSE_POINT.y;
            } else {
                this.yaw += this.mouseSensitivity
                        * (super.SKETCH.GLOBAL_MOUSE_POINT.x - FlyCamera.mousePositionBeforeLock.x);
                this.pitch += this.mouseSensitivity
                        * (super.SKETCH.GLOBAL_MOUSE_POINT.y - FlyCamera.mousePositionBeforeLock.y);
            }
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
