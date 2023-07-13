package com.brahvim.nerd.processing_wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.brahvim.nerd.framework.ecs.NerdEcsComponent;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule.NerdScenesModuleSettings;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Want to hack into the {@link NerdSketch} class and control its inner workings
 * beyond just... using callbacks? Why not extend it!?
 *
 * <p>
 * Override/Implement {@link NerdCustomSketchBuilder#build()}, and return an
 * instance of your own {@link NerdSketch} subclass!
 */
public abstract class NerdCustomSketchBuilder {

	// region Fields, constructor, building...
	protected static final String NULL_ERR_MSG = "A listener passed to `NerdSketchKey` cannot be `null`";

	protected final NerdSketchBuilderSettings SKETCH_KEY;

	protected NerdCustomSketchBuilder() {
		this.SKETCH_KEY = new NerdSketchBuilderSettings();
	}

	// Getting this passed in here to allow fake arguments.
	// I know `sun.java.command` exists!
	// (System property containing the strings passed to `main` with spaces between
	// them. Accessible via `System.getProperty()` like all other ones!)
	public final NerdSketchBuildArtifacts build(final String[] p_javaMainArgs) {
		final NerdSketch constructedSketch = this.buildImpl(p_javaMainArgs);
		this.SKETCH_KEY.nerdModulesSupplier = this.supplyNerdModulesOrder();
		NerdCustomSketchBuilder.runSketch(constructedSketch, p_javaMainArgs);
		return new NerdSketchBuildArtifacts(constructedSketch);
	}

	public static void runSketch(final NerdSketch p_sketch) {
		NerdCustomSketchBuilder.runSketch(p_sketch, null);
	}

	public static void runSketch(final NerdSketch p_sketch, final String[] p_javaMainArgs) {
		final String[] args = new String[] { p_sketch.getClass().getName() };

		if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
			PApplet.runSketch(args, p_sketch);
		else
			PApplet.runSketch(PApplet.concat(args, p_javaMainArgs), p_sketch);
	}

	protected abstract Function<NerdSketch, LinkedHashSet<NerdModule>> supplyNerdModulesOrder();

	protected abstract NerdSketch buildImpl(String[] p_javaMainArgs);
	// endregion

	// region Renderer selection.
	// Do we actually need to have this renderer around?
	// I have seen computers too old for OpenGL `3.0`!
	// I guess it's okay for 2D games...?
	public NerdCustomSketchBuilder useJavaRenderer() {
		this.SKETCH_KEY.renderer = PConstants.JAVA2D;
		return this;
	}

	// I won't be working with this UI framework any day...?
	/*
	 * public NerdCustomSketchBuilder usesJavaFxRenderer() {
	 * this.SKETCH_KEY.renderer = PConstants.FX2D;
	 * return this;
	 * }
	 */
	// endregion

	// region Adding listeners.
	public NerdCustomSketchBuilder addSketchConstructionListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.sketchConstructedListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addSketchDisposalListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.disposalListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addSketchSetupListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.setupListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addPreListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.preListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addPostListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.postListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addPreDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.preDrawListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.drawListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addPostDrawListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.postDrawListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}

	public NerdCustomSketchBuilder addSketchExitListener(final Consumer<NerdSketch> p_listener) {
		this.SKETCH_KEY.exitListeners.add(Objects.requireNonNull(
				p_listener, NerdCustomSketchBuilder.NULL_ERR_MSG));
		return this;
	}
	// endregion

	public <ExtT extends NerdExtension> NerdCustomSketchBuilder addNerdExt(
			final String p_extName,
			final Class<? extends NerdExtension> p_extClass,
			final UnaryOperator<NerdExtSettings<ExtT>> p_extSettingsSupplier) {
		try {
			final NerdExtension extObject = p_extClass.getConstructor(NerdCustomSketchBuilder.class).newInstance(this);
			this.SKETCH_KEY.nerdExtensions.put(p_extName, extObject);
			extObject.setExtSettings(p_extSettingsSupplier);
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("Could not construct a `" + NerdExtension.class.getSimpleName() + "` object!");
		}

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

	public NerdCustomSketchBuilder setEcsSystemOrder(
			final Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] p_ecsSystems) {
		this.SKETCH_KEY.ecsSystemOrder = p_ecsSystems;
		return this;
	}

	public NerdCustomSketchBuilder setScenesModuleSettings(final Consumer<NerdScenesModuleSettings> p_settingsBuilder) {
		final NerdScenesModule.NerdScenesModuleSettings toPass = new NerdScenesModule.NerdScenesModuleSettings();

		if (p_settingsBuilder != null)
			p_settingsBuilder.accept(toPass);

		this.SKETCH_KEY.scenesModuleSettings = toPass;
		return this;
	}

	public NerdCustomSketchBuilder setScenesModuleSettings(final NerdScenesModuleSettings p_settings) {
		if (p_settings != null)
			this.SKETCH_KEY.scenesModuleSettings = p_settings;
		return this;
	}

	public NerdCustomSketchBuilder setStringTablePath(final String p_path) {
		this.SKETCH_KEY.stringTablePath = p_path;
		return this;
	}

	public NerdCustomSketchBuilder setFirstScene(final Class<? extends NerdScene> p_firstScene) {
		// Objects.requireNonNull(p_firstScene, "The first scene needs to be set, and
		// cannot be `null`!");
		this.SKETCH_KEY.firstSceneClass = p_firstScene;
		return this;
	}

	public NerdCustomSketchBuilder setAntiAliasing(final int p_value) {
		this.SKETCH_KEY.antiAliasing = p_value;
		return this;
	}
	// endregion

	// region Window behaviors and properties.
	public NerdCustomSketchBuilder canResize() {
		this.SKETCH_KEY.canResize = true;
		return this;
	}

	public NerdCustomSketchBuilder startFullscreen() {
		this.SKETCH_KEY.startedFullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder preventCloseOnEscape() {
		this.SKETCH_KEY.preventCloseOnEscape = true;
		return this;
	}

	public NerdCustomSketchBuilder disableFullscreenInitially() {
		this.SKETCH_KEY.cannotFullscreen = false;
		return this;
	}

	public NerdCustomSketchBuilder disableF11FullscreenInitially() {
		this.SKETCH_KEY.cannotF11Fullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder disableAltEnterFullscreenInitially() {
		this.SKETCH_KEY.cannotAltEnterFullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder setIconPath(final String p_pathString) {
		this.SKETCH_KEY.iconPath = p_pathString;
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
