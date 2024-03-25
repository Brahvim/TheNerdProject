package com.brahvim.nerd.framework.cameras;

import java.util.Objects;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.window_management.NerdWindowModule;
import com.brahvim.nerd.window_management.window_module_impls.NerdGlWindowModule;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

// Declared as abstract because it is supposed to be extended everytime.
public abstract class NerdAbstractCamera {

	// region Fields.
	// region `public` fields.
	// region `static` fields.
	public static final float
	/*   */ DEFAULT_FOV = PApplet.radians(60),
			DEFAULT_NEAR = 0.1f,
			DEFAULT_FAR = 10_000,
			DEFAULT_MOUSE_Z = 1;

	public static final float
	/*   */ DEFAULT_POSITION_X = 0,
			DEFAULT_POSITION_Y = 0,
			DEFAULT_POSITION_Z = 0,
			// "Up", a.k.a "orientation" vector:
			DEFAULT_ORIENTATION_X = 0,
			DEFAULT_ORIENTATION_Y = 1,
			DEFAULT_ORIENTATION_Z = 0;
	// endregion

	// These should never point to a different object!
	// What if somebody took a reference to them and wanted to *rely* on it!?:
	public final PVector
	/*   */ POSITION,
			ORIENTATION,
			DEFAULT_POSITION,
			DEFAULT_ORIENTATION;

	public float
	/*   */ aspect /* `= 1`? */,
			fov = NerdAbstractCamera.DEFAULT_FOV,
			far = NerdAbstractCamera.DEFAULT_FAR,
			near = NerdAbstractCamera.DEFAULT_NEAR,
			mouseZ = NerdAbstractCamera.DEFAULT_MOUSE_Z;

	public boolean doAutoAspect = true;
	public int projection = PConstants.PERSPECTIVE;
	// endregion

	protected final NerdP3dGraphics GRAPHICS;
	protected final NerdGlWindowModule WINDOW;
	protected final NerdSketch<PGraphics3D> SKETCH;
	// endregion

	// region Constructors.
	@SuppressWarnings("unchecked")
	protected NerdAbstractCamera(final NerdSketch<PGraphics3D> p_sketch, final PGraphics3D p_graphics) {
		this.SKETCH = Objects.requireNonNull(p_sketch, "The parameter `p_sketch` was `null`!");
		this.WINDOW = (NerdGlWindowModule) this.SKETCH.getNerdModule(NerdWindowModule.class);
		this.GRAPHICS = new NerdP3dGraphics(this.SKETCH,
				Objects.requireNonNull(p_graphics, "The parameter `p_graphics` was `null`!"));

		this.POSITION = new PVector();
		this.DEFAULT_POSITION = new PVector();
		this.ORIENTATION = new PVector( // Yes, this represents the "up" vector.
				NerdAbstractCamera.DEFAULT_ORIENTATION_X,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Y,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Z);
		this.DEFAULT_ORIENTATION = new PVector( // Yes, these are meant to be the exact same.
				NerdAbstractCamera.DEFAULT_ORIENTATION_X,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Y,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Z);
	}

	@SuppressWarnings("unchecked")
	protected NerdAbstractCamera(final NerdP3dGraphics p_graphics) {
		this.GRAPHICS = Objects.requireNonNull(p_graphics, "The parameter `p_graphics` was `null`!");
		this.SKETCH = this.GRAPHICS.getSketch();
		this.WINDOW = (NerdGlWindowModule) this.SKETCH.getNerdModule(NerdWindowModule.class);

		this.POSITION = new PVector();
		this.DEFAULT_POSITION = new PVector();
		this.ORIENTATION = new PVector( // Yes, this represents the "up" vector.
				NerdAbstractCamera.DEFAULT_ORIENTATION_X,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Y,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Z);
		this.DEFAULT_ORIENTATION = new PVector( // Yes, these are meant to be the exact same.
				NerdAbstractCamera.DEFAULT_ORIENTATION_X,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Y,
				NerdAbstractCamera.DEFAULT_ORIENTATION_Z);
	}

	@SuppressWarnings("unchecked")
	protected NerdAbstractCamera(final NerdAbstractCamera p_camera) {
		this.GRAPHICS = Objects.requireNonNull(p_camera).GRAPHICS;
		this.SKETCH = this.GRAPHICS.getSketch();
		this.WINDOW = (NerdGlWindowModule) this.SKETCH.getNerdModule(NerdWindowModule.class);

		this.POSITION = p_camera.POSITION.copy();
		this.ORIENTATION = p_camera.ORIENTATION.copy();
		this.DEFAULT_POSITION = p_camera.DEFAULT_POSITION.copy();
		this.DEFAULT_ORIENTATION = p_camera.DEFAULT_ORIENTATION.copy();
	}
	// endregion

	protected void applyOrtho() {
		this.GRAPHICS.ortho(
				-this.GRAPHICS.cx, this.GRAPHICS.cx,
				-this.GRAPHICS.cy, this.GRAPHICS.cy,
				this.near, this.far);
	}

	protected void applyPerspective() {
		this.GRAPHICS.perspective(
				this.fov, this.aspect,
				this.near, this.far);
	}

	// region `public` methods.
	public abstract void applyMatrix();

	public void applyProjection() {
		// Apply projection:
		switch (this.projection) {
			case PConstants.ORTHOGRAPHIC -> this.applyOrtho();
			case PConstants.PERSPECTIVE -> this.applyPerspective();
			// default -> throw new UnsupportedOperationException(
			// "`NerdCamera::projection` can only be either" +
			// "`PConstants.PERSPECTIVE` or `PConstants.ORTHOGRAPHIC`.");
		}
	}

	public void apply() {
		if (this.doAutoAspect)
			// It probably is faster not to perform this check.
			// if (!(this.SKETCH.pwidth == this.SKETCH.width
			// || this.SKETCH.pheight == this.SKETCH.height))
			// A simple divide instruction is enough!
			this.aspect = this.GRAPHICS.scr;

		this.applyMatrix();
		this.applyProjection();
	}

	public void reset() {
		this.POSITION.set(this.DEFAULT_POSITION);
		this.ORIENTATION.set(this.DEFAULT_ORIENTATION);

		// region Settings.
		this.projection = PConstants.PERSPECTIVE;
		this.far = NerdAbstractCamera.DEFAULT_FAR;
		this.fov = NerdAbstractCamera.DEFAULT_FOV;
		this.near = NerdAbstractCamera.DEFAULT_NEAR;
		this.mouseZ = NerdAbstractCamera.DEFAULT_MOUSE_Z;
		// endregion
	}
	// endregion

}
