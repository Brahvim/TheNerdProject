package com.brahvim.nerd.framework.cameras;

import java.awt.Point;

import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.jogamp.newt.opengl.GLWindow;

import processing.core.PApplet;
import processing.core.PVector;

public class NerdFlyCamera extends NerdAbstractCamera {
	// Mathematics, thanks to https://learnopengl.com/Getting-started/Camera!

	// region Fields.
	public static final float DEFAULT_MOUSE_SENSITIVITY = 0.18f;

	public float yaw, zoom, pitch;
	public boolean holdMouse = true;
	public boolean shouldConstrainPitch = true;
	public PVector front = new PVector(), defaultCamFront = new PVector();
	public float mouseSensitivity = NerdFlyCamera.DEFAULT_MOUSE_SENSITIVITY;

	protected final NerdDisplayModule DISPLAY;

	private boolean pholdMouse;
	// endregion

	// region Construction.
	// Nope! Can't merge the two o' these:
	public NerdFlyCamera(final NerdP3dGraphics p_graphics) {
		super(p_graphics);
		this.front = super.pos.copy();
		this.DISPLAY = super.SKETCH.getNerdModule(NerdDisplayModule.class);

		super.WINDOW.cursorVisible = false;
		this.defaultCamFront = this.front.copy();
	}

	public NerdFlyCamera(final NerdP3dGraphics p_graphics, final PVector p_defaultFront) {
		super(p_graphics);
		this.front.set(p_defaultFront);
		this.DISPLAY = super.SKETCH.getNerdModule(NerdDisplayModule.class);

		super.WINDOW.cursorVisible = false;
		this.defaultCamFront.set(p_defaultFront);
	}

	public NerdFlyCamera(final NerdFlyCamera p_source) {
		super(p_source.GRAPHICS);
		this.DISPLAY = p_source.DISPLAY;

		// Copying settings over to `this`.
		this.up = p_source.up.copy();
		this.pos = p_source.pos.copy();
		this.front = p_source.front.copy();

		this.far = p_source.far;
		this.fov = p_source.fov;
		this.near = p_source.near;

		this.script = p_source.script;

		this.yaw = p_source.yaw;
		this.zoom = p_source.zoom;
		this.pitch = p_source.pitch;

		this.mouseSensitivity = p_source.mouseSensitivity;
		this.shouldConstrainPitch = p_source.shouldConstrainPitch;
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
	public void applyMatrix() {
		super.applyProjection();

		// System.out.println(this.front);
		// System.out.println(super.pos);
		// System.out.println(super.up);

		// Apply the camera matrix:
		super.GRAPHICS.camera(
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
	public boolean wasHoldingMouseLastFrame() {
		return this.pholdMouse;
	}

	public void moveX(final float p_velX) {
		super.pos.add(
				PVector.mult(
						PVector.cross(
								this.front, super.up, null)
								.normalize(),
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
		if (!this.holdMouse && this.pholdMouse) // || this.holdMouse && !this.pholdMouse)
			return;

		// region Update `yaw` and `pitch`, and perform the mouse-lock:
		final Point mouseLockPos = this.calculateMouseLockPos();

		if (this.holdMouse) {
			final GLWindow window = super.WINDOW.getNativeObject();
			// window.warpPointer(mouseLockPos.x, mouseLockPos.y);
			window.warpPointer(window.getSurfaceWidth() / 2, window.getSurfaceHeight() / 2);

			final var input = super.SKETCH.getNerdModule(NerdInputModule.class);

			// Should use our own `Robot` instance anyway!:
			// super.SKETCH.ROBOT.mouseMove(mouseLockPos.x, mouseLockPos.y);
			this.yaw += this.mouseSensitivity
					* (input.GLOBAL_MOUSE_POINT.x - mouseLockPos.x);
			this.pitch += this.mouseSensitivity
					* (input.GLOBAL_MOUSE_POINT.y - mouseLockPos.y);
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
		final float
		/*   */ YAW_COS = PApplet.cos(PApplet.radians(this.yaw)),
				YAW_SIN = PApplet.sin(PApplet.radians(this.yaw)),
				PITCH_COS = PApplet.cos(PApplet.radians(this.pitch)),
				PITCH_SIN = PApplet.sin(PApplet.radians(this.pitch));

		this.front.set(
				YAW_COS * PITCH_COS,
				/* */ PITCH_SIN /* */,
				YAW_SIN * PITCH_COS);
		this.front.normalize();
		// endregion
	}

	protected Point calculateMouseLockPos() {
		if (super.WINDOW.fullscreen) {
			// this.DISPLAYS.updateDisplayRatios();
			return new Point(this.DISPLAY.displayWidthHalf, this.DISPLAY.displayHeightHalf);
		} else {
			final PVector position = super.WINDOW.getPositionAsPVector();
			return new Point(
					(int) (position.x + super.WINDOW.cx),
					(int) (position.y + super.WINDOW.cy));
		}
	}
	// endregion

}
