package com.brahvim.nerd.window_management;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.util.Arrays;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdDisplayModule extends NerdModule {

	// region Fields!
	protected NerdInputModule input;
	protected NerdWindowModule<?> window;
	protected GraphicsDevice previousMonitor, currentMonitor;

	// region Display properties.
	public float displayScr;
	public int pixelDensity;
	public int displayRefreshRate;
	public int pixelWidth, pixelHeight;
	public int displayWidth, displayHeight;
	public int displayWidthHalf, displayHeightHalf;
	public int displayWidthQuart, displayHeightQuart;
	public int displayWidthTwice, displayHeightTwice; // <-- Twice!
	public int displayWidthThirdQuart, displayHeightThirdQuart;
	// endregion

	// region Previous frame display properties.
	public float pdisplayScr;
	public int pdisplayRefreshRate;
	public int pdisplayWidth, pdisplayHeight; // <-- Not included with Processing!
	public int pdisplayWidthHalf, pdisplayHeightHalf;
	public int pdisplayWidthQuart, pdisplayHeightQuart;
	public int pdisplayWidthTwice, pdisplayHeightTwice; // <-- Twice!
	public int ppixelDensity, ppixelWidth, ppixelHeight; // <-- Also not included with Processing!
	public int pdisplayWidthThirdQuart, pdisplayHeightThirdQuart;
	// endregion
	// endregion

	public NerdDisplayModule(final NerdSketch<?> p_sketch) {
		super(p_sketch);
	}

	@Override
	public void preSetup() {
		this.currentMonitor = super.SKETCH.DEFAULT_JAVA_SCREEN;
		this.input = (NerdInputModule) super.SKETCH.getNerdModule(NerdInputModule.class);
		this.window = (NerdWindowModule<?>) super.SKETCH.getNerdModule(NerdWindowModule.class);
	}

	public void updateDisplayParameters() {
		this.displayWidth = super.SKETCH.displayWidth;
		this.displayHeight = super.SKETCH.displayHeight;

		this.displayWidthTwice = this.displayWidth * 2;
		this.displayHeightTwice = this.displayHeight * 2;
		this.displayScr = (float) this.displayWidth / (float) this.displayHeight;

		this.displayWidthHalf = this.displayWidth / 2;
		this.displayHeightHalf = this.displayHeight / 2;

		// Dividing directly by `4` for more precision...?
		this.displayWidthQuart = this.displayWidth / 4;
		this.displayHeightQuart = this.displayHeight / 4;

		this.displayWidthThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
		this.displayHeightThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
	}

	public void updateSketchAwtGlobals() {
		// Why is `Array.equals()` faster!? It is supposed to be slow!
		if (!Arrays.equals(super.SKETCH.JAVA_SCREENS, super.SKETCH.LOCAL_GRAPHICS_ENVIRONMENT.getScreenDevices())) {
			super.SKETCH.DEFAULT_JAVA_SCREEN = super.SKETCH.LOCAL_GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
			super.SKETCH.DEFAULT_JAVA_SCREEN_MODE = super.SKETCH.DEFAULT_JAVA_SCREEN.getDisplayMode();
			super.SKETCH.DEFAULT_REFRESH_RATE = super.SKETCH.DEFAULT_JAVA_SCREEN_MODE.getRefreshRate();
		}
	}

	public void recordPreviousDisplayParameters() {
		this.pdisplayScr = this.displayScr;
		this.ppixelWidth = this.pixelWidth;
		this.ppixelHeight = this.pixelHeight;
		this.ppixelDensity = this.pixelDensity;
		this.pdisplayRefreshRate = this.displayRefreshRate;

		this.pdisplayWidthTwice = this.displayWidthTwice;
		this.pdisplayHeightTwice = this.displayHeightTwice;

		this.pdisplayWidth = this.displayWidth;
		this.pdisplayHeight = this.displayHeight;

		this.pdisplayWidthHalf = this.displayWidthHalf;
		this.pdisplayHeightHalf = this.displayHeightHalf;

		this.pdisplayWidthQuart = this.displayWidthQuart;
		this.pdisplayHeightQuart = this.displayHeightQuart;

		this.pdisplayWidthThirdQuart = this.displayWidthThirdQuart;
		this.pdisplayHeightThirdQuart = this.displayHeightThirdQuart;
	}

	public void updatePAppletDisplayDimensionsIfNotDefault() {
		if (this.currentMonitor == super.SKETCH.DEFAULT_JAVA_SCREEN)
			return;

		this.updatePAppletDisplayDimensions();
	}

	public void updatePAppletDisplayDimensions() {
		if (this.currentMonitor == null)
			this.currentMonitor = super.SKETCH.DEFAULT_JAVA_SCREEN;

		// Update `PApplet::displayWidth` and `PApplet::displayHeight`:
		final DisplayMode currentMonitorMode = this.currentMonitor.getDisplayMode();
		super.SKETCH.displayWidth = currentMonitorMode.getWidth();
		super.SKETCH.displayHeight = currentMonitorMode.getHeight();
	}

	@Override
	public void pre() {
		this.recordPreviousDisplayParameters(); // Not a bottleneck.
		this.updateDisplayParameters();
		this.updateSketchAwtGlobals();

		if (this.previousMonitor != this.currentMonitor)
			this.onWindowMonitorChange();

		if (super.SKETCH.focused)
			this.currentMonitor = this.getGraphicsDeviceAt(this.input.GLOBAL_MOUSE_POINT);

		this.updatePAppletDisplayDimensionsIfNotDefault();
	}

	// Internal callback. `protected` so it can be extended further!
	protected void onWindowMonitorChange() {
		this.previousMonitor = this.currentMonitor;

		if (this.currentMonitor != null)
			this.displayRefreshRate = this.currentMonitor.getDisplayMode().getRefreshRate();

		this.updatePAppletDisplayDimensions();
		this.updateDisplayParameters();
		super.SKETCH.monitorChanged(); // Notify the `NerdSketch` instance.
	}

	// region Getters.
	public GraphicsDevice getPreviousMonitor() {
		return this.previousMonitor;
	}

	public GraphicsDevice getCurrentMonitor() {
		return this.currentMonitor;
	}

	// I tried the 3rd-to-last method in
	// https://stackoverflow.com/a/21592711/,
	// but https://stackoverflow.com/a/1248865/ was what worked.
	// And yes, I modified it.
	public GraphicsDevice getGraphicsDeviceAt(final Point p_pos) {
		for (final GraphicsDevice d : super.SKETCH.JAVA_SCREENS)
			for (final GraphicsConfiguration c : d.getConfigurations())
				if (c.getBounds().contains(p_pos))
					return d;

		// If the point is outside all monitors, default to the default monitor!:
		return super.SKETCH.DEFAULT_JAVA_SCREEN;
	}

	public DisplayMode getCurrentMonitorDisplayMode() {
		return this.currentMonitor.getDisplayMode();
	}

	public Point getCurrentMonitorDimensions() {
		final DisplayMode a = this.getCurrentMonitorDisplayMode();
		return new Point(a.getWidth(), a.getHeight());
	}
	// endregion

}
