package com.brahvim.nerd.api;

import processing.core.PApplet;
import processing.core.PConstants;

public class SketchBuilder {
    private SketchInitializer sketchInitializer;

    public SketchBuilder() {
        this.sketchInitializer = new SketchInitializer();
    }

    // Hmmm... "`SketchSettings`" instead..?
    public class SketchInitializer {
        public int width = 400, height = 400;
        public String renderer = PConstants.P3D;
        public boolean closeOnEscape, startedFullscreen;

        private SketchInitializer() {
        }
    }

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
    public SketchBuilder useJavaRenderer() {
        this.sketchInitializer.renderer = PConstants.JAVA2D;
        return this;
    }

    public SketchBuilder useOpenGlRenderer() {
        this.sketchInitializer.renderer = PConstants.P3D;
        return this;
    }

    public SketchBuilder useJavaFxRenderer() {
        this.sketchInitializer.renderer = PConstants.FX2D;
        return this;
    }

    public SketchBuilder usePdfRenderer() {
        this.sketchInitializer.renderer = PConstants.PDF;
        return this;
    }

    public SketchBuilder useSvgRenderer() {
        this.sketchInitializer.renderer = PConstants.SVG;
        return this;
    }

    public SketchBuilder useDxfRenderer() {
        this.sketchInitializer.renderer = PConstants.DXF;
        return this;
    }
    // endregion

    public SketchBuilder setWidth(int p_width) {
        this.sketchInitializer.width = p_width;
        return this;
    }

    public SketchBuilder setHeight(int p_height) {
        this.sketchInitializer.height = p_height;
        return this;
    }

    public SketchBuilder startFullscreen() {
        this.sketchInitializer.startedFullscreen = true;
        return this;
    }

    public SketchBuilder closeOnEscape() {
        this.sketchInitializer.closeOnEscape = true;
        return this;
    }

}
