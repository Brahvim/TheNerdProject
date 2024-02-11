package com.brahvim.nerd.window_management;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Set;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.useless_callback_interfaces.hardware.window.NerdWindowListener;
import com.brahvim.nerd.window_management.window_module_impls.NerdFx2dWindowModule;
import com.brahvim.nerd.window_management.window_module_impls.NerdGlWindowModule;
import com.brahvim.nerd.window_management.window_module_impls.NerdJava2dWindowModule;

import processing.awt.PGraphicsJava2D;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PSurface;
import processing.core.PVector;
import processing.javafx.PGraphicsFX2D;
import processing.opengl.PGraphicsOpenGL;

/**
 * Please use {@linkplain NerdWindowModule#createWindowModule(NerdSketch)
 * NerdWindowModule::createWindowModule(NerdSketch)} to create instances
 * specific to your {@link NerdSketch}'s renderer.
 */
public abstract class NerdWindowModule<SketchPGraphicsT extends PGraphics> extends NerdModule<SketchPGraphicsT> {

	// region Instance fields.
	/** Position of the window relative to the monitor. */
	public final PVector WINDOW_POSITION = new PVector(), PREV_WINDOW_POSITION = new PVector();

	public boolean pfocused, focused;
	public boolean fullscreen, pfullscreen;
	public boolean cursorConfined, cursorVisible = true;
	public boolean pcursorConfined, pcursorVisible = true;

	// Windows dimensions for the current and previous frames:
	public float pdbx, pdby, pcx, pcy, pqx, pqy, pq3x, pq3y, pscr;
	public float dbx, dby, cx, cy, qx, qy, q3x, q3y, scr;
	public int width, height, pwidth, pheight;

	protected final Set<NerdWindowListener> windowListeners = new LinkedHashSet<>(1);

	protected PImage iconImage;
	protected PSurface sketchSurface;
	protected NerdDisplayModule<SketchPGraphicsT> displays;
	// endregion

