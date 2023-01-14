package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PApplet;
import processing.core.PConstants;

public final class SketchBuilder {
    // region Fields, constructor, and class `SketchKey`.
    private final SketchKey SKETCH_KEY;

    public SketchBuilder() {
        this.SKETCH_KEY = new SketchKey();
    }

    // Hmmm... "`SketchSettings`" instead..?
    public class SketchKey extends NerdKey {
        public int width = 400, height = 400;
        public String renderer = PConstants.P3D, iconPath, name;
        public boolean dontCloseOnEscape, startedFullscreen, canResize,
                cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
        public Class<? extends NerdScene> firstScene;
        public HashMap<Class<? extends NerdScene>, Boolean> scenesToCache;

        private SketchKey() {
            this.scenesToCache = new HashMap<>();
        }

        @Override
        public boolean fitsLock(Class<?> p_class) {
            // Putting `p_class` in the argument eliminates the need for a `null` check.
            return Sketch.class.isAssignableFrom(p_class);
        }
    }
    // endregion

    public Sketch build(String[] p_javaMainArgs) {
        Sketch constructedSketch = new Sketch(this.SKETCH_KEY);
        String[] args = new String[] { constructedSketch.getClass().getName() };

        if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
            PApplet.runSketch(args, constructedSketch);
        else
            PApplet.runSketch(PApplet.concat(p_javaMainArgs, args), constructedSketch);

        return constructedSketch;
    }

    // region Renderer selection.
    public SketchBuilder useJavaRenderer() {
        this.SKETCH_KEY.renderer = PConstants.JAVA2D;
        return this;
    }

    public SketchBuilder useOpenGlRenderer() {
        this.SKETCH_KEY.renderer = PConstants.P3D;
        return this;
    }

    public SketchBuilder useJavaFxRenderer() {
        this.SKETCH_KEY.renderer = PConstants.FX2D;
        return this;
    }

    public SketchBuilder usePdfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.PDF;
        return this;
    }

    public SketchBuilder useSvgRenderer() {
        this.SKETCH_KEY.renderer = PConstants.SVG;
        return this;
    }

    public SketchBuilder useDxfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.DXF;
        return this;
    }
    // endregion

    // region `set()`.
    public SketchBuilder setWidth(int p_width) {
        this.SKETCH_KEY.width = p_width;
        return this;
    }

    public SketchBuilder setHeight(int p_height) {
        this.SKETCH_KEY.height = p_height;
        return this;
    }

    public SketchBuilder setTitle(String p_name) {
        this.SKETCH_KEY.name = p_name;
        return this;
    }

    public SketchBuilder setFirstScene(Class<? extends NerdScene> p_firstScene) {
        this.SKETCH_KEY.firstScene = p_firstScene;
        return this;
    }
    // endregion

    public SketchBuilder canResize() {
        this.SKETCH_KEY.canResize = true;
        return this;
    }

    // region Fullscreen settings.
    public SketchBuilder startFullscreen() {
        this.SKETCH_KEY.startedFullscreen = true;
        return this;
    }

    public SketchBuilder cannotFullscreen() {
        this.SKETCH_KEY.cannotFullscreen = false;
        return this;
    }

    public SketchBuilder cannotF11Fullscreen() {
        this.SKETCH_KEY.cannotF11Fullscreen = true;
        return this;
    }

    public SketchBuilder cannotAltEnterFullscreen() {
        this.SKETCH_KEY.cannotAltEnterFullscreen = true;
        return this;
    }
    // endregion

    // region Scene caching.
    public SketchBuilder cacheScene(boolean p_isDeletable, Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.SKETCH_KEY.scenesToCache.put(p_sceneClass, p_isDeletable);
        return this;
    }

    @SafeVarargs
    public final SketchBuilder cacheAllScenes(Boolean p_isDeletable, Class<? extends NerdScene>... p_sceneClasses) {
        if (p_sceneClasses == null)
            return this;

        for (Class<? extends NerdScene> c : p_sceneClasses) {
            if (c == null)
                continue;
            this.SKETCH_KEY.scenesToCache.put(c, p_isDeletable);
        }
        return this;
    }
    // endregion

    public SketchBuilder preventCloseOnEscape() {
        this.SKETCH_KEY.dontCloseOnEscape = true;
        return this;
    }

    public SketchBuilder setIconPath(String p_pathString) {
        this.SKETCH_KEY.iconPath = p_pathString;
        return this;
    }

}
