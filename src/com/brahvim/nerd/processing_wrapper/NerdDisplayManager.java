package com.brahvim.nerd.processing_wrapper;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Objects;

public class NerdDisplayManager {

    protected final NerdSketch SKETCH;
    protected final NerdWindowManager WINDOW;
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

    public NerdDisplayManager(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch;
        this.WINDOW = this.SKETCH.WINDOW;
    }

    public void updateDisplayRatios() {
        this.displayWidthTwice = this.displayWidth * 2;
        this.displayHeightTwice = this.displayHeight * 2;
        this.displayScr = (float) this.displayWidth / (float) this.displayHeight;

        if (this.currentMonitor != null)
            this.displayRefreshRate = this.currentMonitor.getDisplayMode().getRefreshRate();

        this.displayWidthHalf = this.displayWidth / 2;
        this.displayHeightHalf = this.displayHeight / 2;

        // Dividing directly by `4` for more precision...?
        this.displayWidthQuart = this.displayWidth / 4;
        this.displayHeightQuart = this.displayHeight / 4;

        this.displayWidthThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
        this.displayHeightThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
    }

    public void recordCurrentDisplayRatios() {
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

    // region Current and previous frame monitor settings, plus callback!
    public void preCallback(final LinkedHashSet<NerdSketch.NerdSketchWindowListener> p_windowListeners) {
        this.displayWidth = this.SKETCH.displayWidth;
        this.displayHeight = this.SKETCH.displayHeight;

        final GraphicsDevice[] updatedList = this.SKETCH.LOCAL_GRAPHICS_ENVIRONMENT.getScreenDevices();
        if (this.SKETCH.JAVA_SCREENS != updatedList) {
            this.SKETCH.DEFAULT_JAVA_SCREEN = this.SKETCH.LOCAL_GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
            this.SKETCH.DEFAULT_JAVA_SCREEN_MODE = this.SKETCH.DEFAULT_JAVA_SCREEN.getDisplayMode();
            this.SKETCH.DEFAULT_REFRESH_RATE = this.SKETCH.DEFAULT_JAVA_SCREEN_MODE.getRefreshRate();
        }

        if (this.previousMonitor != this.currentMonitor) {
            this.previousMonitor = this.currentMonitor;
            this.updateDisplayRatios();
            for (final NerdSketch.NerdSketchWindowListener l : Objects.requireNonNull(p_windowListeners,
                    "`NerdDisplayManager::preCallback()` received `null`!"))
                l.monitorChanged();

            this.SKETCH.SCENES.monitorChanged();
        }

        if (this.SKETCH.focused)
            this.currentMonitor = this.getGraphicsDeviceAt(this.SKETCH.GLOBAL_MOUSE_POINT);

        // Update `this.SKETCH.displayWidth` and `this.SKETCH.displayHeight`:
        if (this.currentMonitor != null) {
            final DisplayMode CURRENT_MON_MODE = this.currentMonitor.getDisplayMode();
            this.SKETCH.displayWidth = CURRENT_MON_MODE.getWidth();
            this.SKETCH.displayHeight = CURRENT_MON_MODE.getHeight();
        }
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
        for (final GraphicsDevice d : this.SKETCH.JAVA_SCREENS)
            for (final GraphicsConfiguration c : d.getConfigurations())
                if (c.getBounds().contains(p_pos))
                    return d;

        // If the point is outside all monitors, default to the default monitor!:
        return this.SKETCH.DEFAULT_JAVA_SCREEN;
    }
    // endregion

}
