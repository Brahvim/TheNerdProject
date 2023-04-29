package com.brahvim.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;
import java.util.Objects;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.rendering.cameras.FlyCamera;

import processing.core.PVector;

public class SmoothCamera extends FlyCamera {

    // region Fields.
    public float accFrict, velFrict;
    public float normalSpeed = 0.5f, fastSpeed = 2, slowSpeed = 0.125f;

    private PVector accVec = new PVector(), velVec = new PVector();
    // endregion

    // region Construction.
    public SmoothCamera(final Sketch p_sketch) {
        super(p_sketch);
    }

    public SmoothCamera(final Sketch p_sketch, final PVector p_defaultFront) {
        super(p_sketch, p_defaultFront);
    }
    // endregion

    // region Getters and setters.
    public PVector getVelVec() {
        return this.velVec;
    }

    public PVector getAccVec() {
        return this.accVec;
    }

    public PVector setVelVec(final PVector p_vec) {
        return this.velVec = Objects.requireNonNull(p_vec);
    }

    public PVector setAccVec(final PVector p_vec) {
        return this.accVec = Objects.requireNonNull(p_vec);
    }
    // endregion

    public void update() {
        this.controlCameraWithAcc();
    }

    private void controlCameraWithAcc() {
        // Increase speed when holding `Ctrl`:
        final float accMultiplier;

        if (SKETCH.keyIsPressed(KeyEvent.VK_CONTROL))
            accMultiplier = 2;
        else if (SKETCH.keyIsPressed(KeyEvent.VK_ALT))
            accMultiplier = 0.125f;
        else
            accMultiplier = 0.5f;

        // region Roll.
        if (SKETCH.keyIsPressed(KeyEvent.VK_Z))
            super.getUp().x += accMultiplier * 0.1f;

        if (SKETCH.keyIsPressed(KeyEvent.VK_C))
            super.getUp().x += -accMultiplier * 0.1f;
        // endregion

        // region Elevation.
        if (SKETCH.keyIsPressed(KeyEvent.VK_SPACE))
            this.accVec.y += /* this.GRAVITY * */ -accMultiplier;

        if (SKETCH.keyIsPressed(KeyEvent.VK_SHIFT))
            this.accVec.y += accMultiplier;
        // endregion

        // region `W`-`A`-`S`-`D` controls.
        if (SKETCH.keyIsPressed(KeyEvent.VK_W))
            this.accVec.z += -accMultiplier;

        if (SKETCH.keyIsPressed(KeyEvent.VK_A))
            this.accVec.x += -accMultiplier;

        if (SKETCH.keyIsPressed(KeyEvent.VK_S))
            this.accVec.z += accMultiplier;

        if (SKETCH.keyIsPressed(KeyEvent.VK_D))
            this.accVec.x += accMultiplier;

        this.velVec.add(this.accVec);
        this.accVec.mult(this.accFrict);
        this.velVec.mult(this.velFrict);

        super.moveX(this.velVec.x);
        super.moveY(this.velVec.y);
        super.moveZ(this.velVec.z);

        // this.accVec.set(0, 0, 0);
        // endregion
    }

}
