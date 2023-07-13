package com.brahvim.nerd.processing_wrapper;

import java.awt.Point;
import java.util.LinkedHashSet;

import com.brahvim.nerd.processing_callback_interfaces.hardware.window.NerdWindowListener;

import processing.core.PImage;
import processing.core.PSurface;
import processing.core.PVector;

public abstract class NerdWindowModule extends NerdModule {

	// region Fields.
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

	protected final LinkedHashSet<NerdWindowListener> windowListeners = new LinkedHashSet<>(1);

	protected PImage iconImage;
	protected PSurface sketchSurface;
	protected NerdDisplayModule displays;
	// endregion

	// region Construction and initialization.
	protected NerdWindowModule(final NerdSketch p_sketch) {
		super(p_sketch);
		this.fullscreen = super.SKETCH.SKETCH_SETTINGS.STARTED_FULLSCREEN;
	}

	@Override
	protected void preSetup() {
		final NerdSketch sketch = super.SKETCH;

		this.sketchSurface = sketch.getSurface();
		this.displays = sketch.getNerdModule(NerdDisplayModule.class);
		this.iconImage = sketch.loadImage(sketch.SKETCH_SETTINGS.ICON_PATH);

		this.preSetupImpl();
		this.updateWindowParameters();
	}

	protected abstract void preSetupImpl();
	// endregion

	// region Taking the window to the center.
	public void centerWindow() {
		// You called this function when the window changed its size or position, right?
		// Remember: Computers with multiple displays exist! We shouldn't cache this:
		this.updateWindowParameters();
		this.centerWindowImpl();

		// this.surface.setLocation(winX, winY);
		// (Well, changing the display does NOT effect those variables in any way :|)
	}

	protected abstract void centerWindowImpl();
	// endregion

	// region Updates!
	private void updateWindowParameters() {
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

	private void recordCurrentWindowParameters() {
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
	public abstract boolean setAlwaysOnTop(final boolean p_name);

	public abstract NerdWindowModule setName(final String p_name);

	public abstract NerdWindowModule setSize(final PVector p_size);

	public abstract NerdWindowModule setResizable(final boolean p_state);

	public abstract NerdWindowModule setSize(final int p_x, final int p_y);

	public abstract NerdWindowModule setSize(final float p_x, final float p_y);

	public abstract NerdWindowModule setPosition(final PVector p_position);

	public abstract NerdWindowModule setPosition(final int p_x, final int p_y);

	public abstract NerdWindowModule setPosition(final float p_x, final float p_y);
	// endregion

	// region Callbacks.
	@Override
	protected void pre() {
		this.recordCurrentWindowParameters();

		// Current state:
		this.width = super.SKETCH.width;
		this.height = super.SKETCH.height;
		// this.focused = super.SKETCH.focused; // Better handled in the `focus*()`!

		this.PREV_WINDOW_POSITION.set(this.WINDOW_POSITION);
		this.WINDOW_POSITION.set(this.getPositionAsPVector());

		// When the window is resized, do the following!:
		if (!(this.pwidth == this.width || this.pheight == this.height)) {
			this.updateWindowParameters();
			for (final NerdModule m : super.getModulesUpdatedFramely())
				m.fullscreenChanged(this.fullscreen);
		}
	}

	@Override
	protected void post() {
		this.postImpl();

		if (this.pfullscreen != this.fullscreen)
			for (final NerdModule m : super.getModulesUpdatedFramely())
				m.fullscreenChanged(this.fullscreen);

		this.pfullscreen = this.fullscreen;
		this.pcursorVisible = this.cursorVisible;
		this.pcursorConfined = this.cursorConfined;
	}

	@Override
	protected void focusGained() {
		// Copying, because the sketch performs a decision here.
		this.focused = super.SKETCH.focused;
	}

	@Override
	public void focusLost() {
		// Copying, because the sketch performs a decision here.
		this.focused = super.SKETCH.focused;
	}

	protected abstract void postImpl();
	// endregion

}
