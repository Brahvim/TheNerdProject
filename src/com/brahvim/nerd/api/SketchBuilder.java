package com.brahvim.nerd.api;

import processing.core.PApplet;
import processing.core.PConstants;

public class SketchBuilder {
    private String renderer = PConstants.P3D;

    public SketchBuilder() {
    }

    // Why use a `SketchInitializer`, when you could make `Sketch` use only one
    // parameterized constructor taking in a `SketchBuilder` the same way?

    public Sketch build(String[] p_javaMainArgs) {
        Sketch constructedSketch = new Sketch(this.sketchInitializer);
        String[] args = new String[] { constructedSketch.getClass().getName() };

        if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
            PApplet.runSketch(args, constructedSketch);
        else
            PApplet.runSketch(PApplet.concat(p_javaMainArgs, args), constructedSketch);

        return constructedSketch;
    }

    // region Renderer selection.
    public void useJavaRenderer() {
        this.sketchInitializer.renderer = PConstants.JAVA2D;
    }

    public void useOpenGlRenderer() {
        this.sketchInitializer.renderer = PConstants.P3D;
    }

    public void useJavaFxRenderer() {
        this.sketchInitializer.renderer = PConstants.FX2D;
    }

    public void usePdfRenderer() {
        this.sketchInitializer.renderer = PConstants.PDF;
    }

    public void useSvgRenderer() {
        this.sketchInitializer.renderer = PConstants.SVG;
    }

    public void useDxfRenderer() {
        this.sketchInitializer.renderer = PConstants.DXF;
    }
    // endregion

}
