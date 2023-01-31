package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PVector;

public class FlyCamera extends NerdCamera {
    // Mathematics, thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final static float DEFAULT_MOUSE_SENSITIVITY = 0.1f;

    public float yaw, zoom, pitch;
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = FlyCamera.DEFAULT_MOUSE_SENSITIVITY;
    public PVector front = new PVector(),
            right = new PVector(),
            worldUp = super.up;
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
        this.updateFlyCamera();
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
    public void completeReset() {
        // Exactly what `NerdCamera` does.
        this.resetCamParams();
        this.resetSettings();
        // ...these two methods also wrap calls to `FlyCamera::CAMERA`.
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.SKETCH, super.clone());

        // region Copying settings over to `toRet`.
        toRet.yaw = this.yaw;
        toRet.zoom = this.zoom;
        toRet.pitch = this.pitch;

        toRet.front.set(this.front);
        toRet.right.set(this.right);
        toRet.worldUp.set(this.worldUp);

        toRet.mouseSensitivity = this.mouseSensitivity;
        toRet.shouldConstrainPitch = this.shouldConstrainPitch;
        // endregion

        return toRet;
    }

    @Override
    public void resetCamParams() {
        this.yaw = 0;
        this.pitch = 0;

        this.front.set(0, 0, 0);
        this.right.set(0, 0, 0);
        this.worldUp.set(0, 1, 0);

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
        p_velX *= this.SKETCH.frameTime;
        super.pos.add(PVector.mult(this.right, p_velX));
    }

    public void moveY(float p_velY) {
        super.pos.y += p_velY;
        super.center.y += p_velY;
    }

    public void moveZ(float p_velZ) {
        p_velZ *= this.SKETCH.frameTime;
        super.pos.add(PVector.mult(this.front, p_velZ));
    }
    // endregion

    private void updateFlyCamera() {
        // Again, thanks to [https://learnopengl.com/Getting-started/Camera]!!!

        final float YAW_SIN = PApplet.sin(PApplet.radians(this.yaw)),
                PITCH_SIN = PApplet.sin(PApplet.radians(this.pitch));
        final float YAW_COS = PApplet.cos(PApplet.radians(this.yaw)),
                PITCH_COS = PApplet.cos(PApplet.radians(this.pitch));

        this.front.x = YAW_COS * PITCH_COS;
        this.front.y = PITCH_SIN;
        this.front.z = YAW_SIN * PITCH_COS;
        this.front.normalize();

        PVector.cross(this.front, this.worldUp, this.right).normalize();
        PVector.cross(this.right, this.front, super.up).normalize();

        // Making sure `NerdCamera` uses these correctly with Processing:
        PVector.add(super.pos, this.front, super.center);

        // Doing mouse updates after translational ones as advised by
        // [https://stackoverflow.com/a/52949834/13951505] to prevent the camera from
        // to prevent the camera from flipping at the origin!

        // region Mouse movement updates.
        this.yaw += // this.SKETCH.frameTime *
                this.mouseSensitivity * (super.SKETCH.mouseY - super.SKETCH.pmouseY);
        this.pitch += // this.SKETCH.frameTime *
                this.mouseSensitivity * (super.SKETCH.mouseX - super.SKETCH.pmouseX);

        if (this.shouldConstrainPitch) {
            if (this.pitch > 89)
                this.pitch = 89;
            if (this.pitch < -89)
                this.pitch = -89;
        }
        // endregion

    }

}
