package com.brahvim.nerd.framework.cameras;

import java.util.Objects;

import com.brahvim.nerd.processing_wrapper.graphics_backends.nerd_graphics_impls.NerdP3dGraphics;

import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdBasicCameraBuilder} to get instances of
 *          {@link NerdBasicCamera}.
 */
public class NerdBasicCamera extends NerdAbstractCamera {

	protected PVector center, defaultCamCenter;

	public NerdBasicCamera(final NerdBasicCamera original) {
		super(original.GRAPHICS);

		// Copying camera parameters.
		this.up = original.up.copy();
		this.pos = original.pos.copy();
		this.center = original.center.copy();

		this.far = original.far;
		this.fov = original.fov;
		this.near = original.near;

		this.script = original.script;
	}

	protected NerdBasicCamera(final NerdP3dGraphics p_graphics) {
		super(p_graphics);
		this.center = new PVector();
	}

	@Override
	public void completeReset() {
		super.completeReset();

		if (this.defaultCamCenter == null)
			this.center.set(0, 0, 0);
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
