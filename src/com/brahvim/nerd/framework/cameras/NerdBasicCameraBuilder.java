package com.brahvim.nerd.framework.cameras;

import java.util.function.Consumer;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class NerdBasicCameraBuilder {

	private final NerdSketch SKETCH;
	private final NerdBasicCamera BUILD;

	public NerdBasicCameraBuilder(final NerdSketch p_sketch, final PGraphics p_graphics) {
		this(new NerdGenericGraphics(p_sketch, p_graphics));
	}

	public NerdBasicCameraBuilder(final NerdGenericGraphics p_graphics) {
		this.SKETCH = p_graphics.getSketch(); // Used by `setClearColor()`.
		this.BUILD = new NerdBasicCamera(p_graphics);

		// region My defaults:
		// this.defaultCamUp = new PVector(0, 1, 0);

		// this.defaultCamPos = new PVector(
		// this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f,
		// 600);

		// this.defaultCamCenter = new PVector(
		// this.parentSketch.INIT_WIDTH * 0.5f, this.parentSketch.INIT_HEIGHT * 0.5f,
		// 0);
		// endregion

	}

	public NerdBasicCamera build() {
		return this.BUILD;
	}

	// region Vectors.
	// region Default ones.
	// region Default up vector.
	public NerdBasicCameraBuilder setDefaultUp(final float p_x, final float p_y) {
		this.BUILD.defaultCamUp.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultUp(final float p_x, final float p_y, final float p_z) {
		this.BUILD.defaultCamUp.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultUp(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.defaultCamUp.set(p_vec);
		return this;
	}
	// endregion

	// region Default position vector.
	public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y) {
		this.BUILD.defaultCamPos.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultPos(final float p_x, final float p_y, final float p_z) {
		this.BUILD.defaultCamPos.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultPos(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.defaultCamPos.set(p_vec);
		return this;
	}
	// endregion

	// region Default center vector.
	public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y) {
		this.BUILD.defaultCamCenter.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultCenter(final float p_x, final float p_y, final float p_z) {
		this.BUILD.defaultCamCenter.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setDefaultCenter(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.defaultCamCenter.set(p_vec);
		return this;
	}
	// endregion
	// endregion

	// region Dynamic ones.
	// region Dynamic up vector.
	public NerdBasicCameraBuilder setUp(final float p_x, final float p_y) {
		this.BUILD.up.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setUp(final float p_x, final float p_y, final float p_z) {
		this.BUILD.up.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setUp(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.up.set(p_vec);
		return this;
	}
	// endregion

	// region Dynamic position vector.
	public NerdBasicCameraBuilder setPos(final float p_x, final float p_y) {
		this.BUILD.pos.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setPos(final float p_x, final float p_y, final float p_z) {
		this.BUILD.pos.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setPos(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.pos.set(p_vec);
		return this;
	}
	// endregion

	// region Dynamic center vector.
	public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y) {
		this.BUILD.center.set(p_x, p_y);
		return this;
	}

	public NerdBasicCameraBuilder setCenter(final float p_x, final float p_y, final float p_z) {
		this.BUILD.center.set(p_x, p_y, p_z);
		return this;
	}

	public NerdBasicCameraBuilder setCenter(final PVector p_vec) {
		if (p_vec != null)
			this.BUILD.center.set(p_vec);
		return this;
	}
	// endregion
	// endregion
	// endregion

	// region `float` values.
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

	public NerdBasicCameraBuilder setScript(final boolean p_doScript) {
		this.BUILD.doScript = p_doScript;
		return this;
	}
	// endregion

	public NerdBasicCameraBuilder setScript(final Consumer<NerdAbstractCamera> p_script) {
		this.BUILD.script = p_script;
		return this;
	}

	// region `setClearColor()` overloads (they use `this.sketch`!).
	public NerdBasicCameraBuilder setClearColor(final int p_color) {
		this.BUILD.clearColorParam1 = this.SKETCH.red(p_color);
		this.BUILD.clearColorParam2 = this.SKETCH.green(p_color);
		this.BUILD.clearColorParam3 = this.SKETCH.blue(p_color);
		this.BUILD.clearColorParamAlpha = 255; // I have to do this!
		// this.alpha = this.SKETCH.alpha(p_color);
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

	public NerdBasicCameraBuilder setClearColor(final float p_v1, final float p_v2, final float p_v3,
			final float p_alpha) {
		this.BUILD.clearColorParam1 = p_v1;
		this.BUILD.clearColorParam2 = p_v2;
		this.BUILD.clearColorParam3 = p_v3;
		this.BUILD.clearColorParamAlpha = p_alpha;
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

}
