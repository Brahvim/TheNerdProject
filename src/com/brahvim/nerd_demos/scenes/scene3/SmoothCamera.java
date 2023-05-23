package com.brahvim.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PVector;

public class SmoothCamera extends NerdFlyCamera {

	// region Fields.
	public float accFrict = 0.9f, velFrict = 0.9f;
	public float normalSpeed = 0.5f, fastSpeed = 2, slowSpeed = 0.125f;

	private PVector accVec = new PVector(), velVec = new PVector();
	// endregion

	// region Construction.
	public SmoothCamera(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	public SmoothCamera(final NerdSketch p_sketch, final PVector p_defaultFront) {
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

	@Override
	public void apply() {
		this.controlCamera();
		super.apply();
	}

	private void controlCamera() {
		// Increase speed when holding `Ctrl`:
		final float accMultiplier;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_CONTROL))
			accMultiplier = this.fastSpeed;
		else if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_ALT))
			accMultiplier = this.slowSpeed;
		else
			accMultiplier = this.normalSpeed;

		// region Roll.
		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_Z))
			super.up.x += this.normalSpeed * 0.1f;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_C))
			super.up.x += -this.normalSpeed * 0.1f;

		// if (super.up.x > PConstants.TAU || super.up.x < -PConstants.TAU)
		// super.up.x -= super.up.x;
		// endregion

		// region Elevation.
		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_SPACE))
			this.accVec.y += -accMultiplier;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_SHIFT))
			this.accVec.y += accMultiplier;
		// endregion

		// region `W`-`A`-`S`-`D` controls.
		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_W))
			this.accVec.z += -accMultiplier;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_A))
			this.accVec.x += -accMultiplier;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_S))
			this.accVec.z += accMultiplier;

		if (SKETCH.INPUT.keyIsPressed(KeyEvent.VK_D))
			this.accVec.x += accMultiplier;

		this.accVec.mult(this.accFrict);
		this.velVec.add(this.accVec);
		this.velVec.mult(this.velFrict);

		super.moveX(this.velVec.x);
		super.moveY(this.velVec.y);
		super.moveZ(this.velVec.z);
		// endregion
	}

}
