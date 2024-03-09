package com.brahvim.nerd.window_management.window_module_impls;

import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.awt.PGraphicsJava2D;
import processing.awt.PSurfaceAWT;
import processing.core.PImage;
import processing.core.PVector;

public class NerdJava2dWindowModule extends NerdWindowModule<PGraphicsJava2D> {

	protected JFrame frame;
	public boolean stayUndecorated = false;
	protected PSurfaceAWT.SmoothCanvas canvas;

	public NerdJava2dWindowModule(final NerdSketch<PGraphicsJava2D> p_sketch) {
		super(p_sketch);
	}

	// region Getters.
	@Override
	public String getName() {
		return this.frame.getName();
	}

	@Override
	public int getX() {
		return this.frame.getX();
	}

	@Override
	public int getY() {
		return this.frame.getY();
	}

	@Override
	public int getWidth() {
		return this.frame.getWidth();
	}

	@Override
	public int getHeight() {
		return this.frame.getHeight();
	}

	@Override
	public Point getSize() {
		return new Point(this.frame.getWidth(), this.frame.getHeight());
	}

	@Override
	public Point getPosition() {
		// Faster!.... *wait, really?*
		return this.frame.getLocation();
	}

	@Override
	public PVector getSizeAsPVector() {
		return new PVector(this.frame.getWidth(), this.frame.getHeight());
	}

	public Image getIcon() {
		return this.frame.getIconImage();
	}

	public PImage getIconAsPImage() {
		// Not caching this since people might just hack in and change the image on the
		// `JFrame` without us knowing!:
		return new PImage(this.getIcon());
	}

	@Override
	public PVector getPositionAsPVector() {
		return new PVector(this.frame.getX(), this.frame.getY());
	}

	@Override
	public JFrame getNativeObject() {
		return this.frame;
	}

	@Override
	public boolean getAlwaysOnTop() {
		return this.frame.isAlwaysOnTop();
	}

	@Override
	public boolean isResizable() {
		return this.frame.isResizable();
	}
	// endregion

	// region Setters.
	@Override
	public NerdJava2dWindowModule setName(final String p_name) {
		this.frame.setName(p_name);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final PVector p_size) {
		this.frame.setSize((int) p_size.x, (int) p_size.y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setResizable(final boolean p_state) {
		this.frame.setResizable(p_state);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final float p_x, final float p_y) {
		this.frame.setSize((int) p_x, (int) p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setSize(final int p_x, final int p_y) {
		this.frame.setSize(p_x, p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final PVector p_position) {
		this.frame.setLocation((int) p_position.x, (int) p_position.y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final int p_x, final int p_y) {
		this.frame.setLocation(p_x, p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowModule setPosition(final float p_x, final float p_y) {
		this.frame.setLocation((int) p_x, (int) p_y);
		return this;
	}

	/**
	 * @return Whether or not the operation was successful.
	 */
	@Override
	public boolean setAlwaysOnTop(final boolean p_state) {
		if (!this.frame.isAlwaysOnTopSupported())
			return false;

		// this.window.removeNotify();
		this.frame.setAlwaysOnTop(true);
		// this.window.addNotify();

		return true;
	}

	public NerdJava2dWindowModule setIcon(final Image p_image) {
		this.frame.setIconImage(p_image);
		return this;
	}
	// endregion

	@Override
	protected void preSetupImpl() {
		try {
			super.iconImage = super.SKETCH.loadImage(super.SKETCH.SKETCH_SETTINGS.windowIconPath);
		} catch (final Exception e) { // NOSONAR!
		}

		if (super.iconImage != null)
			super.sketchSurface.setIcon(super.iconImage);

		this.canvas = (PSurfaceAWT.SmoothCanvas) super.sketchSurface.getNative();
		this.frame = (JFrame) this.canvas.getFrame();

		if (super.SKETCH.SKETCH_SETTINGS.canResize)
			this.frame.setResizable(true);
	}

	@Override
	protected void postImpl() {
		// Fullscreen?
		// https://stackoverflow.com/a/11570414/

		if (this.pfullscreen != this.fullscreen) {
			this.frame.removeNotify();

			this.frame.setVisible(false);
			while (this.frame.isDisplayable())
				;

			if (this.fullscreen) {
				// If `super.SKETCH.display*` are set to the actual (AWT) ones:

				// this.window.setLocation(-7, -30);
				// this.window.setSize(super.SKETCH.displayWidth - 354,
				// super.SKETCH.displayHeight - 270);

				// Though these are arbitrary numbers, they work cross-platform, surprisingly!:

				// this.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				this.frame.setLocation(-8, -30);
				this.frame.setSize(super.SKETCH.displayWidth + 15, super.SKETCH.displayHeight);
				this.frame.setUndecorated(true);
				this.frame.setVisible(true);
				this.frame.addNotify();
			} else {
				this.frame.setUndecorated(this.stayUndecorated);
				this.centerWindow();
				this.frame.setVisible(true);
				this.frame.setSize(
						super.SKETCH.SKETCH_SETTINGS.width,
						super.SKETCH.SKETCH_SETTINGS.height);
				this.frame.addNotify();
			}

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
