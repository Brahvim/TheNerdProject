package com.brahvim.nerd.processing_wrapper.sketches.special;

import java.awt.Frame;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.window_management.window_module_impls.NerdJava2dWindowModule;

import processing.awt.PGraphicsJava2D;
import processing.awt.PSurfaceAWT;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PSurface;

public class NerdJava2dUndecoratedWindowSketch extends NerdSketch<PGraphicsJava2D> {

    protected NerdJava2dWindowModule window;

    public NerdJava2dUndecoratedWindowSketch(final NerdSketchSettings<PGraphicsJava2D> p_settings) {
        super(p_settings);
    }

    @Override
    public void postSetup() {
        super.postSetup();

        // Our specialty!:
        this.window = (NerdJava2dWindowModule) super.GENERIC_WINDOW;
        this.window.stayUndecorated = true;
    }

    @Override
    // To remove borders WITHOUT using `fullScreen()`:
    protected PSurface initSurface() {
        final PSurface toRet = super.initSurface();
        final PSurfaceAWT.SmoothCanvas smoothCanvas = (SmoothCanvas) ((PSurfaceAWT) this.surface).getNative();

        final Frame frame = smoothCanvas.getFrame();
        frame.removeNotify();
        frame.setUndecorated(true);
        frame.addNotify();

        return toRet;
    }

}
