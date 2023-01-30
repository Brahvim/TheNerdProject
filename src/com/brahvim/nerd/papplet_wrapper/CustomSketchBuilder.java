package com.brahvim.nerd.papplet_wrapper;

import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;

public abstract class CustomSketchBuilder<SKETCH_TYPE extends Sketch> {

    // region Fields and the constructor.
    protected final SketchKey SKETCH_KEY;

    public CustomSketchBuilder() {
        this.SKETCH_KEY = new SketchKey();
    }
    // endregion

    public abstract SKETCH_TYPE build(String[] p_javaMainArgs);

    // region Renderer selection.
    public CustomSketchBuilder<SKETCH_TYPE> useJavaRenderer() {
        this.SKETCH_KEY.renderer = PConstants.JAVA2D;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> useOpenGlRenderer() {
        this.SKETCH_KEY.renderer = PConstants.P3D;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> useJavaFxRenderer() {
        this.SKETCH_KEY.renderer = PConstants.FX2D;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> usePdfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.PDF;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> useSvgRenderer() {
        this.SKETCH_KEY.renderer = PConstants.SVG;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> useDxfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.DXF;
        return this;
    }
    // endregion

    // region `onSketchEvent()`.
    public CustomSketchBuilder<SKETCH_TYPE> onSketchConstructed(Sketch.SketchInsideListener p_constructionListener) {
        this.SKETCH_KEY.sketchconstructedListener = p_constructionListener;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> onSketchDispose(Sketch.SketchInsideListener p_disposaListener) {
        this.SKETCH_KEY.disposalListener = p_disposaListener;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> onSketchSetup(Sketch.SketchInsideListener p_setupListener) {
        this.SKETCH_KEY.setupListener = p_setupListener;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> onSketchExit(Sketch.SketchInsideListener p_setupListener) {
        this.SKETCH_KEY.exitListener = p_setupListener;
        return this;
    }
    // endregion

    // region `set()`.
    // region Window settings!
    // region Dimensions.
    public CustomSketchBuilder<SKETCH_TYPE> setWidth(int p_width) {
        this.SKETCH_KEY.width = p_width;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> setHeight(int p_height) {
        this.SKETCH_KEY.height = p_height;
        return this;
    }
    // endregion

    public CustomSketchBuilder<SKETCH_TYPE> setTitle(String p_name) {
        this.SKETCH_KEY.name = p_name;
        return this;
    }
    // endregion

    public CustomSketchBuilder<SKETCH_TYPE> setStringTablePath(String p_path) {
        this.SKETCH_KEY.stringTablePath = p_path;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> setFirstScene(Class<? extends NerdScene> p_firstScene) {
        this.SKETCH_KEY.firstScene = p_firstScene;
        return this;
    }

    // region `Sketch.CallbackOrder`.
    public CustomSketchBuilder<SKETCH_TYPE> setPreCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.preCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> setDrawCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.drawCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> setPostCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.postCallOrder = p_order;
        return this;
    }
    // endregion
    // endregion

    // region Window behaviors and properties.
    public CustomSketchBuilder<SKETCH_TYPE> setIconPath(String p_pathString) {
        this.SKETCH_KEY.iconPath = p_pathString;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> canResize() {
        this.SKETCH_KEY.canResize = true;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> preventCloseOnEscape() {
        this.SKETCH_KEY.dontCloseOnEscape = true;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> startFullscreen() {
        this.SKETCH_KEY.startedFullscreen = true;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> cannotFullscreen() {
        this.SKETCH_KEY.cannotFullscreen = false;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> cannotF11Fullscreen() {
        this.SKETCH_KEY.cannotF11Fullscreen = true;
        return this;
    }

    public CustomSketchBuilder<SKETCH_TYPE> cannotAltEnterFullscreen() {
        this.SKETCH_KEY.cannotAltEnterFullscreen = true;
        return this;
    }
    // endregion

    // region Any kind of pre-loading.
    public CustomSketchBuilder<SKETCH_TYPE> preLoadAssets(Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.SKETCH_KEY.scenesToPreload.add(p_sceneClass);
        return this;
    }

    @SafeVarargs
    public final CustomSketchBuilder<SKETCH_TYPE> preLoadAssets(Class<? extends NerdScene>... p_sceneClasses) {
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
