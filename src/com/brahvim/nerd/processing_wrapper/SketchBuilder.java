package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;

import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PApplet;
import processing.core.PConstants;

public final class SketchBuilder {
    // region Fields, constructor, class `SketchInitializer`...

    private SketchKey sketchInitializer;

    public SketchBuilder() {
        this.sketchInitializer = new SketchKey();
    }

    // Hmmm... "`SketchSettings`" instead..?
    public class SketchKey {
        public int width = 400, height = 400;
        public String renderer = PConstants.P3D, iconPath, name;
        public boolean dontCloseOnEscape, startedFullscreen, canResize,
                cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
        public Class<? extends NerdScene> firstScene;
        public HashMap<Class<? extends NerdScene>, Boolean> scenesToCache;

        private SketchKey() {
            this.scenesToCache = new HashMap<>();
        }
    }
    // endregion

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

    public SketchBuilder setName(String p_name) {
        this.sketchInitializer.name = p_name;
        return this;
    }

    public SketchBuilder setFirstScene(Class<? extends NerdScene> p_firstScene) {
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

    // region Scene caching.
    public SketchBuilder cacheScene(boolean p_isDeletable, Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.sketchInitializer.scenesToCache.put(p_sceneClass, p_isDeletable);
        return this;
    }

    @SafeVarargs
    public final SketchBuilder cacheAllScenes(Boolean p_isDeletable, Class<? extends NerdScene>... p_sceneClasses) {
        if (p_sceneClasses == null)
            return this;

        for (Class<? extends NerdScene> c : p_sceneClasses) {
            if (c == null)
                continue;
            this.sketchInitializer.scenesToCache.put(c, p_isDeletable);
        }
        return this;
    }
    // endregion

    public SketchBuilder preventCloseOnEscape() {
        this.sketchInitializer.dontCloseOnEscape = true;
        return this;
    }

    public SketchBuilder setIconPath(String p_pathString) {
        this.sketchInitializer.iconPath = p_pathString;
        return this;
    }

}
