package com.brahvim.nerd.papplet_wrapper;

import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;

// If "`SketchT`" sounds weird to you, check out [https://stackoverflow.com/a/30146204/13951505]
public abstract class CustomSketchBuilder<SketchT extends Sketch> {

    // region Fields and the constructor.
    protected final SketchKey SKETCH_KEY;

    public CustomSketchBuilder() {
        this.SKETCH_KEY = new SketchKey();
    }
    // endregion

    public abstract SketchT build(String[] p_javaMainArgs);

    // region Renderer selection.
    public CustomSketchBuilder<SketchT> useJavaRenderer() {
        this.SKETCH_KEY.renderer = PConstants.JAVA2D;
        return this;
    }

    public CustomSketchBuilder<SketchT> useOpenGlRenderer() {
        this.SKETCH_KEY.renderer = PConstants.P3D;
        return this;
    }

    public CustomSketchBuilder<SketchT> useJavaFxRenderer() {
        this.SKETCH_KEY.renderer = PConstants.FX2D;
        return this;
    }

    public CustomSketchBuilder<SketchT> usePdfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.PDF;
        return this;
    }

    public CustomSketchBuilder<SketchT> useSvgRenderer() {
        this.SKETCH_KEY.renderer = PConstants.SVG;
        return this;
    }

    public CustomSketchBuilder<SketchT> useDxfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.DXF;
        return this;
    }
    // endregion

    // region `onSketchEvent()`.
    public CustomSketchBuilder<SketchT> onSketchConstructed(Sketch.SketchInsideListener p_constructionListener) {
        this.SKETCH_KEY.sketchconstructedListener = p_constructionListener;
        return this;
    }

    public CustomSketchBuilder<SketchT> onSketchDispose(Sketch.SketchInsideListener p_disposaListener) {
        this.SKETCH_KEY.disposalListener = p_disposaListener;
        return this;
    }

    public CustomSketchBuilder<SketchT> onSketchSetup(Sketch.SketchInsideListener p_setupListener) {
        this.SKETCH_KEY.setupListener = p_setupListener;
        return this;
    }

    public CustomSketchBuilder<SketchT> onSketchExit(Sketch.SketchInsideListener p_setupListener) {
        this.SKETCH_KEY.exitListener = p_setupListener;
        return this;
    }
    // endregion

    // region `set()`.
    // region Window settings!
    // region Dimensions.
    public CustomSketchBuilder<SketchT> setWidth(int p_width) {
        this.SKETCH_KEY.width = p_width;
        return this;
    }

    public CustomSketchBuilder<SketchT> setHeight(int p_height) {
        this.SKETCH_KEY.height = p_height;
        return this;
    }
    // endregion

    public CustomSketchBuilder<SketchT> setTitle(String p_name) {
        this.SKETCH_KEY.name = p_name;
        return this;
    }
    // endregion

    public CustomSketchBuilder<SketchT> setStringTablePath(String p_path) {
        this.SKETCH_KEY.stringTablePath = p_path;
        return this;
    }

    public CustomSketchBuilder<SketchT> setFirstScene(Class<? extends NerdScene> p_firstScene) {
        this.SKETCH_KEY.firstScene = p_firstScene;
        return this;
    }

    // region `Sketch.CallbackOrder`.
    public CustomSketchBuilder<SketchT> setPreCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.preCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder<SketchT> setDrawCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.drawCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder<SketchT> setPostCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.postCallOrder = p_order;
        return this;
    }
    // endregion
    // endregion

    // region Window behaviors and properties.
    public CustomSketchBuilder<SketchT> setIconPath(String p_pathString) {
        this.SKETCH_KEY.iconPath = p_pathString;
        return this;
    }

    public CustomSketchBuilder<SketchT> canResize() {
        this.SKETCH_KEY.canResize = true;
        return this;
    }

    public CustomSketchBuilder<SketchT> preventCloseOnEscape() {
        this.SKETCH_KEY.dontCloseOnEscape = true;
        return this;
    }

    public CustomSketchBuilder<SketchT> startFullscreen() {
        this.SKETCH_KEY.startedFullscreen = true;
        return this;
    }

    public CustomSketchBuilder<SketchT> cannotFullscreen() {
        this.SKETCH_KEY.cannotFullscreen = false;
        return this;
    }

    public CustomSketchBuilder<SketchT> cannotF11Fullscreen() {
        this.SKETCH_KEY.cannotF11Fullscreen = true;
        return this;
    }

    public CustomSketchBuilder<SketchT> cannotAltEnterFullscreen() {
        this.SKETCH_KEY.cannotAltEnterFullscreen = true;
        return this;
    }
    // endregion

    // region Any kind of pre-loading.
    public CustomSketchBuilder<SketchT> preLoadAssets(Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.SKETCH_KEY.scenesToPreload.add(p_sceneClass);
        return this;
    }

    @SafeVarargs
    public final CustomSketchBuilder<SketchT> preLoadAssets(Class<? extends NerdScene>... p_sceneClasses) {
        if (p_sceneClasses == null)
            return this;

        for (Class<? extends NerdScene> c : p_sceneClasses) {
            if (c == null)
                continue;
            this.SKETCH_KEY.scenesToPreload.add(c);
        }
        return this;
    }
    // endregion

}
