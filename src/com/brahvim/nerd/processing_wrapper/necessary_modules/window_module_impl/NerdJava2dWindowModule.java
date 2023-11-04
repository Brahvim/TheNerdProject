package com.brahvim.nerd.processing_wrapper.necessary_modules.window_module_impl;

import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdWindowModule;

import processing.awt.PSurfaceAWT;
import processing.core.PVector;

public class NerdJava2dWindowModule extends NerdWindowModule {

	protected JFrame window;
	protected PSurfaceAWT.SmoothCanvas canvas;

	public NerdJava2dWindowModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	// region Getters.
	@Override
	public String getName() {
		return this.window.getName();
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
		// Faster!.... *wait, really?*
		return this.window.getLocation();
	}

	@Override
	public PVector getSizeAsPVector() {
		return new PVector(this.window.getWidth(), this.window.getHeight());
	}

	// public Image getIcon() {
	// return this.window.getIconImage();
	// }

	@Override
	public PVector getPositionAsPVector() {
		return new PVector(this.window.getX(), this.window.getY());
	}

	@Override
	public Object getNativeObject() {
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
	public NerdJava2dWindowModule setName(final String p_name) {
		this.window.setName(p_name);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final PVector p_size) {
		this.window.setSize((int) p_size.x, (int) p_size.y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setResizable(final boolean p_state) {
		this.window.setResizable(p_state);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final float p_x, final float p_y) {
		this.window.setSize((int) p_x, (int) p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final int p_x, final int p_y) {
		this.window.setSize(p_x, p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final PVector p_position) {
		this.window.setLocation((int) p_position.x, (int) p_position.y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final int p_x, final int p_y) {
		this.window.setLocation(p_x, p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final float p_x, final float p_y) {
		this.window.setLocation((int) p_x, (int) p_y);
		return this;
	}

	/**
	 * @return Whether or not the operation was successful.
	 */
	@Override
	public boolean setAlwaysOnTop(final boolean p_name) {
		if (!this.window.isAlwaysOnTopSupported())
			return false;

		// this.window.removeNotify();
		this.window.setAlwaysOnTop(true);
		// this.window.addNotify();

		return true;
	}

	public NerdJava2dWindowModule setIcon(final Image p_image) {
		this.window.setIconImage(p_image);
		return this;
	}
	// endregion

	@Override
	protected void preSetupImpl() {
		super.sketchSurface.setIcon(super.getIconImage());
		this.canvas = (PSurfaceAWT.SmoothCanvas) super.sketchSurface.getNative();
		this.window = (JFrame) canvas.getFrame();

		if (super.SKETCH.SKETCH_SETTINGS.INITIALLY_RESIZABLE)
			this.window.setResizable(true);
	}

	@Override
	protected void postImpl() {
		// Fullscreen?
		// https://stackoverflow.com/a/11570414/

		if (this.pfullscreen != this.fullscreen) {
			this.window.removeNotify();

			this.window.setVisible(false);
			while (this.window.isDisplayable())
				;

			if (this.fullscreen) {
				// If `super.SKETCH.display*` are set to the actual (AWT) ones:

				// this.window.setLocation(-7, -30);
				// this.window.setSize(super.SKETCH.displayWidth - 354,
				// super.SKETCH.displayHeight - 270);

				// Though these are arbitrary numbers, they work cross-platform, surprisingly!:

				// this.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				this.window.setLocation(-8, -30);
				this.window.setSize(super.SKETCH.displayWidth + 15, super.SKETCH.displayHeight);
				this.window.setUndecorated(true);
			} else {
				this.centerWindow();
				this.window.setSize(super.SKETCH.SKETCH_SETTINGS.INIT_WIDTH,
						super.SKETCH.SKETCH_SETTINGS.INIT_HEIGHT);
				this.window.setUndecorated(false);
			}

			this.window.setVisible(true);
			this.window.addNotify();
		}

		if (this.cursorVisible)
			super.SKETCH.cursor();
		else
			super.SKETCH.noCursor();
	}

	@Override
	protected void centerWindowImpl() {
		final Point dimensions = super.displays.getCurrentMonitorDimensions();
		super.sketchSurface.setLocation(
				(int) (dimensions.x * 0.5f - super.width),
				(int) (dimensions.y * 0.5f - super.q3y));
	}

}
