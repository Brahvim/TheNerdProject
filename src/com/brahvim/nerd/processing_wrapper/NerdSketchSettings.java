package com.brahvim.nerd.processing_wrapper;

import processing.core.PApplet;

public class NerdSketchSettings {

    /** Should the sketch be started in fullscreen? */
    public boolean fullScreen;

    /**
     * One of the initial dimensions for the sketch's window.
     * Is {@code 640x480} by default!
     */
    public int width = 640, height = 480;

    /** What {@link NerdSketch#settings} passes to {@link PApplet#smooth} */
    public int antiAliasing = 0;

    // region Initial dimensions.
    public void startInFullscreen() {
        this.fullScreen = true;
    }

    public void setHeight(final int p_height) {
        this.height = p_height;
    }

    public void setWidth(final int p_width) {
        this.width = p_width;
    }
    // endregion

}
