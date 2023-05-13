package com.brahvim.nerd.processing_wrapper.window_man_subs;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdWindowManager;

import processing.awt.PSurfaceAWT;
import processing.core.PVector;

public class NerdJava2dWindowManager extends NerdWindowManager {

	protected JFrame window;
	protected PSurfaceAWT.SmoothCanvas canvas;

	public NerdJava2dWindowManager(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	// region Getters.
	@Override
	public String getName() {
		return this.window.getName();
	}

	@Override
	public PVector getSize() {
		final Dimension size = this.window.getSize();
		return new PVector((float) size.getWidth(), (float) size.getHeight());
	}

	public Image getIcon() {
		return this.window.getIconImage();
	}

	@Override
	public PVector getPosition() {
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
	// endregion

	// region Setters.
	@Override
	public NerdJava2dWindowManager setName(final String p_name) {
		this.window.setName(p_name);
		return this;
	}

	@Override
	public NerdJava2dWindowManager setSize(final PVector p_size) {
		this.window.setSize((int) p_size.x, (int) p_size.y);
		return this;
	}

	@Override
	public NerdJava2dWindowManager setSize(final int p_x, final int p_y) {
		this.window.setSize(p_x, p_y);
		return this;
	}

	@Override
	public NerdJava2dWindowManager setPosition(final PVector p_position) {
		this.window.setLocation((int) p_position.x, (int) p_position.y);
		return this;
	}

	@Override
	public NerdJava2dWindowManager setPosition(final int p_x, final int p_y) {
		this.window.setLocation(p_x, p_y);
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

	public NerdJava2dWindowManager setIcon(final Image p_image) {
		this.window.setIconImage(p_image);
		return this;
	}
	// endregion

	@Override
	public void initImpl() {
		super.surface.setIcon(super.SKETCH.getIconImage());
		this.canvas = (PSurfaceAWT.SmoothCanvas) super.surface.getNative();
		this.window = (JFrame) canvas.getFrame();

		if (super.SKETCH.INITIALLY_RESIZABLE)
			this.window.setResizable(true);
	}

	@Override
	public void postCallbackImpl() {
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
				this.window.setSize(super.SKETCH.INIT_WIDTH, super.SKETCH.INIT_HEIGHT);
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
	protected void centerWindowImpl(final float p_displayWidth, final float p_displayHeight) {
		super.surface.setLocation( // this.window.setLocation(
				(int) (p_displayWidth * 0.5f - super.width),
				(int) (p_displayHeight * 0.5f - super.q3y));
	}

}
