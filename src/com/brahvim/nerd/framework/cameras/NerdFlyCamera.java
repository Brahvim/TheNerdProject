package com.brahvim.nerd.framework.cameras;

import java.awt.Point;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.jogamp.newt.opengl.GLWindow;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdFlyCamera extends NerdAbstractCamera {
	// Mathematics, thanks to [ https://learnopengl.com/Getting-started/Camera ]!

	// region Fields.
	public static final float DEFAULT_MOUSE_SENSITIVITY = 0.18f;

	public final PVector FRONT, DEFAULT_FRONT;

	public float yaw, zoom, pitch;
	public boolean holdMouse = true;
	public boolean shouldConstrainPitch = true;
	public float mouseSensitivity = NerdFlyCamera.DEFAULT_MOUSE_SENSITIVITY;

	protected final NerdDisplayModule<PGraphics3D> DISPLAY;

	private boolean pholdMouse;
	// endregion

	// region Construction.
	// Nope! Can't merge the two o' these constructu's:
	@SuppressWarnings("unchecked")
	public NerdFlyCamera(final NerdP3dGraphics p_graphics, final PVector p_defaultFront) {
		super(p_graphics);
		this.DISPLAY = super.SKETCH.getNerdModule(NerdDisplayModule.class);

		this.FRONT = p_defaultFront.copy();
		super.WINDOW.cursorVisible = false;
		this.DEFAULT_FRONT = p_defaultFront.copy();
	}

	@SuppressWarnings("unchecked")
	public NerdFlyCamera(final NerdP3dGraphics p_graphics) {
		super(p_graphics);
		this.DISPLAY = super.SKETCH.getNerdModule(NerdDisplayModule.class);

		this.FRONT = super.POSITION.copy();
		super.WINDOW.cursorVisible = false;
		this.DEFAULT_FRONT = this.FRONT.copy();
	}

	public NerdFlyCamera(final NerdFlyCamera p_source) {
		super(p_source);
		this.DISPLAY = p_source.DISPLAY;

		// Copying settings over to `this`.
		this.FRONT = p_source.FRONT.copy();
		this.DEFAULT_FRONT = p_source.DEFAULT_FRONT.copy();

		this.far = p_source.far;
		this.fov = p_source.fov;
		this.near = p_source.near;

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
	public void reset() {
		super.reset();
		this.yaw = this.pitch = 0;
		this.FRONT.set(this.DEFAULT_FRONT);
	}

	@Override
	public void applyMatrix() {
		// System.out.println(this.front);
		// System.out.println(super.pos);
		// System.out.println(super.up);

		// Apply the camera matrix:
		super.GRAPHICS.camera(
				super.POSITION.x, super.POSITION.y, super.POSITION.z,

				// Camera center point:
				this.FRONT.x + super.POSITION.x,
				this.FRONT.y + super.POSITION.y,
				this.FRONT.z + super.POSITION.z,

				super.ORIENTATION.x, super.ORIENTATION.y, super.ORIENTATION.z);
	}
	// endregion

	// region Methods specific to `FlyCamera`.
	public boolean wasHoldingMouseLastFrame() {
		return this.pholdMouse;
	}

	public void moveVec(final PVector p_speed) {
		this.moveX(p_speed.x);
		this.moveY(p_speed.y);
		this.moveZ(p_speed.z);
	}

	public void moveX(final float p_velX) {
		super.POSITION.add(
				PVector.mult(
						PVector.cross(
								this.FRONT, super.ORIENTATION, null)
								.normalize(),
						p_velX));
	}

	public void moveY(final float p_velY) {
		super.POSITION.y += p_velY;
	}

	public void moveZ(final float p_velZ) {
		super.POSITION.sub(PVector.mult(this.FRONT, p_velZ));
	}

	public void addRoll(final float p_roll) {
		super.ORIENTATION.z += p_roll;
	}

	// Setters for movement:
	public void setRoll(final float p_roll) {
		super.ORIENTATION.z = p_roll;
	}

	public void addHeight(final float p_height) {
		super.POSITION.y += p_height;
	}

	public void setHeight(final float p_height) {
		super.POSITION.y = p_height;
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

			@SuppressWarnings("unchecked")
			final NerdInputModule<PGraphics3D> input = super.SKETCH.getNerdModule(NerdInputModule.class);

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

		this.FRONT.set(
				YAW_COS * PITCH_COS,
				/* */ PITCH_SIN /* */,
				YAW_SIN * PITCH_COS);
		this.FRONT.normalize();
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
