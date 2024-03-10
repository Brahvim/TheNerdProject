package com.brahvim.nerd.framework.cameras;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PVector;

/**
 * @apiNote Please use a {@link NerdBasicCameraBuilder} to get instances of
 *          {@link NerdBasicCamera}.
 */
public class NerdBasicCamera extends NerdAbstractCamera {

	// region Fields.
	public static final float
	/*   */ DEFAULT_CENTER_X = 0,
			DEFAULT_CENTER_Y = 0,
			DEFAULT_CENTER_Z = 0;

	public final PVector CENTER, DEFAULT_CENTER;
	// endregion

	public NerdBasicCamera(final NerdBasicCamera p_source) {
		super(p_source);
		this.CENTER = p_source.CENTER.copy();
		this.DEFAULT_CENTER = p_source.DEFAULT_CENTER.copy();
	}

	protected NerdBasicCamera(final NerdP3dGraphics p_graphics) {
		super(p_graphics);
		this.DEFAULT_CENTER = new PVector(
				NerdBasicCamera.DEFAULT_CENTER_X,
				NerdBasicCamera.DEFAULT_CENTER_Y,
				NerdBasicCamera.DEFAULT_CENTER_Z);
		this.CENTER = new PVector(
				NerdBasicCamera.DEFAULT_CENTER_X,
				NerdBasicCamera.DEFAULT_CENTER_Y,
				NerdBasicCamera.DEFAULT_CENTER_Z);
	}

	@Override
	public void reset() {
		super.reset();
		this.CENTER.set(this.DEFAULT_CENTER);
	}

	@Override
	public void applyMatrix() {
		super.GRAPHICS.camera(
				this.POSITION.x, this.POSITION.y, this.POSITION.z,
				this.CENTER.x, this.CENTER.y, this.CENTER.z,
				this.ORIENTATION.x, this.ORIENTATION.y, this.ORIENTATION.z);

		// Translate! People probably still prefer things on the top left corner `P3D`
		// ...even if it could mean translating twice in some cases, it's alright!
		// this.SKETCH.translate(-this.SKETCH.cx, -this.SKETCH.cy);
		// ...nope! I'll remove this! It causes the camera position to seem to change
		// when you resize the window!
		// Lesson learnt: **use this only if your camera never moves!**
	}

}
