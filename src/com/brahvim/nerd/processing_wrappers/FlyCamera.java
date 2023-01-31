package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.math.VecUtilsForPVector;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PVector;

public class FlyCamera extends NerdCamera {
    // Mathematics thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public float sensitivity = 0.1f;
    public float pitch, yaw;
    public PVector mouseDir = new PVector(),
            camFront = new PVector(),
            camAddent = new PVector();

    private float sinYaw, cosYaw, sinPitch, cosPitch;
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
        this.rotateCamera();
        super.apply();
    }

    @Override
    public void applyMatrix() {
        this.rotateCamera();
        super.applyMatrix();
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
        // ...these two methods also wrap calls to `FpsCamera::CAMERA`.
    }

    @Override
    public FlyCamera clone() {
        FlyCamera toRet = new FlyCamera(super.SKETCH, super.clone());

        // region Setting Euler angles.
        toRet.yaw = this.yaw;
        toRet.pitch = this.pitch;

        toRet.sinYaw = this.sinYaw;
        toRet.sinPitch = this.sinPitch;

        toRet.cosYaw = this.cosYaw;
        toRet.cosPitch = this.cosPitch;
        // endregion

        toRet.mouseDir.set(this.mouseDir);
        toRet.camFront.set(this.camFront);
        toRet.camAddent.set(this.camAddent);

        return toRet;
    }

    @Override
    public void resetCamParams() {
        this.yaw = 0;
        this.pitch = 0;

        this.sinYaw = 0;
        this.sinPitch = 0;

        this.cosYaw = 0;
        this.cosPitch = 0;

        this.mouseDir.set(0, 0, 0);
        this.camFront.set(0, 0, 0);
        this.camAddent.set(0, 0, 0);

        super.resetCamParams();
    }

    @Override
    public void resetSettings() {
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

    // TODO: Fix these!
    // region Methods specific to `FlyCamera`.
    public void moveX(float p_velX) {
        this.camAddent.add(
                VecUtilsForPVector.normalize(VecUtilsForPVector.cross(
                        this.camFront, super.up)).mult(p_velX));
    }

    // TODO: Play around with this and figure the Math out!
    public void moveY(float p_velY) {
        super.pos.y += p_velY;
        super.center.y += p_velY;
    }

    public void moveZ(float p_velZ) {
        this.camAddent.add(PVector.mult(this.camFront, p_velZ));
    }

    public void rotateCamera() {
        System.out.println(super.SKETCH.mouseX - super.SKETCH.pmouseX);

        this.yaw += (super.SKETCH.mouseX - super.SKETCH.pmouseX) * this.sensitivity;
        this.pitch += (super.SKETCH.mouseY - super.SKETCH.pmouseY) * this.sensitivity;

        if (this.pitch > 89)
            this.pitch = 89;
        if (this.pitch < -89)
            this.pitch = -89;

        this.sinYaw = PApplet.sin(PApplet.radians(this.yaw));
        this.cosYaw = PApplet.cos(PApplet.radians(this.yaw));

        this.sinPitch = PApplet.sin(PApplet.radians(this.pitch));
        this.cosPitch = PApplet.cos(PApplet.radians(this.pitch));

        // this.mouseDir.set(0, 0, 0);
        this.mouseDir.x = this.cosYaw * this.cosPitch;
        this.mouseDir.y = this.sinPitch;
        this.mouseDir.z = this.sinYaw * this.cosPitch;

        this.camFront.set(this.mouseDir.normalize());

        // this.camAddent.y = 0;
        super.pos.add(this.camAddent);
        this.camAddent.set(0, 0, 0);

        // super.center.set(0, 0, 0);
        super.center.add(super.pos);
        super.center.add(this.camFront);

    }
    // endregion

}
