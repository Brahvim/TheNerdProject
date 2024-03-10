package com.brahvim.nerd.framework.cameras;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PConstants;
import processing.core.PVector;

public class NerdBasicCameraBuilder {

	private final NerdBasicCamera BUILD;

	// This order of dependence between these constructors ensures
	// that any `NerdSketch<PGraphics3D>` implementation can be used;
	// ...not just `NerdP3dGraphics`!
	public NerdBasicCameraBuilder(final NerdP3dGraphics p_graphics) {
		this.BUILD = new NerdBasicCamera(p_graphics);
	}

	public NerdBasicCamera build() {
		return this.BUILD;
	}

	// region Vectors.
	// region Constant (default) ones.
	// region Default orientation vector.
	public NerdBasicCameraBuilder setDefaultOrientation(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.DEFAULT_ORIENTATION.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultOrientation(final float p_x, final float p_y) {
		this.BUILD.DEFAULT_ORIENTATION.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultOrientation(final float p_x, final float p_y, final float p_z) {
		this.BUILD.DEFAULT_ORIENTATION.set(p_x, p_y, p_z);
		return this;
	}
	// endregion

	// region Default position vector.
	public NerdBasicCameraBuilder setDefaultPos(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.DEFAULT_POSITION.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y) {
		this.BUILD.DEFAULT_POSITION.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y, final float p_z) {
		this.BUILD.DEFAULT_POSITION.set(p_x, p_y, p_z);
		return this;
	}
	// endregion

	// region Default center vector.
	public NerdBasicCameraBuilder setDefaultCenter(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.DEFAULT_CENTER.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y) {
		this.BUILD.DEFAULT_CENTER.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y, final float p_z) {
		this.BUILD.DEFAULT_CENTER.set(p_x, p_y, p_z);
		return this;
	}
	// endregion
	// endregion

	// region Dynamic (variable) ones.
	// region Variable orientation vector.
	public NerdBasicCameraBuilder setOrientation(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.ORIENTATION.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setOrientation(final float p_x, final float p_y) {
		this.BUILD.ORIENTATION.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setOrientation(final float p_x, final float p_y, final float p_z) {
		this.BUILD.ORIENTATION.set(p_x, p_y, p_z);
		return this;
	}
	// endregion

	// region Variable position vector.
	public NerdBasicCameraBuilder setPos(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.POSITION.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setPos(final float p_x, final float p_y) {
		this.BUILD.POSITION.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setPos(final float p_x, final float p_y, final float p_z) {
		this.BUILD.POSITION.set(p_x, p_y, p_z);
		return this;
	}
	// endregion

	// region Variable center vector.
	public NerdBasicCameraBuilder setCenter(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.CENTER.set(p_vec);
		return this;
	}

	public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y) {
		this.BUILD.CENTER.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y, final float p_z) {
		this.BUILD.CENTER.set(p_x, p_y, p_z);
		return this;
	}
	// endregion
	// endregion
	// endregion

	// region `float`s.
	public NerdBasicCameraBuilder setFar(final float p_far) {
		this.BUILD.far = p_far;
		return this;
	}

	public NerdBasicCameraBuilder setFov(final float p_fov) {
		this.BUILD.fov = p_fov;
		return this;
	}

	public NerdBasicCameraBuilder setNear(final float p_near) {
		this.BUILD.near = p_near;
		return this;
	}

	public NerdBasicCameraBuilder setMouseZ(final float p_mouseZ) {
		this.BUILD.mouseZ = p_mouseZ;
		return this;
	}
	// endregion

	public NerdBasicCameraBuilder setAutoAspect(final boolean p_doAutoAspect) {
		this.BUILD.doAutoAspect = p_doAutoAspect;
		return this;
	}

	// region Projection settings.
	public NerdBasicCameraBuilder useOrtho() {
		this.BUILD.projection = PConstants.ORTHOGRAPHIC;
		return this;
	}

	public NerdBasicCameraBuilder usePerspective() {
		this.BUILD.projection = PConstants.PERSPECTIVE;
		return this;
	}
	// endregion

}
