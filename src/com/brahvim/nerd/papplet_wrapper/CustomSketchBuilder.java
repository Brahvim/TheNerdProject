package com.brahvim.nerd.papplet_wrapper;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Want to hack into the `Sketch` class and control its inner workings?
 * Why not extend it!
 *
 * Override/Implement this class's `build()` method and return an instance of
 * that `Sketch` subclass!
 */

// If "`SketchT`" sounds weird to you, check out:
// [https://stackoverflow.com/a/30146204/]

public abstract class CustomSketchBuilder /* <SketchT extends Sketch> */ {

    // region Fields, constructor, building...
    protected final SketchKey SKETCH_KEY;

    public CustomSketchBuilder() {
        this.SKETCH_KEY = new SketchKey();
    }

    public final Sketch build(String[] p_javaMainArgs) {
        Sketch constructedSketch = this.buildImpl(p_javaMainArgs);
        String[] args = new String[] { constructedSketch.getClass().getName() };

        if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
            PApplet.runSketch(args, constructedSketch);
        else
            PApplet.runSketch(PApplet.concat(args, p_javaMainArgs), constructedSketch);

        return constructedSketch;
    }

    protected abstract Sketch buildImpl(String[] p_javaMainArgs);
    // endregion

    // region Renderer selection.
    public CustomSketchBuilder usesJavaRenderer() {
        this.SKETCH_KEY.renderer = PConstants.JAVA2D;
        return this;
    }

    public CustomSketchBuilder usesOpenGlRenderer() {
        this.SKETCH_KEY.renderer = PConstants.P3D;
        return this;
    }

    public CustomSketchBuilder usesJavaFxRenderer() {
        this.SKETCH_KEY.renderer = PConstants.FX2D;
        return this;
    }

    public CustomSketchBuilder usesPdfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.PDF;
        return this;
    }

    public CustomSketchBuilder usesSvgRenderer() {
        this.SKETCH_KEY.renderer = PConstants.SVG;
        return this;
    }

    public CustomSketchBuilder usesDxfRenderer() {
        this.SKETCH_KEY.renderer = PConstants.DXF;
        return this;
    }
    // endregion

    public CustomSketchBuilder usesOpenAl() {
        this.SKETCH_KEY.useOpenal = true;
        return this;
    }

    // region `onSketchEvent()`.
    public CustomSketchBuilder onSketchConstructed(Consumer<Sketch> p_constructionListener) {
        this.SKETCH_KEY.sketchConstructedListener = p_constructionListener;
        return this;
    }

    public CustomSketchBuilder onSketchDispose(Consumer<Sketch> p_disposaListener) {
        this.SKETCH_KEY.disposalListener = p_disposaListener;
        return this;
    }

    public CustomSketchBuilder onSketchSetup(Consumer<Sketch> p_setupListener) {
        this.SKETCH_KEY.setupListener = p_setupListener;
        return this;
    }

    public CustomSketchBuilder onSketchExit(Consumer<Sketch> p_setupListener) {
        this.SKETCH_KEY.exitListener = p_setupListener;
        return this;
    }
    // endregion

    // region `set()`.
    // region Window settings!
    // region Dimensions.
    public CustomSketchBuilder setWidth(int p_width) {
        this.SKETCH_KEY.width = p_width;
        return this;
    }

    public CustomSketchBuilder setHeight(int p_height) {
        this.SKETCH_KEY.height = p_height;
        return this;
    }
    // endregion

    public CustomSketchBuilder setTitle(String p_name) {
        this.SKETCH_KEY.name = p_name;
        return this;
    }
    // endregion

    public CustomSketchBuilder setSceneManagerSettings(Supplier<SceneManagerSettings> p_settingsBuilder) {
        if (p_settingsBuilder != null)
            this.SKETCH_KEY.sceneManagerSettings = p_settingsBuilder.get();
        return this;
    }

    public CustomSketchBuilder setSceneManagerSettings(SceneManagerSettings p_settings) {
        if (p_settings != null)
            this.SKETCH_KEY.sceneManagerSettings = p_settings;
        return this;
    }

    public CustomSketchBuilder setAlContextSettings(Supplier<AlContext.AlContextSettings> p_settingsBuilder) {
        if (p_settingsBuilder != null)
            this.SKETCH_KEY.alContextSettings = p_settingsBuilder.get();
        return this;
    }

    public CustomSketchBuilder setAlContextSettings(AlContext.AlContextSettings p_settings) {
        if (p_settings != null)
            this.SKETCH_KEY.alContextSettings = p_settings;
        return this;
    }

    public CustomSketchBuilder setStringTablePath(String p_path) {
        this.SKETCH_KEY.stringTablePath = p_path;
        return this;
    }

    public CustomSketchBuilder setFirstScene(Class<? extends NerdScene> p_firstScene) {
        // Objects.requireNonNull(p_firstScene, "The first scene needs to be set, and
        // cannot be `null`!");
        this.SKETCH_KEY.firstScene = p_firstScene;
        return this;
    }

    public CustomSketchBuilder setAntiAliasing(int p_value) {
        this.SKETCH_KEY.antiAliasing = p_value;
        return this;
    }

    // region `Sketch.CallbackOrder`.
    public CustomSketchBuilder setPreCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.preCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder setDrawCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.drawCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder setPostCallOrder(Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.postCallOrder = p_order;
        return this;
    }
    // endregion
    // endregion

    // region Window behaviors and properties.
    public CustomSketchBuilder setIconPath(String p_pathString) {
        this.SKETCH_KEY.iconPath = p_pathString;
        return this;
    }

    public CustomSketchBuilder canResize() {
        this.SKETCH_KEY.canResize = true;
        return this;
    }

    public CustomSketchBuilder preventCloseOnEscape() {
        this.SKETCH_KEY.dontCloseOnEscape = true;
        return this;
    }

    public CustomSketchBuilder startFullscreen() {
        this.SKETCH_KEY.startedFullscreen = true;
        return this;
    }

    public CustomSketchBuilder cannotFullscreen() {
        this.SKETCH_KEY.cannotFullscreen = false;
        return this;
    }

    public CustomSketchBuilder cannotF11Fullscreen() {
        this.SKETCH_KEY.cannotF11Fullscreen = true;
        return this;
    }

    public CustomSketchBuilder cannotAltEnterFullscreen() {
        this.SKETCH_KEY.cannotAltEnterFullscreen = true;
        return this;
    }
    // endregion

    // region Any kind of pre-loading.
    public CustomSketchBuilder preLoadAssets(Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.SKETCH_KEY.scenesToPreload.add(p_sceneClass);
        return this;
    }

    @SuppressWarnings("all")
    // @SafeVarargs
    public CustomSketchBuilder preLoadAssets(Class<? extends NerdScene>... p_sceneClasses) {
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
