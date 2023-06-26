package com.brahvim.nerd.framework.cameras;

import java.util.Objects;
import java.util.function.Consumer;

import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

// Declared as abstract because it is supposed to be extended everytime.
public abstract class NerdAbstractCamera {

	// region Fields.
	public static final float DEFAULT_CAM_FOV = PApplet.radians(60),
			DEFAULT_CAM_NEAR = 0.05f, DEFAULT_CAM_FAR = 10_000, DEFAULT_CAM_MOUSE_Z = 1;

	public final NerdSketch SKETCH;
	public Consumer<NerdAbstractCamera> script; // Smart users will write complete classes for these.

	// ...yeah, for some reason `PApplet::color()` fails.
	public float clearColorParam1, clearColorParam2, clearColorParam3, clearColorParamAlpha;
	public float fov = NerdAbstractCamera.DEFAULT_CAM_FOV,
			far = NerdAbstractCamera.DEFAULT_CAM_FAR,
			near = NerdAbstractCamera.DEFAULT_CAM_NEAR,
			mouseZ = NerdAbstractCamera.DEFAULT_CAM_MOUSE_Z,
			aspect /* `= 1`? */;

	protected PVector pos = new PVector(), up = new PVector(0, 1, 0),
			defaultCamUp, defaultCamPos;

	public int projection = PConstants.PERSPECTIVE;
	public boolean doScript = true, doAutoClear = true, doAutoAspect = true;
	// endregion

	protected NerdAbstractCamera(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public void applyMatrix(final NerdGraphics p_graphics) {
		this.applyMatrix(p_graphics.getUnderlyingBuffer());
	}

	public abstract void applyMatrix(final PGraphics p_graphics);

	public void applyProjection(final NerdGraphics p_graphics) {
		this.applyProjection(p_graphics.getUnderlyingBuffer());
	}

	public void applyProjection(final PGraphics p_graphics) {
		if (!PConstants.P3D.equals(this.SKETCH.RENDERER))
			return;

		if (this.doAutoAspect)
			// It probably is faster not to perform this check.
			// if (!(this.SKETCH.pwidth == this.SKETCH.width
			// || this.SKETCH.pheight == this.SKETCH.height))
			// A simple divide instruction is enough!
			this.aspect = (float) this.SKETCH.width / (float) this.SKETCH.height;

		// Apply projection:
		switch (this.projection) {
			case PConstants.PERSPECTIVE -> p_graphics.perspective(
					this.fov, this.aspect, this.near, this.far);

			case PConstants.ORTHOGRAPHIC -> p_graphics.ortho(
					-this.SKETCH.WINDOW.cx, this.SKETCH.WINDOW.cx,
					-this.SKETCH.WINDOW.cy, this.SKETCH.WINDOW.cy,
					this.near, this.far);

			default -> throw new UnsupportedOperationException(
					"`NerdCamera::projection` can only be either" +
							"`PConstants.PERSPECTIVE` or `PConstants.ORTHOGRAPHIC`.");
		}
	}

	// region Pre-implemented methods.
	// region Getters and setters.
	public PVector getUp() {
		return this.up;
	}

	public PVector getPos() {
		return this.pos;
	}

	public void setUp(final PVector p_up) {
		this.up = p_up;
	}

	public void setPos(final PVector p_pos) {
		this.pos = p_pos;
	}
	// endregion

	public void apply(final NerdGraphics p_graphics) {
		this.apply(p_graphics.getUnderlyingBuffer());
	}

	public void apply(final PGraphics p_graphics) {
		// #JIT_FTW!:

		this.clear(p_graphics);
		this.runScript();
		this.applyMatrix(p_graphics);
	}

	// TODO: Fix whatever this reminds you of. PAIN! x[
	public void clear(final PGraphics p_graphics) {
		this.SKETCH.alphaBg(
				this.clearColorParam1, this.clearColorParam2,
				this.clearColorParam3, this.clearColorParamAlpha);
	}

	public void completeReset() {
		// region Parameters and `NerdAbstractCamera`-only vectors.
		this.clearColorParam1 = 0;
		this.clearColorParam2 = 0;
		this.clearColorParam3 = 0;
		this.clearColorParamAlpha = 255;

		if (this.defaultCamUp == null)
			this.up.set(0, 1, 0);
		else
			this.up.set(this.defaultCamUp);

		if (this.defaultCamPos == null)
			this.pos.set(0, 0, 0);
		else
			this.pos.set(this.defaultCamPos);
		// endregion

		// region Settings.
		this.projection = PConstants.PERSPECTIVE;
		this.doScript = true;
		this.doAutoClear = true;
		this.far = NerdAbstractCamera.DEFAULT_CAM_FAR;
		this.fov = NerdAbstractCamera.DEFAULT_CAM_FOV;
		this.near = NerdAbstractCamera.DEFAULT_CAM_NEAR;
		this.mouseZ = NerdAbstractCamera.DEFAULT_CAM_MOUSE_Z;
		// endregion
	}

	public void runScript() {
		if (this.script != null && this.doScript)
			this.script.accept(this);
	}

	public PVector getDefaultCamUp() {
		return this.defaultCamPos;
	}

	public PVector getDefaultCamPos() {
		return this.defaultCamPos;
	}

	public void setDefaultCamUp(final PVector p_vec) {
		this.defaultCamUp = Objects.requireNonNull(p_vec);
	}

	public void setDefaultCamPos(final PVector p_vec) {
		this.defaultCamPos = Objects.requireNonNull(p_vec);
	}

	// region `setClearColor()` overloads.
	public void setClearColor(final int p_color) {
		this.clearColorParam1 = this.SKETCH.red(p_color);
		this.clearColorParam2 = this.SKETCH.green(p_color);
		this.clearColorParam3 = this.SKETCH.blue(p_color);
		this.clearColorParamAlpha = 255; // I have to do this!
		// this.alpha = this.SKETCH.alpha(p_color);
	}

	public void setClearColor(final float p_grey, final float p_alpha) {
		this.clearColorParam1 = p_grey;
		this.clearColorParam2 = p_grey;
		this.clearColorParam3 = p_grey;
		this.clearColorParamAlpha = p_alpha;
	}

	public void setClearColor(final float p_v1, final float p_v2, final float p_v3) {
		this.clearColorParam1 = p_v1;
		this.clearColorParam2 = p_v2;
		this.clearColorParam3 = p_v3;
		this.clearColorParamAlpha = 255;
	}

	public void setClearColor(final float p_v1, final float p_v2, final float p_v3, final float p_alpha) {
		this.clearColorParam1 = p_v1;
		this.clearColorParam2 = p_v2;
		this.clearColorParam3 = p_v3;
		this.clearColorParamAlpha = p_alpha;
	}
	// endregion
	// endregion

}
