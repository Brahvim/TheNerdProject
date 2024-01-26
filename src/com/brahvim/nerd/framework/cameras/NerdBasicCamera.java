package com.brahvim.nerd.framework.cameras;

import java.util.Objects;

import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;

import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdBasicCameraBuilder} to get instances of
 *          {@link NerdBasicCamera}.
 */
public class NerdBasicCamera extends NerdAbstractCamera {

	public static final float
	/*   */ DEFAULT_CAM_CENTER_X = 0,
			DEFAULT_CAM_CENTER_Y = 0,
			DEFAULT_CAM_CENTER_Z = 0;

	protected PVector
	/*   */ center,
			defaultCamCenter = new PVector(
					NerdBasicCamera.DEFAULT_CAM_CENTER_X,
					NerdBasicCamera.DEFAULT_CAM_CENTER_Y,
					NerdBasicCamera.DEFAULT_CAM_CENTER_Z);

	public NerdBasicCamera(final NerdBasicCamera p_original) {
		super(p_original.GRAPHICS);

		// Copying camera parameters.
		this.up = p_original.up.copy();
		this.pos = p_original.pos.copy();
		this.center = p_original.center.copy();

		this.far = p_original.far;
		this.fov = p_original.fov;
		this.near = p_original.near;

		this.script = p_original.script;
	}

	protected NerdBasicCamera(final NerdP3dGraphics p_graphics) {
		super(p_graphics);
		this.center = new PVector();
	}

	@Override
	public void completeReset() {
		super.completeReset();

		if (this.defaultCamCenter == null)
			this.center.set(
					NerdBasicCamera.DEFAULT_CAM_CENTER_X,
					NerdBasicCamera.DEFAULT_CAM_CENTER_Y,
					NerdBasicCamera.DEFAULT_CAM_CENTER_Z);
		else
			this.center.set(this.defaultCamCenter);
	}

	@Override
	public void applyMatrix() {
		super.applyProjection();

		super.GRAPHICS.camera(
				this.pos.x, this.pos.y, this.pos.z,
				this.center.x, this.center.y, this.center.z,
				this.up.x, this.up.y, this.up.z);

		// Translate! People probably still prefer things on the top left corner `P3D`
		// ...even if it could mean translating twice in some cases, it's alright!
		// this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
		// ...nope! I'll remove this! It causes the camera position to seem to change
		// when you resize the window!
		// Lesson learnt: **use this only if your camera never moves!**
	}

	public PVector getCenter() {
		return this.center;
	}

	public PVector getDefaultCamCenter() {
		return this.defaultCamCenter;
	}

	public void setCenter(final PVector p_vec) {
		this.center = p_vec;
	}

	public void setDefaultCamCenter(final PVector p_defaultCamCenter) {
		this.defaultCamCenter = Objects.requireNonNull(p_defaultCamCenter);
	}

}
