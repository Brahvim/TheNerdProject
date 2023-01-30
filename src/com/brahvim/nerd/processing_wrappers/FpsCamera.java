package com.brahvim.nerd.processing_wrappers;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PVector;

public class FpsCamera {
    // Mathematics thanks to [https://learnopengl.com/Getting-started/Camera]!

    // region Fields.
    public final Sketch SKETCH;
    public final NerdCamera CAMERA;

    public float sensitivity = 0.1f;
    public float pitch, yaw;
    public PVector mouseDir = new PVector(),
            camFront = new PVector(),
            camAddent = new PVector();

    private float sinYaw, cosYaw, sinPitch, cosPitch;
    // endregion

    // region Constructors.
    public FpsCamera(Sketch p_sketch, NerdCamera p_camera) {
        this.SKETCH = p_sketch;
        this.CAMERA = p_camera;
    }

    public FpsCamera(Sketch p_sketch) {
        this(p_sketch, new NerdCamera(p_sketch));
    }
    // endregion

    // region From `NerdCamera`.
    public void apply() {
        this.rotateCamera();
        this.CAMERA.apply();
    }

    public void applyMatrix() {
        this.rotateCamera();
        this.CAMERA.applyMatrix();
    }

    public void clear() {
        this.CAMERA.clear();
    }

    public void completeReset() {
        // Exactly what `NerdCamera` does.
        this.resetCamParams();
        this.resetSettings();
        // ...these two methods also wrap calls to `FpsCamera::CAMERA`.
    }

    public FpsCamera clone() {
        FpsCamera toRet = new FpsCamera(this.SKETCH, this.CAMERA.clone());

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

        this.CAMERA.resetCamParams();
    }

    public void resetSettings() {
        this.CAMERA.resetSettings();
    }

    public void runScript() {
        this.CAMERA.runScript();
    }

    public void useProcessingDefaults() {
        this.CAMERA.useProcessingDefaults();
    }
    // endregion

    public void rotateCamera() {
        this.yaw += (this.SKETCH.mouseX - this.SKETCH.pmouseX) * this.sensitivity;
        this.pitch += (this.SKETCH.mouseY - this.SKETCH.pmouseY) * this.sensitivity;

        if (this.pitch > 89)
            this.pitch = 89;
        if (this.pitch < -89)
            this.pitch = -89;

        this.mouseDir.set(0, 0, 0);

        this.sinYaw = PApplet.sin(PApplet.radians(this.yaw));
        this.cosYaw = PApplet.cos(PApplet.radians(this.yaw));

        this.sinPitch = PApplet.sin(PApplet.radians(this.pitch));
        this.cosPitch = PApplet.cos(PApplet.radians(this.pitch));

        this.mouseDir.x = this.cosYaw * this.cosPitch;
        this.mouseDir.y = this.sinPitch;
        this.mouseDir.z = this.sinYaw * this.cosPitch;

        this.camFront.set(this.mouseDir.normalize());
    }

    public void setScript(NerdCamera.Script p_script) {
        this.CAMERA.script = p_script;
    }

}
