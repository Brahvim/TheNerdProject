package com.brahvim.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdInputManager;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class SmoothCamera extends NerdFlyCamera {

	// region Fields.
	public static final float DEFAULT_ACC_FRICT = 0.9f, DEFAULT_VEL_FRICT = 0.9f;
	public static final float NORMAL_SPEED = 0.5f, FAST_SPEED = 2, SLOW_SPEED = 0.125f;

	public float accFrict = SmoothCamera.DEFAULT_ACC_FRICT, velFrict = SmoothCamera.DEFAULT_VEL_FRICT;

	private final NerdInputManager INPUT;
	private PVector accVec = new PVector(), velVec = new PVector();
	private PVector circumAmbPos = new PVector();
	// endregion

	// region Construction.
	public SmoothCamera(final NerdGraphics p_graphics) {
		super(p_graphics);
		this.INPUT = super.SKETCH.INPUT;
	}

	public SmoothCamera(final NerdGraphics p_graphics, final PVector p_defaultFront) {
		super(p_graphics, p_defaultFront);
		this.INPUT = super.SKETCH.INPUT;
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

	public PVector getCircumAmbPos() {
		return this.circumAmbPos;
	}

	public PVector setCircumAmbPos(final PVector p_vec) {
		return this.circumAmbPos = p_vec;
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

		// Reset these so holding `ALT` changes them:
		this.accFrict = SmoothCamera.DEFAULT_ACC_FRICT;
		this.velFrict = SmoothCamera.DEFAULT_VEL_FRICT;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_CONTROL)) {
			accMultiplier = SmoothCamera.FAST_SPEED;
		} else if (this.INPUT.keyIsPressed(KeyEvent.VK_ALT)) {
			accMultiplier = SmoothCamera.SLOW_SPEED;
			this.accFrict = this.velFrict = 0.95f;
		} else {
			accMultiplier = SmoothCamera.NORMAL_SPEED;
		}

		// region Roll.
		if (this.INPUT.keyIsPressed(KeyEvent.VK_Z))
			super.up.x += SmoothCamera.NORMAL_SPEED * 0.1f;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_C))
			super.up.x += -SmoothCamera.NORMAL_SPEED * 0.1f;

		// if (super.up.x > PConstants.TAU || super.up.x < -PConstants.TAU)
		// super.up.x -= super.up.x;
		// endregion

		// region Elevation.
		if (this.INPUT.keyIsPressed(KeyEvent.VK_SPACE))
			this.accVec.y += -accMultiplier;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_SHIFT))
			this.accVec.y += accMultiplier;
		// endregion

		// region Circumambulation, id est "moving in circles".
		if (this.INPUT.keyIsPressed(KeyEvent.VK_Q)) {
			if (!this.INPUT.keyWasPressed(KeyEvent.VK_Q))
				this.circumAmbPos.set(super.front); // PVector.sub(super.front, super.pos));

			// super.front.set(this.circumAmbPos);

			super.pos.x += PApplet.sin(SKETCH.millis() * 0.01f * accMultiplier) * 50;
			super.pos.z += PApplet.cos(SKETCH.millis() * 0.01f * accMultiplier) * 50;

			super.front.x = PApplet.sin(-SKETCH.millis() * 0.01f * accMultiplier) * 50;
			super.front.z = PApplet.cos(-SKETCH.millis() * 0.01f * accMultiplier) * 50;
		}

		if (this.INPUT.keyIsPressed(KeyEvent.VK_E)) {
			if (!this.INPUT.keyWasPressed(KeyEvent.VK_E))
				this.circumAmbPos.set(super.front); // PVector.sub(super.front, super.pos));

			// super.front.set(this.circumAmbPos);

			super.pos.x += PApplet.sin(-SKETCH.millis() * 0.01f * accMultiplier) * 50;
			super.pos.z += PApplet.cos(-SKETCH.millis() * 0.01f * accMultiplier) * 50;

			super.front.x = PApplet.sin(-SKETCH.millis() * 0.01f * accMultiplier) * 50;
			super.front.z = PApplet.cos(-SKETCH.millis() * 0.01f * accMultiplier) * 50;
		}
		// endregion

		// region `W`-`A`-`S`-`D` controls.
		if (this.INPUT.keyIsPressed(KeyEvent.VK_W))
			this.accVec.z += -accMultiplier;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_A))
			this.accVec.x += -accMultiplier;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_S))
			this.accVec.z += accMultiplier;

		if (this.INPUT.keyIsPressed(KeyEvent.VK_D))
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
