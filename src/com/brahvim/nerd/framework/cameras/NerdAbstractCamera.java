package com.brahvim.nerd.framework.cameras;

import java.util.Objects;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.window_management.NerdWindowModule;
import com.brahvim.nerd.window_management.window_module_impls.NerdGlWindowModule;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

// Declared as abstract because it is supposed to be extended everytime.
public abstract class NerdAbstractCamera {

	// region Fields.
	// region `public` fields.
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

	public final PVector // These should NEVER change!
	/*   */ POSITION, // What if somebody...
			ORIENTATION, // ...takes a reference to them?
			DEFAULT_POSITION, // ...and wants to rely on it?
			DEFAULT_ORIENTATION;

	// ...yeah, for some reason `PApplet::color()` fails. No ARGB!
	public float clearColorParam1, clearColorParam2, clearColorParam3, clearColorParamAlpha;
	public float fov = NerdAbstractCamera.DEFAULT_FOV,
			far = NerdAbstractCamera.DEFAULT_FAR,
			near = NerdAbstractCamera.DEFAULT_NEAR,
			mouseZ = NerdAbstractCamera.DEFAULT_MOUSE_Z,
			aspect /* `= 1`? */;
	public int projection = PConstants.PERSPECTIVE;

	public boolean
	/*   */ doAutoClear = true,
			doAutoAspect = true,
			doClearWithImage = true,
			doClearWithColors = true;
	// endregion

	protected final NerdP3dGraphics GRAPHICS;
	protected final NerdGlWindowModule WINDOW;
	protected final NerdSketch<PGraphics3D> SKETCH;

	protected PImage clearImage; // "Clear, Clear! Crystal-Clear!"
	// endregion

	@SuppressWarnings("unchecked")
	protected NerdAbstractCamera(final NerdSketch<PGraphics3D> p_sketch, final PGraphics3D p_graphics) {
		this.SKETCH = Objects.requireNonNull(p_sketch, "The parameter `p_sketch` was `null`!");
		this.WINDOW = (NerdGlWindowModule) this.SKETCH.getNerdModule(NerdWindowModule.class);
		this.GRAPHICS = new NerdP3dGraphics(this.SKETCH,
				Objects.requireNonNull(p_graphics, "The parameter `p_graphics` was `null`!"));

		this.POSITION = new PVector();
		this.DEFAULT_POSITION = new PVector();
		this.ORIENTATION = new PVector(
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
		this.ORIENTATION = new PVector(0, 1, 0);
		this.DEFAULT_ORIENTATION = new PVector(0, 1, 0);
	}

	@SuppressWarnings("unchecked")
	protected NerdAbstractCamera(final NerdAbstractCamera p_camera) {
		this.GRAPHICS = Objects.requireNonNull(p_camera,
				"The parameter `p_camera` was `null`!").GRAPHICS;
		this.SKETCH = this.GRAPHICS.getSketch();
		this.WINDOW = (NerdGlWindowModule) this.SKETCH.getNerdModule(NerdWindowModule.class);

		this.POSITION = p_camera.POSITION.copy();
		this.ORIENTATION = p_camera.ORIENTATION.copy();
		this.DEFAULT_POSITION = p_camera.DEFAULT_POSITION.copy();
		this.DEFAULT_ORIENTATION = p_camera.DEFAULT_ORIENTATION.copy();
	}

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

	public void setClearImage(final PImage p_image) {
		Objects.requireNonNull(p_image);
		this.clearImage = p_image;
	}

	public abstract void applyMatrix();

	public void applyProjection() {
		// Apply projection:
		switch (this.projection) {
			case PConstants.ORTHOGRAPHIC -> this.applyOrtho();
			case PConstants.PERSPECTIVE -> this.applyPerspective();

			default -> throw new UnsupportedOperationException(
					"`NerdCamera::projection` can only be either" +
							"`PConstants.PERSPECTIVE` or `PConstants.ORTHOGRAPHIC`.");
		}
	}

	public void clear() {
		// TODO: Investigate! Perhaps move this to `NerdAbstractGraphics`!
		// For some reason, swapping these conditions causes `DemoScene3` to crash when
		// a scene-switch occurs (only test scene using an image background).

		if (this.doClearWithImage) { // No `null`-bombs, right?!
			if (this.clearImage != null)
				// if (this.clearImage.width == this.GRAPHICS.width
				// && this.clearImage.height == this.GRAPHICS.height)
				this.GRAPHICS.background(this.clearImage); // GO!!!
		} else if (this.doClearWithColors) { // Clear with colors.6
			this.GRAPHICS.background(
					this.clearColorParam1, this.clearColorParam2,
					this.clearColorParam3, this.clearColorParamAlpha);
		}
	}

	public void apply() {
		if (this.doAutoAspect)
			// It probably is faster not to perform this check.
			// if (!(this.SKETCH.pwidth == this.SKETCH.width
			// || this.SKETCH.pheight == this.SKETCH.height))
			// A simple divide instruction is enough!
			this.aspect = this.GRAPHICS.scr;

		if (this.doAutoClear)
			this.clear();

		this.applyMatrix();
		this.applyProjection();
	}

	public void reset() {
		// region Parameters and `NerdAbstractCamera`-only vectors.
		this.clearColorParam1 = 0;
		this.clearColorParam2 = 0;
		this.clearColorParam3 = 0;
		this.clearColorParamAlpha = 255;

		this.POSITION.set(this.DEFAULT_POSITION);
		this.ORIENTATION.set(this.DEFAULT_ORIENTATION);
		// endregion

		// region Settings.
		this.doAutoClear = true;
		this.projection = PConstants.PERSPECTIVE;
		this.far = NerdAbstractCamera.DEFAULT_FAR;
		this.fov = NerdAbstractCamera.DEFAULT_FOV;
		this.near = NerdAbstractCamera.DEFAULT_NEAR;
		this.mouseZ = NerdAbstractCamera.DEFAULT_MOUSE_Z;
		// endregion
	}
	// endregion

}
