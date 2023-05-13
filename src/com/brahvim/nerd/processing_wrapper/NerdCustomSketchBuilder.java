package com.brahvim.nerd.processing_wrapper;

import java.util.Objects;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scenes.NerdScene;
import com.brahvim.nerd.framework.scenes.NerdSceneManager;
import com.brahvim.nerd.framework.scenes.NerdSceneManager.SceneManagerSettings;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Want to hack into the {@link NerdSketch} class and control its inner workings
 * beyond just... using callbacks? Why not extend it!?<br>
 * <br>
 *
 * Override/Implement {@link NerdCustomSketchBuilder#build()}, and return an
 * instance of your own {@link NerdSketch} subclass!
 */
public abstract class NerdCustomSketchBuilder {

	// region Field*[s]*, constructor, building...
	protected final NerdSketchBuilderSettings SKETCH_KEY;

	public NerdCustomSketchBuilder() {
		this.SKETCH_KEY = new NerdSketchBuilderSettings();
	}

	// Got this passed in here to allow passing in fake values.
	// I know `sun.java.command` (system property, accessible via
	// `System.getProperty()`) exists.
	public final NerdSketchBuildArtifacts build(final String[] p_javaMainArgs) {
		final NerdSketch constructedSketch = this.buildImpl(p_javaMainArgs);
		final String[] args = new String[] { constructedSketch.getClass().getName() };

		if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
			PApplet.runSketch(args, constructedSketch);
		else
			PApplet.runSketch(PApplet.concat(args, p_javaMainArgs), constructedSketch);

		final NerdSketchBuildArtifacts toRet = new NerdSketchBuildArtifacts(constructedSketch);

		switch (constructedSketch.RENDERER) {
			case PConstants.JAVA2D -> toRet.addExtObject(
					"JFrame", constructedSketch.sketchFrame);

			case PConstants.P2D, PConstants.P3D -> {
				toRet.addExtObject("GL", constructedSketch.gl);
				toRet.addExtObject("GLU", constructedSketch.glu);
				toRet.addExtObject("PGL", constructedSketch.pgl);
				toRet.addExtObject("GLWindow", constructedSketch.glWindow);
				toRet.addExtObject("PGraphicsOpenGL", constructedSketch.glGraphics);
			}
		}

		return toRet;
	}

	protected abstract NerdSketch buildImpl(String[] p_javaMainArgs);
	// endregion

	// region Renderer selection.
	public NerdCustomSketchBuilder usesJavaRenderer() {
		this.SKETCH_KEY.renderer = PConstants.JAVA2D;
		return this;
	}

	public NerdCustomSketchBuilder usesOpenGlRenderer() {
		this.SKETCH_KEY.renderer = PConstants.P3D;
		return this;
	}

	public NerdCustomSketchBuilder usesJavaFxRenderer() {
		this.SKETCH_KEY.renderer = PConstants.FX2D;
		return this;
	}

	public NerdCustomSketchBuilder usesPdfRenderer() {
		this.SKETCH_KEY.renderer = PConstants.PDF;
		return this;
	}

	public NerdCustomSketchBuilder usesSvgRenderer() {
		this.SKETCH_KEY.renderer = PConstants.SVG;
		return this;
	}

	public NerdCustomSketchBuilder usesDxfRenderer() {
		this.SKETCH_KEY.renderer = PConstants.DXF;
		return this;
	}
	// endregion

	// region Adding listeners.
	public NerdCustomSketchBuilder addSketchConstructionListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.sketchConstructedListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addSketchDisposalListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.disposalListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addSketchSetupListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.setupListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addPreListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.preListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addPostListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.postListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addPreDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.preDrawListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.drawListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addPostDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.postDrawListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}

	public NerdCustomSketchBuilder addSketchExitListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.exitListeners.add(Objects.requireNonNull(
				p_listener, "A listener passed to `SketchKey` cannot be `null`"));
		return this;
	}
	// endregion

	public NerdCustomSketchBuilder addNerdExt(final NerdExt p_extObj) {
		this.SKETCH_KEY.nerdExtensions.put(
				p_extObj.getExtName(),
				p_extObj.init(this));

		return this;
	}

	// region `set()`.
	// region Window settings!
	// region Dimensions.
	public NerdCustomSketchBuilder setWidth(final int p_width) {
		this.SKETCH_KEY.width = p_width;
		return this;
	}

	public NerdCustomSketchBuilder setHeight(final int p_height) {
		this.SKETCH_KEY.height = p_height;
		return this;
	}
	// endregion

	public NerdCustomSketchBuilder setTitle(final String p_name) {
		this.SKETCH_KEY.name = p_name;
		return this;
	}
	// endregion

	public NerdCustomSketchBuilder setEcsSystemOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
		this.SKETCH_KEY.ecsSystemOrder = p_ecsSystems;
		return this;
	}

	public NerdCustomSketchBuilder setSceneManagerSettings(final Consumer<SceneManagerSettings> p_settingsBuilder) {
		final var toPass = new NerdSceneManager.SceneManagerSettings();

		if (p_settingsBuilder != null)
			p_settingsBuilder.accept(toPass);

		this.SKETCH_KEY.sceneManagerSettings = toPass;
		return this;
	}

	public NerdCustomSketchBuilder setSceneManagerSettings(final SceneManagerSettings p_settings) {
		if (p_settings != null)
			this.SKETCH_KEY.sceneManagerSettings = p_settings;
		return this;
	}

	public NerdCustomSketchBuilder setStringTablePath(final String p_path) {
		this.SKETCH_KEY.stringTablePath = p_path;
		return this;
	}

	public NerdCustomSketchBuilder setFirstScene(final Class<? extends NerdScene> p_firstScene) {
		// Objects.requireNonNull(p_firstScene, "The first scene needs to be set, and
		// cannot be `null`!");
		this.SKETCH_KEY.firstScene = p_firstScene;
		return this;
	}

	public NerdCustomSketchBuilder setAntiAliasing(final int p_value) {
		this.SKETCH_KEY.antiAliasing = p_value;
		return this;
	}

	// region Setting `NerdSceneManager.SceneManagerSettings.CallbackOrder`.
	public NerdCustomSketchBuilder setPreCallOrder(final NerdSceneManager.SceneManagerSettings.CallbackOrder p_order) {
		this.SKETCH_KEY.preCallOrder = p_order;
		return this;
	}

	public NerdCustomSketchBuilder setDrawCallOrder(final NerdSceneManager.SceneManagerSettings.CallbackOrder p_order) {
		this.SKETCH_KEY.drawCallOrder = p_order;
		return this;
	}

	public NerdCustomSketchBuilder setPostCallOrder(final NerdSceneManager.SceneManagerSettings.CallbackOrder p_order) {
		this.SKETCH_KEY.postCallOrder = p_order;
		return this;
	}
	// endregion
	// endregion

	// region Window behaviors and properties.
	public NerdCustomSketchBuilder setIconPath(final String p_pathString) {
		this.SKETCH_KEY.iconPath = p_pathString;
		return this;
	}

	public NerdCustomSketchBuilder canResize() {
		this.SKETCH_KEY.canResize = true;
		return this;
	}

	public NerdCustomSketchBuilder preventCloseOnEscape() {
		this.SKETCH_KEY.dontCloseOnEscape = true;
		return this;
	}

	public NerdCustomSketchBuilder startFullscreen() {
		this.SKETCH_KEY.startedFullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder cannotFullscreen() {
		this.SKETCH_KEY.cannotFullscreen = false;
		return this;
	}

	public NerdCustomSketchBuilder cannotF11Fullscreen() {
		this.SKETCH_KEY.cannotF11Fullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder cannotAltEnterFullscreen() {
		this.SKETCH_KEY.cannotAltEnterFullscreen = true;
		return this;
	}
	// endregion

	// region Any kind of pre-loading.
	public NerdCustomSketchBuilder preLoadAssets(final Class<? extends NerdScene> p_sceneClass) {
		if (p_sceneClass == null)
			return this;

		this.SKETCH_KEY.scenesToPreload.add(p_sceneClass);
		return this;
	}

	@SuppressWarnings("all")
	public NerdCustomSketchBuilder preLoadAssets(final Class<? extends NerdScene>... p_sceneClasses) {
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
