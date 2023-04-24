package com.brahvim.nerd.papplet_wrapper.sketch_managers.window_man;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.jogamp.newt.opengl.GLWindow;

import processing.core.PVector;

/* `package` */ class NerdGlWindowManager extends NerdWindowManager {

    protected GLWindow window;

    // region Construction and initialization.
    public NerdGlWindowManager(final Sketch p_sketch) {
        super(p_sketch);
    }

    @Override
    public void initImpl() {
        this.window = (GLWindow) super.surface.getNative();

        // Let to remain in `Sketch::setup()` due to `ThreadDeath` issues.
        // if (super.SKETCH.INITIALLY_RESIZABLE)
        // this.window.setResizable(true);

        // while (!this.window.isResizable())
        // ;
    }
    // endregion

    // region Getters.
    @Override
    public String getName() {
        return this.window.getTitle();
    }

    @Override
    public PVector getSize() {
        return new PVector(this.window.getWidth(), this.window.getHeight());
    }

    @Override
    public Object getNativeObject() {
        return this.window;
    }

    @Override
    public PVector getPosition() {
        return new PVector(this.window.getX(), this.window.getY());
    }
    // endregion

    // region Setters.
    @Override
    public NerdGlWindowManager setName(final String p_name) {
        this.window.setTitle(p_name);
        return this;
    }

    @Override
    public NerdGlWindowManager setSize(final PVector p_size) {
        this.window.setSize((int) p_size.x, (int) p_size.y);
        return this;
    }

    @Override
    public NerdGlWindowManager setSize(final int p_x, final int p_y) {
        this.window.setSize(p_x, p_y);
        return this;
    }

    @Override
    public NerdGlWindowManager setPosition(final int p_x, int p_y) {
        this.window.setPosition(p_x, p_y);
        return this;
    }

    @Override
    public NerdGlWindowManager setPosition(final PVector p_position) {
        this.window.setPosition((int) p_position.x, (int) p_position.y);
        return this;
    }
    // endregion

    @Override
    public void postCallbackImpl() {
        if (this.pfullscreen != this.fullscreen) {
            this.window.setFullscreen(this.fullscreen);

            // Wait for the window to change its mode.
            // Don't wait for more than `5000` milliseconds!:
            // ...yes, that should crash the program :|
            // (It didn't, during my tests, surprisingly :O
            // The window just... waited there and didn't change states O_O
            // ...and then Processing began rendering again :D
            // Apparently `setFullscreen()` returns a `boolean`, meaning that it does
            // error-checking! Quite kind of JogAmp!)

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
    protected void centerWindowImpl(final float p_displayWidth, final float p_displayHeight) {
        this.window.setPosition(
                (int) ((p_displayHeight * 0.5f) - super.cx),
                (int) ((p_displayHeight * 0.5f) - super.cy));
    }

}
