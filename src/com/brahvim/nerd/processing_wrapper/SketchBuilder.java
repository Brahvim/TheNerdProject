package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.scene_api.Scene;

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
        public boolean dontCloseOnEscape, startedFullscreen, canResize,
                cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
        public Class<? extends Scene> firstScene;

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

    // region `set()`.
    public SketchBuilder setWidth(int p_width) {
        this.sketchInitializer.width = p_width;
        return this;
    }

    public SketchBuilder setHeight(int p_height) {
        this.sketchInitializer.height = p_height;
        return this;
    }

    public SketchBuilder setFirstScene(Class<? extends Scene> p_firstScene) {
        this.sketchInitializer.firstScene = p_firstScene;
        return this;
    }
    // endregion

    public SketchBuilder canResize() {
        this.sketchInitializer.canResize = true;
        return this;
    }

    // region Fullscreen settings.
    public SketchBuilder startFullscreen() {
        this.sketchInitializer.startedFullscreen = true;
        return this;
    }

    public SketchBuilder cannotFullscreen() {
        this.sketchInitializer.cannotFullscreen = false;
        return this;
    }

    public SketchBuilder cannotF11Fullscreen() {
        this.sketchInitializer.cannotF11Fullscreen = true;
        return this;
    }

    public SketchBuilder cannotAltEnterFullscreen() {
        this.sketchInitializer.cannotAltEnterFullscreen = true;
        return this;
    }
    // endregion

    public SketchBuilder dontCloseOnEscape() {
        this.sketchInitializer.dontCloseOnEscape = true;
        return this;
    }

}