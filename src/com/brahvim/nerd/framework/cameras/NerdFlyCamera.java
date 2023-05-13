package com.brahvim.nerd.framework.cameras;

import java.awt.Point;

import com.brahvim.nerd.processing_wrapper.NerdDisplayManager;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdWindowManager;

import processing.core.PApplet;
import processing.core.PVector;

public class NerdFlyCamera extends NerdAbstractCamera {
	// Mathematics, thanks to https://learnopengl.com/Getting-started/Camera!

	// region Fields.
	public static final float DEFAULT_MOUSE_SENSITIVITY = 0.2f;

	public float yaw, zoom, pitch;
	public boolean shouldConstrainPitch = true;
	public boolean holdMouse = true, pholdMouse;
	public float mouseSensitivity = NerdFlyCamera.DEFAULT_MOUSE_SENSITIVITY;
	public PVector front = new PVector(), defaultCamFront = new PVector();

	private final NerdWindowManager WINDOW;
	private final NerdDisplayManager DISPLAYS;
	// endregion

	// region Construction.
	public NerdFlyCamera(final NerdSketch p_sketch) {
		super(p_sketch);
		this.front = super.pos.copy();
		this.WINDOW = super.SKETCH.WINDOW;
		this.DISPLAYS = super.SKETCH.DISPLAYS;

		this.WINDOW.cursorVisible = false;
		this.defaultCamFront = this.front.copy();
	}

	public NerdFlyCamera(final NerdSketch p_sketch, final PVector p_defaultFront) {
		super(p_sketch);
		this.front.set(p_defaultFront);
		this.WINDOW = super.SKETCH.WINDOW;
		this.DISPLAYS = super.SKETCH.DISPLAYS;

		this.WINDOW.cursorVisible = false;
		this.defaultCamFront.set(p_defaultFront);
	}
	// endregion

	// region From `NerdCamera`.
	@Override
	public void apply() {
		super.apply();

		if (super.SKETCH.focused)
			this.mouseTransform();

		this.pholdMouse = this.holdMouse;
	}

	@Override
	public NerdFlyCamera clone() {
		final NerdFlyCamera toRet = new NerdFlyCamera(super.SKETCH);

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
	public void moveX(final float p_velX) {
		super.pos.add(
				PVector.mult(
						PVector.cross(
								this.front, super.up, null).normalize(),
						p_velX));
	}

	public void moveY(final float p_velY) {
		super.pos.y += p_velY;
	}

	public void moveZ(final float p_velZ) {
		super.pos.sub(PVector.mult(this.front, p_velZ));
	}

	public void roll(final float p_roll) {
		super.up.x += p_roll;
	}

	// Setters for movement:
	public void setY(final float p_height) {
		super.pos.y = p_height;
	}

	public void setRoll(final float p_roll) {
		super.up.x = p_roll;
	}

	protected void mouseTransform() {
		if (!this.holdMouse && this.pholdMouse
				|| this.holdMouse && !this.pholdMouse)
			return;

		// region Update `yaw` and `pitch`, and perform the mouse-lock:
		final Point mouseLockPos = this.calculateMouseLockPos();

		if (this.holdMouse) {
			super.SKETCH.ROBOT.mouseMove(mouseLockPos.x, mouseLockPos.y);
			this.yaw += this.mouseSensitivity
					* (super.SKETCH.GLOBAL_MOUSE_POINT.x - mouseLockPos.x);
			this.pitch += this.mouseSensitivity
					* (super.SKETCH.GLOBAL_MOUSE_POINT.y - mouseLockPos.y);
		} else if (super.SKETCH.mousePressed) {
			this.yaw += this.mouseSensitivity * (super.SKETCH.mouseX - super.SKETCH.pmouseX);
			this.pitch += this.mouseSensitivity * (super.SKETCH.mouseY - super.SKETCH.pmouseY);
		}
		// endregion

		if (this.shouldConstrainPitch) {
			if (this.pitch > 89.0f)
				this.pitch = 89.0f;
			if (this.pitch < -89.0f)
				this.pitch = -89.0f;
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

	private Point calculateMouseLockPos() {
		if (this.WINDOW.fullscreen) {
			// this.DISPLAYS.updateDisplayRatios();
			return new Point(this.DISPLAYS.displayWidthHalf, this.DISPLAYS.displayHeightHalf);
		} else {
			final PVector position = this.WINDOW.getPosition();
			return new Point(
					(int) (position.x + this.WINDOW.cx),
					(int) (position.y + this.WINDOW.cy));
		}
	}
	// endregion

}
