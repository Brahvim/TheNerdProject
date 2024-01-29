package com.brahvim.nerd.framework.cameras;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdBasicCameraBuilder {

	private final NerdBasicCamera BUILD;
	private final NerdSketch<PGraphics3D> SKETCH;

	// This order of dependence between these constructors ensures
	// that any `NerdSketch<PGraphics3D>` implementation can be used;
	// ...not just `NerdP3dGraphics`!
	public NerdBasicCameraBuilder(final NerdP3dGraphics p_graphics) {
		this.SKETCH = p_graphics.getSketch();
		this.BUILD = new NerdBasicCamera(p_graphics);
	}

	public NerdBasicCameraBuilder(final NerdSketch<PGraphics3D> p_sketch, final PGraphics3D p_graphics) {
		this(new NerdP3dGraphics(p_sketch, p_graphics));
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

	public NerdBasicCameraBuilder setNear(final float p_fov) {
		this.BUILD.near = p_fov;
		return this;
	}

	public NerdBasicCameraBuilder setMouseZ(final float p_mouseZ) {
		this.BUILD.mouseZ = p_mouseZ;
		return this;
	}
	// endregion

	// region `boolean`s.
	public NerdBasicCameraBuilder setAutoClear(final boolean p_doAutoClear) {
		this.BUILD.doAutoClear = p_doAutoClear;
		return this;
	}

	public NerdBasicCameraBuilder setAutoAspect(final boolean p_doAutoAspect) {
		this.BUILD.doAutoAspect = p_doAutoAspect;
		return this;
	}

	public NerdBasicCameraBuilder setClearWithImage(final boolean p_clearWithImage) {
		this.BUILD.doClearWithImage = p_clearWithImage;
		return this;
	}
	// endregion

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

	// region `setClearColor()` overloads (they use `this::SKETCH`).
	public NerdBasicCameraBuilder setClearColor(final int p_color) {
		this.BUILD.clearColorParam1 = this.SKETCH.red(p_color);
		this.BUILD.clearColorParam2 = this.SKETCH.green(p_color);
		this.BUILD.clearColorParam3 = this.SKETCH.blue(p_color);
		this.BUILD.clearColorParamAlpha = 255; // RGB colors have a complete alpha, duh.
		return this;
	}

	public NerdBasicCameraBuilder setClearColor(final float p_grey, final float p_alpha) {
		this.BUILD.clearColorParam1 = p_grey;
		this.BUILD.clearColorParam2 = p_grey;
		this.BUILD.clearColorParam3 = p_grey;
		this.BUILD.clearColorParamAlpha = p_alpha;
		return this;
	}

	public NerdBasicCameraBuilder setClearColor(final float p_v1, final float p_v2, final float p_v3) {
		this.BUILD.clearColorParam1 = p_v1;
		this.BUILD.clearColorParam2 = p_v2;
		this.BUILD.clearColorParam3 = p_v3;
		this.BUILD.clearColorParamAlpha = 255;
		return this;
	}

	public NerdBasicCameraBuilder setClearColor(
			final float p_v1,
			final float p_v2,
			final float p_v3,
			final float p_alpha) {
		this.BUILD.clearColorParam1 = p_v1;
		this.BUILD.clearColorParam2 = p_v2;
		this.BUILD.clearColorParam3 = p_v3;
		this.BUILD.clearColorParamAlpha = p_alpha;
		return this;
	}
	// endregion

	public NerdBasicCameraBuilder setClearImage(final PImage p_image) {
		this.BUILD.clearImage = p_image;
		return this;
	}

}
