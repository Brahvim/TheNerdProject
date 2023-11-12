package com.brahvim.nerd.processing_wrapper.special_sketches;

import java.awt.Frame;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.awt.PGraphicsJava2D;
import processing.awt.PSurfaceAWT;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PSurface;

public class NerdSketchUndecoratedJava2dWindow extends NerdSketch<PGraphicsJava2D> {

    public NerdSketchUndecoratedJava2dWindow(NerdSketchSettings<PGraphicsJava2D> p_settings) {
        super(p_settings);
    }

    @Override
    // To remove borders WITHOUT using `fullScreen()`:
    protected PSurface initSurface() {
        final PSurface toRet = super.initSurface();
        final PSurfaceAWT.SmoothCanvas smoothCanvas = (SmoothCanvas) ((PSurfaceAWT) surface).getNative();

        final Frame frame = smoothCanvas.getFrame();
        frame.removeNotify();
        frame.setUndecorated(true);
        frame.addNotify();

        return toRet;
    }

}
