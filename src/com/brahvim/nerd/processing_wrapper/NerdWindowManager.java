package com.brahvim.nerd.processing_wrapper;

import java.awt.DisplayMode;
import java.util.LinkedHashSet;
import java.util.Objects;

import com.brahvim.nerd.processing_wrapper.window_man_subs.NerdGlWindowManager;
import com.brahvim.nerd.processing_wrapper.window_man_subs.NerdJava2dWindowManager;

import processing.core.PConstants;
import processing.core.PSurface;
import processing.core.PVector;

public abstract class NerdWindowManager {

    // region Fields.
    /** Position of the window relative to the monitor. */
    public final PVector WINDOW_POSITION = new PVector(), PREV_WINDOW_POSITION = new PVector();

    public boolean fullscreen, pfullscreen;
    public boolean cursorConfined, cursorVisible = true;
    public boolean pcursorConfined, pcursorVisible = true;

    // Windows dimensions for the current and previous frames:
    public float pdbx, pdby, pcx, pcy, pqx, pqy, pq3x, pq3y, pscr;
    public float dbx, dby, cx, cy, qx, qy, q3x, q3y, scr;
    public int width, height, pwidth, pheight;

    protected final NerdSketch SKETCH;

    protected PSurface surface;
    protected NerdDisplayManager displays;
    // endregion

    // region Construction and initialization.
    public NerdWindowManager(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch;
        this.fullscreen = this.SKETCH.STARTED_FULLSCREEN;
    }

    public void init() {
        this.surface = this.SKETCH.getSurface();
        this.displays = this.SKETCH.getDisplayManager();
        this.initImpl();
    }

    protected abstract void initImpl();
    // endregion

    /**
     * Feel free not to use this method and call constructors yourself!
     */
    public static NerdWindowManager createWindowMan(final NerdSketch p_sketch) {
        return switch (p_sketch.RENDERER) {
            case PConstants.P2D, PConstants.P3D -> new NerdGlWindowManager(p_sketch);
            case PConstants.JAVA2D -> new NerdJava2dWindowManager(p_sketch);
            default -> null;
        };
    }

    // region Taking the window to the center.
    public void centerWindow() {
        // You called this function when the window changed its size or position, right?
        // Remember: Computers with multiple displays exist! We shouldn't cache this:
        this.updateWindowRatios();

        final DisplayMode CURRENT_DISPLAY_MODE = this.displays.getCurrentMonitor() == null
                ? this.SKETCH.DEFAULT_JAVA_SCREEN_MODE
                : this.displays.getCurrentMonitor().getDisplayMode();

        this.centerWindowImpl(CURRENT_DISPLAY_MODE.getWidth(), CURRENT_DISPLAY_MODE.getHeight());

        // this.surface.setLocation(winX, winY);
        // (Well, changing the display does NOT effect those variables in any way :|)
    }

    protected abstract void centerWindowImpl(final float p_displayWidth, final float p_displayHeight);
    // endregion

    // region Updates!
    public void updateWindowRatios() {
        this.dbx = this.width * 2;
        this.dby = this.height * 2;

        this.cx = this.width * 0.5f;
        this.cy = this.height * 0.5f;

        this.qx = this.cx * 0.5f;
        this.qy = this.cy * 0.5f;

        this.q3x = this.cx + this.qx;
        this.q3y = this.cy + this.qy;

        this.scr = (float) this.width / (float) this.height;
    }

    public void recordCurrentWindowRatios() {
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
    public PSurface getSurface() {
        return this.surface;
    }

    public abstract String getName();

    public abstract PVector getSize();

    public abstract PVector getPosition();

    public abstract Object getNativeObject();

    public abstract boolean getAlwaysOnTop();
    // endregion

    // region Setters.
    // Implementations return pointers of their own type, not `NerdWindowManager*`s,
    public abstract boolean setAlwaysOnTop(final boolean p_name);

    public abstract NerdWindowManager setName(final String p_name);

    public abstract NerdWindowManager setSize(final PVector p_size);

    public abstract NerdWindowManager setSize(final int p_x, final int p_y);

    public abstract NerdWindowManager setPosition(final PVector p_position);

    public abstract NerdWindowManager setPosition(final int p_x, final int p_y);
    // endregion

    // region Callbacks.
    public void preCallback(final LinkedHashSet<NerdSketch.NerdSketchWindowListener> p_windowListeners) {
        // Previous state:
        this.pwidth = this.width;
        this.pheight = this.height;

        // Current state:
        this.width = this.SKETCH.width;
        this.height = this.SKETCH.height;

        this.PREV_WINDOW_POSITION.set(this.WINDOW_POSITION);
        this.WINDOW_POSITION.set(this.getPosition());

        // When the window is resized, do the following!:
        if (!(this.pwidth == this.width || this.pheight == this.height)) {
            this.updateWindowRatios();

            for (final NerdSketch.NerdSketchWindowListener l : Objects.requireNonNull(
                    p_windowListeners, "`NerdWindowManager::preCallback()` received `null`.)"))
                l.resized();

            this.SKETCH.SCENES.resized();
        }
    }

    public void postCallback(final LinkedHashSet<NerdSketch.NerdSketchWindowListener> p_windowListeners) {
        this.postCallbackImpl();

        if (this.pfullscreen != this.fullscreen) {
            for (final NerdSketch.NerdSketchWindowListener l : Objects.requireNonNull(
                    p_windowListeners, "`NerdWindowManager::preCallback()` received `null`.)"))
                l.fullscreenChanged(this.fullscreen);

            this.SKETCH.SCENES.fullscreenChanged(this.fullscreen);
        }

        this.pfullscreen = this.fullscreen;
        this.pcursorVisible = this.cursorVisible;
        this.pcursorConfined = this.cursorConfined;
    }

    protected abstract void postCallbackImpl();
    // endregion

}
