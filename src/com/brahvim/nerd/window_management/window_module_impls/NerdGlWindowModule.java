package com.brahvim.nerd.window_management.window_module_impls;

import java.awt.Point;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.window_management.NerdWindowModule;
import com.jogamp.newt.opengl.GLWindow;

import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class NerdGlWindowModule extends NerdWindowModule<PGraphicsOpenGL> {

	protected GLWindow window;

	public NerdGlWindowModule(final NerdSketch<PGraphicsOpenGL> p_sketch) {
		super(p_sketch);
	}

	// region Getters.
	@Override
	public String getName() {
		return this.window.getTitle();
	}

	@Override
	public int getX() {
		return this.window.getX();
	}

	@Override
	public int getY() {
		return this.window.getY();
	}

	@Override
	public int getWidth() {
		return this.window.getWidth();
	}

	@Override
	public int getHeight() {
		return this.window.getHeight();
	}

	@Override
	public Point getSize() {
		return new Point(this.window.getWidth(), this.window.getHeight());
	}

	@Override
	public Point getPosition() {
		return new Point(this.window.getX(), this.window.getY());
	}

	@Override
	public PVector getSizeAsPVector() {
		return new PVector(this.window.getWidth(), this.window.getHeight());
	}

	@Override
	public PVector getPositionAsPVector() {
		return new PVector(this.window.getX(), this.window.getY());
	}

	@Override
	public GLWindow getNativeObject() {
		return this.window;
	}

	@Override
	public boolean getAlwaysOnTop() {
		return this.window.isAlwaysOnTop();
	}

	@Override
	public boolean isResizable() {
		return this.window.isResizable();
	}
	// endregion

	// region Setters.
	@Override
	public NerdGlWindowModule setName(final String p_name) {
		this.window.setTitle(p_name);
		return this;
	}

	@Override
	public NerdGlWindowModule setSize(final PVector p_size) {
		this.window.setSize((int) p_size.x, (int) p_size.y);
		return this;
	}

	@Override
	public NerdGlWindowModule setSize(final int p_x, final int p_y) {
		this.window.setSize(p_x, p_y);
		return this;
	}

	@Override
	public NerdGlWindowModule setResizable(final boolean p_state) {
		while (this.window.isResizable() != p_state)
			this.window.setResizable(p_state);

		return this;
	}

	@Override
	public NerdGlWindowModule setSize(final float p_x, final float p_y) {
		this.window.setSize((int) p_x, (int) p_y);
		return this;
	}

	@Override
	public NerdGlWindowModule setPosition(final float p_x, final float p_y) {
		this.window.setPosition((int) p_x, (int) p_y);
		return this;
	}

	@Override
	public NerdGlWindowModule setPosition(final int p_x, final int p_y) {
		this.window.setPosition(p_x, p_y);
		return this;
	}

	@Override
	public NerdGlWindowModule setPosition(final PVector p_position) {
		this.window.setPosition((int) p_position.x, (int) p_position.y);
		return this;
	}

	@Override
	public boolean setAlwaysOnTop(final boolean p_status) {
		this.window.setAlwaysOnTop(p_status);
		while (!this.window.isAlwaysOnTop())
			;
		return true;
	}
	// endregion

	@Override
	protected void preSetupImpl() {
		this.window = (GLWindow) super.sketchSurface.getNative();

		// Left to remain in `NerdSketch::setup()` due to `ThreadDeath` issues.
		// if (super.SKETCH.canResize)
		// this.window.setResizable(true);

		// `NerdSketch::setup()` does not need this check at all!:
		// while (!this.window.isResizable())
		// ;
	}

	@Override
	public void preImpl() { // Slowdowns? Maybe remove this?
		this.window = (GLWindow) super.sketchSurface.getNative();
	}

	@Override
	protected void postImpl() {
		if (this.pfullscreen != this.fullscreen) {
			this.window.setFullscreen(this.fullscreen);

			/*
			 * Wait for the window to change its mode.
			 * Don't wait for more than `5000` milliseconds!:
			 * ...yes, that should crash the program :|
			 * (It didn't, during my tests, surprisingly :O
			 * The window just... waited there and didn't change states O_O
			 * ...and then Processing began rendering again :D
			 * Apparently `setFullscreen()` returns a `boolean`, meaning that it does
			 * error-checking! Quite kind of JogAmp!)
			 */

			// region Older logic (no time checking!).
			// while (this.fullscreen ? !this.window.isFullscreen() :
			// this.window.isFullscreen())
			// ;
			// endregion

			final long fsStartMillis = System.currentTimeMillis();

			while (this.fullscreen != this.window.isFullscreen())
				if (System.currentTimeMillis() - fsStartMillis > 5000)
					break; // Throw an exception instead?

		}

		// I knew this already, but you may want to check out:
		// http://twicetwo.com/blog/processing/2016/03/01/processing-locking-the-mouse.html

		if (this.cursorConfined != this.pcursorConfined)
			this.window.confinePointer(this.cursorConfined);
		while (this.cursorConfined ? !this.window.isPointerConfined() : this.window.isPointerConfined())
			;

		if (this.cursorVisible != this.pcursorVisible) {
			this.window.setPointerVisible(this.cursorVisible);
			while (this.cursorVisible ? !this.window.isPointerVisible() : this.window.isPointerVisible())
				;
		}
	}

	@Override
	protected void centerWindowImpl() {
		final Point dimensions = super.displays.getCurrentMonitorDimensions();
		this.window.setPosition(
				(int) ((dimensions.x * 0.5f) - super.cx),
				(int) ((dimensions.y * 0.5f) - super.cy));
	}

}