	// region Construction and initialization.
	protected NerdWindowModule(final NerdSketch<SketchPGraphicsT> p_sketch) {
		super(p_sketch);
		this.fullscreen = super.SKETCH.SKETCH_SETTINGS.shouldStartFullscreen;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void preSetup() {
		this.sketchSurface = super.SKETCH.getSurface();
		this.displays = super.SKETCH.getNerdModule(NerdDisplayModule.class);
		this.iconImage = super.SKETCH.loadImage(super.SKETCH.SKETCH_SETTINGS.windowIconPath);

		this.preSetupImpl();
		this.recordAndUpdateWindowParameters();
	}

	protected void preSetupImpl() {
	}
	// endregion

	public static <RetModulePGraphicsT extends PGraphics>
	/*   */ NerdWindowModule<RetModulePGraphicsT> createWindowModule(final NerdSketch<RetModulePGraphicsT> p_sketch) {
		final NerdWindowModule<RetModulePGraphicsT>
		/*   */ toRet = NerdWindowModule.supplyWindowModuleForSketch(p_sketch);

		if (toRet == null)
			throw new IllegalArgumentException(
					"Please write your own `PGraphics` subclass for this one!");

		return toRet;
	}

	@SuppressWarnings("unchecked")
	protected static <RetGraphicsT extends PGraphics>
	/*   */ NerdWindowModule<RetGraphicsT> supplyWindowModuleForSketch(final NerdSketch<RetGraphicsT> p_sketch) {
		return (NerdWindowModule<RetGraphicsT>) switch (p_sketch.getRendererPConstantString()) {
			case PConstants.P2D -> new NerdGlWindowModule((NerdSketch<PGraphicsOpenGL>) p_sketch);
			case PConstants.P3D -> new NerdGlWindowModule((NerdSketch<PGraphicsOpenGL>) p_sketch);
			case PConstants.PDF -> new NerdJava2dWindowModule((NerdSketch<PGraphicsJava2D>) p_sketch);
			case PConstants.SVG -> new NerdJava2dWindowModule((NerdSketch<PGraphicsJava2D>) p_sketch);
			case PConstants.FX2D -> new NerdFx2dWindowModule((NerdSketch<PGraphicsFX2D>) p_sketch);
			case PConstants.JAVA2D -> new NerdJava2dWindowModule((NerdSketch<PGraphicsJava2D>) p_sketch);
			default -> null;
		};
	}

	// region Taking the window to the center.
	public void centerWindow() {
		// You called this function when the window changed its size or position, right?
		// Remember: Computers with multiple displays exist! We shouldn't cache this:
		this.recordAndUpdateWindowParameters();
		this.centerWindowImpl();

		// this.surface.setLocation(winX, winY);
		// (Well, changing the display does NOT effect those variables in any way :|)
	}

	protected abstract void centerWindowImpl();
	// endregion

	// region Updates!
	protected final void recordAndUpdateWindowParameters() {
		this.recordCurrentWindowParameters();
		this.updateWindowParameters();
	}

	protected final void updateWindowParameters() {
		this.width = this.getWidth();
		this.height = this.getHeight();

		this.dbx = this.width * 2.0f;
		this.dby = this.height * 2.0f;

		this.cx = this.width * 0.5f;
		this.cy = this.height * 0.5f;

		this.qx = this.cx * 0.5f;
		this.qy = this.cy * 0.5f;

		this.q3x = this.cx + this.qx;
		this.q3y = this.cy + this.qy;

		this.scr = (float) this.width / (float) this.height;
	}

	protected final void recordCurrentWindowParameters() {
		this.pwidth = this.width;
		this.pheight = this.height;
		this.pfocused = this.focused;

		this.pdbx = this.dbx;
		this.pdby = this.dby;

		this.pcx = this.cx;
		this.pcy = this.cy;

		this.pqx = this.qx;
		this.pqy = this.qy;

		this.pq3x = this.q3x;
		this.pq3y = this.q3y;

		this.pscr = this.scr;
	}
	// endregion

	// region Getters.
	public PSurface getSketchSurface() {
		return this.sketchSurface;
	}

	public PImage getIconImage() {
		return this.iconImage;
	}

	public abstract String getName();

	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract Point getSize();

	public abstract PVector getSizeAsPVector();

	public abstract Point getPosition();

	public abstract PVector getPositionAsPVector();

	public abstract Object getNativeObject();

	public abstract boolean isResizable();

	public abstract boolean getAlwaysOnTop();
	// endregion

	// region Setters.
	// Implementations return pointers of their own type, not `NerdWindowModule*`s,
	public abstract boolean setAlwaysOnTop(final boolean p_state);

	public abstract NerdWindowModule<SketchPGraphicsT> setName(final String p_name);

	public abstract NerdWindowModule<SketchPGraphicsT> setSize(final PVector p_size);

	public abstract NerdWindowModule<SketchPGraphicsT> setResizable(final boolean p_state);

	public abstract NerdWindowModule<SketchPGraphicsT> setSize(final int p_x, final int p_y);

	public abstract NerdWindowModule<SketchPGraphicsT> setSize(final float p_x, final float p_y);

	public abstract NerdWindowModule<SketchPGraphicsT> setPosition(final PVector p_position);

	public abstract NerdWindowModule<SketchPGraphicsT> setPosition(final int p_x, final int p_y);

	public abstract NerdWindowModule<SketchPGraphicsT> setPosition(final float p_x, final float p_y);
	// endregion

	// region Callbacks.
	@Override
	public final void pre() {
		this.preImpl();
		this.recordCurrentWindowParameters(); // Somebody could change them! We gotta' record!

		// Current state:
		this.width = super.SKETCH.width;
		this.height = super.SKETCH.height;
		// this.focused = super.SKETCH.focused; // Better handled in the `focus*()`!

		this.PREV_WINDOW_POSITION.set(this.WINDOW_POSITION);
		this.WINDOW_POSITION.set(this.getPositionAsPVector());

		// When the window is resized, do the following!:
		if (!(this.pwidth == this.width || this.pheight == this.height)) {
			this.updateWindowParameters(); // *Our* updates occur here, and here only.
			super.SKETCH.fullscreenChanged(this.fullscreen);
		}
	}

	protected void preImpl() {
	}

	@Override
	public final void post() {
		this.postImpl();

		if (this.pfullscreen != this.fullscreen)
			super.SKETCH.fullscreenChanged(this.fullscreen);

		this.pfullscreen = this.fullscreen;
		this.pcursorVisible = this.cursorVisible;
		this.pcursorConfined = this.cursorConfined;
	}

	protected void postImpl() {
	}

	@Override
	public void focusGained() {
		// Copying, because the sketch performs a decision here.
		this.focused = super.SKETCH.focused;
	}

	@Override
	public void focusLost() {
		// Copying, because the sketch performs a decision here.
		this.focused = super.SKETCH.focused;
	}
	// endregion

}
