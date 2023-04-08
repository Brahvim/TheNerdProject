package com.brahvim.nerd.papplet_wrapper;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Want to hack into the {@link Sketch} class and control its inner workings
 * beyond just... using callbacks? Why not extend it!?<br>
 * <br>
 *
 * Override/Implement {@link CustomSketchBuilder#build()}, and return an
 * instance of your own {@link Sketch} subclass!
 */
public abstract class CustomSketchBuilder {

    // region Field*[s]*, constructor, building...
    protected final SketchKey SKETCH_KEY;

    public CustomSketchBuilder() {
        this.SKETCH_KEY = new SketchKey();
    }

    public final Sketch build(final String[] p_javaMainArgs) {
        final Sketch constructedSketch = this.buildImpl(p_javaMainArgs);
        final String[] args = new String[] { constructedSketch.getClass().getName() };

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

    // region Adding listeners.
    public CustomSketchBuilder addSketchConstructionListener(final Consumer<Sketch> p_constructionListener) {
        this.SKETCH_KEY.sketchConstructedListeners.add(p_constructionListener);
        return this;
    }

    public CustomSketchBuilder addSketchDisposalListener(final Consumer<Sketch> p_disposaListener) {
        this.SKETCH_KEY.disposalListeners.add(p_disposaListener);
        return this;
    }

    public CustomSketchBuilder addSketchSetupListener(final Consumer<Sketch> p_setupListener) {
        this.SKETCH_KEY.setupListeners.add(p_setupListener);
        return this;
    }

    public CustomSketchBuilder addPreListener(final Consumer<Sketch> p_preListener) {
        this.SKETCH_KEY.preListeners.add(p_preListener);
        return this;
    }

    public CustomSketchBuilder addPostListener(final Consumer<Sketch> p_postListener) {
        this.SKETCH_KEY.postListeners.add(p_postListener);
        return this;
    }

    public CustomSketchBuilder addPreDrawListener(final Consumer<Sketch> p_preDrawListener) {
        this.SKETCH_KEY.preDrawListeners.add(p_preDrawListener);
        return this;
    }

    public CustomSketchBuilder addDrawListener(final Consumer<Sketch> p_drawListener) {
        this.SKETCH_KEY.drawListeners.add(p_drawListener);
        return this;
    }

    public CustomSketchBuilder addPostDrawListener(final Consumer<Sketch> p_postDrawListener) {
        this.SKETCH_KEY.postDrawListeners.add(p_postDrawListener);
        return this;
    }

    public CustomSketchBuilder addSketchExitListener(final Consumer<Sketch> p_exitListener) {
        this.SKETCH_KEY.exitListeners.add(p_exitListener);
        return this;
    }
    // endregion

    public CustomSketchBuilder addNerdExt(final NerdExt p_extObj) {
        this.SKETCH_KEY.nerdExtensions.put(
                p_extObj.getExtName(),
                p_extObj.init(this));

        return this;
    }

    // region `set()`.
    // region Window settings!
    // region Dimensions.
    public CustomSketchBuilder setWidth(final int p_width) {
        this.SKETCH_KEY.width = p_width;
        return this;
    }

    public CustomSketchBuilder setHeight(final int p_height) {
        this.SKETCH_KEY.height = p_height;
        return this;
    }
    // endregion

    public CustomSketchBuilder setTitle(final String p_name) {
        this.SKETCH_KEY.name = p_name;
        return this;
    }
    // endregion

    public CustomSketchBuilder setSceneManagerSettings(final Supplier<SceneManagerSettings> p_settingsBuilder) {
        if (p_settingsBuilder != null)
            this.SKETCH_KEY.sceneManagerSettings = p_settingsBuilder.get();
        return this;
    }

    public CustomSketchBuilder setSceneManagerSettings(final SceneManagerSettings p_settings) {
        if (p_settings != null)
            this.SKETCH_KEY.sceneManagerSettings = p_settings;
        return this;
    }

    public CustomSketchBuilder setStringTablePath(final String p_path) {
        this.SKETCH_KEY.stringTablePath = p_path;
        return this;
    }

    public CustomSketchBuilder setFirstScene(final Class<? extends NerdScene> p_firstScene) {
        // Objects.requireNonNull(p_firstScene, "The first scene needs to be set, and
        // cannot be `null`!");
        this.SKETCH_KEY.firstScene = p_firstScene;
        return this;
    }

    public CustomSketchBuilder setAntiAliasing(final int p_value) {
        this.SKETCH_KEY.antiAliasing = p_value;
        return this;
    }

    // region `Sketch.CallbackOrder`.
    public CustomSketchBuilder setPreCallOrder(final Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.preCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder setDrawCallOrder(final Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.drawCallOrder = p_order;
        return this;
    }

    public CustomSketchBuilder setPostCallOrder(final Sketch.CallbackOrder p_order) {
        this.SKETCH_KEY.postCallOrder = p_order;
        return this;
    }
    // endregion
    // endregion

    // region Window behaviors and properties.
    public CustomSketchBuilder setIconPath(final String p_pathString) {
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
    public CustomSketchBuilder preLoadAssets(final Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            return this;

        this.SKETCH_KEY.scenesToPreload.add(p_sceneClass);
        return this;
    }

    @SuppressWarnings("all")
    public CustomSketchBuilder preLoadAssets(final Class<? extends NerdScene>... p_sceneClasses) {
        if (p_sceneClasses == null)
            return this;

        for (final Class<? extends NerdScene> c : p_sceneClasses) {
            if (c == null)
                continue;
            this.SKETCH_KEY.scenesToPreload.add(c);
        }
        return this;
    }
    // endregion

}
