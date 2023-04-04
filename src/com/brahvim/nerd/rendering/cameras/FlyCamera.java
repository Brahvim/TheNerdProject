package com.brahvim.nerd.rendering.cameras;

import java.awt.Point;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PVector;

public class FlyCamera extends NerdAbstractCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public static final float DEFAULT_MOUSE_SENSITIVITY = 0.2f;

    public boolean holdMouse = true;
    public float yaw, zoom, pitch;
    public PVector front = new PVector(), defaultCamFront = new PVector();
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    // endregion

    // region Construction.
    public FlyCamera(Sketch p_sketch) {
        super(p_sketch);
        this.front = super.pos.copy();
        super.SKETCH.cursorVisible = false;
        this.defaultCamFront = this.front.copy();
    }

    public FlyCamera(Sketch p_sketch, PVector p_defaultFront) {
        super(p_sketch);
        this.front.set(p_defaultFront);
        super.SKETCH.cursorVisible = false;
        this.defaultCamFront.set(p_defaultFront);
    }
    // endregion

    // region From `NerdCamera`.
    @Override
    public void apply() {
        super.apply();

        if (this.holdMouse && super.SKETCH.focused) {
            this.mouseTransform();

            final Point point = this.calculateMouseLockPos();
            super.SKETCH.ROBOT.mouseMove(point.x, point.y);
        }
    }

    private Point calculateMouseLockPos() {
        if (this.SKETCH.fullscreen)
            return new Point(this.SKETCH.displayWidthHalf, this.SKETCH.displayHeightHalf);
        else
            return new Point(
                    (int) (super.SKETCH.WINDOW_POSITION.x),
                    (int) (super.SKETCH.WINDOW_POSITION.y));
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
    public void applyMatrix() {
        super.applyProjection();

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
    public void completeReset() {
        super.completeReset();

        if (this.defaultCamFront == null)
            this.front.set(super.pos);
        else
            this.front.set(this.defaultCamFront);

        this.yaw = this.pitch = 0;
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

    public void roll(float p_roll) {
        super.up.x += p_roll;
    }

    protected void mouseTransform() {
        // region Update `yaw` and `pitch`:
        // System.out.println(super.SKETCH.GLOBAL_MOUSE_POINT);
        // System.out.println(super.SKETCH.displayWidthHalf);

        final Point mouseLockPos = this.calculateMouseLockPos();

        if (this.holdMouse) {
            this.yaw += this.mouseSensitivity
                    * (super.SKETCH.GLOBAL_MOUSE_POINT.x - mouseLockPos.x); // super.SKETCH.displayWidthHalf);
            this.pitch += this.mouseSensitivity
                    * (super.SKETCH.GLOBAL_MOUSE_POINT.y - mouseLockPos.y); // super.SKETCH.displayHeightHalf);
        } else {
            this.yaw += this.mouseSensitivity * (super.SKETCH.mouseX - mouseLockPos.x);
            this.pitch += this.mouseSensitivity * (super.SKETCH.mouseY - mouseLockPos.y);
            // this.yaw += this.mouseSensitivity * (super.SKETCH.mouseX -
            // super.SKETCH.pmouseX);
            // this.pitch += this.mouseSensitivity * (super.SKETCH.mouseY -
            // super.SKETCH.pmouseY); // Remember! Opposite!
        }
        // endregion

        if (this.shouldConstrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        // region Find `this.front` (point camera looks at; related to position).
        final float YAW_COS = PApplet.cos(PApplet.radians(this.yaw)),
                YAW_SIN = PApplet.sin(PApplet.radians(this.yaw)),
                PITCH_COS = PApplet.cos(PApplet.radians(this.pitch)),
                PITCH_SIN = PApplet.sin(PApplet.radians(this.pitch));

        this.front.set(
                YAW_COS * PITCH_COS,
                PITCH_SIN,
                YAW_SIN * PITCH_COS);
        this.front.normalize();
        // endregion
    }
    // endregion

}
