package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch.NerdSketchOnlyAssetsModule;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdDisplayModule;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdInputModule;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdWindowModule;

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

	// region Fields and constructor!
	public static final String NULL_ERR_MSG = "A listener passed to `NerdSketchBuilderSettings` cannot be `null`";

	private final NerdSketchBuilderSettings BUILD_SETTINGS;

	protected NerdCustomSketchBuilder() {
		this.BUILD_SETTINGS = new NerdSketchBuilderSettings();
	}
	// endregion

	// region Building.
	// Getting `p_javaMainArgs` passed in here to allow for fake arguments.
	// I know `sun.java.command` exists!
	// (System property containing the strings passed to `main` with spaces between
	// them. Accessible via `System.getProperty()` like all other ones!)

	public final NerdSketch build(final String[] p_javaMainArgs) {
		// `NerdModule`s are constructed by the `NerdSketch` constructor.

		// In order to decrease code over there, and make it easier to modify
		// the `NerdModule`s used, ...or their order, we keep the code for initializing
		// them ready, beforehand!:

		this.BUILD_SETTINGS.nerdModulesInstantiator = s -> {
			final LinkedHashSet<Function<NerdSketch, NerdModule>> userDefinedModules = new LinkedHashSet<>(0, 1);
			this.supplyUserDefinedModules(userDefinedModules);

			for (final Function<NerdSketch, NerdModule> f : this.supplyDefaultModules())
				s.add(f);

			for (final Function<NerdSketch, NerdModule> f : userDefinedModules)
				s.add(f);
		};

		final NerdSketch constructedSketch = this.createNerdSketch(p_javaMainArgs, this.BUILD_SETTINGS);
		NerdCustomSketchBuilder.runSketch(constructedSketch, p_javaMainArgs);
		return constructedSketch;
	}

	private LinkedHashSet<Function<NerdSketch, NerdModule>> supplyDefaultModules() {
		final LinkedHashSet<Function<NerdSketch, NerdModule>> toRet = new LinkedHashSet<>(5);

		toRet.add(NerdSketchOnlyAssetsModule::new);

		toRet.add(NerdDisplayModule::new);

		toRet.add(NerdWindowModule::createWindowModule);

		toRet.add(NerdInputModule::new);

		toRet.add(NerdScenesModule::new);

		return toRet;
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
	// endregion

	/**
	 * Provides a {@link LinkedHashSet}, to be filled with
	 * {@link NerdModule} instances, given a {@link NerdSketch}.
	 *
	 * <p>
	 * The order in which the {@link NerdModule}s are added, is the order the
	 * {@link NerdSketch} will process them in. If the same {@link NerdModule} is
	 * added twice, only the latter instance will be retained.
	 *
	 * <p>
	 * Some {@link NerdModule}s are provided by default in the
	 * {@link NerdSketch}. If those are added once again, their order in the
	 * {@link NerdModule} calling pipeline will change. Duplicates are not allowed.
	 * Once added, the {@link NerdModule} stays until removed.
	 */
	protected abstract void supplyUserDefinedModules(LinkedHashSet<Function<NerdSketch, NerdModule>> p_set);

	protected abstract NerdSketch createNerdSketch(String[] p_javaMainArgs, NerdSketchBuilderSettings p_settings);

	public NerdCustomSketchBuilder useJavaRenderer() {
		this.BUILD_SETTINGS.renderer = PConstants.JAVA2D;
		return this;
	}

	// region `set()`.
	// region Window settings!
	public NerdCustomSketchBuilder setWidth(final int p_width) {
		this.BUILD_SETTINGS.width = p_width;
		return this;
	}

	public NerdCustomSketchBuilder setHeight(final int p_height) {
		this.BUILD_SETTINGS.height = p_height;
		return this;
	}

	public NerdCustomSketchBuilder setTitle(final String p_name) {
		this.BUILD_SETTINGS.name = p_name;
		return this;
	}
	// endregion

	public NerdCustomSketchBuilder setAntiAliasing(final int p_value) {
		this.BUILD_SETTINGS.antiAliasing = p_value;
		return this;
	}

	public <ModuleT extends NerdModule> NerdCustomSketchBuilder setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass,
			final NerdModuleSettings<ModuleT> p_settings) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settings);
		return this;
	}

	public <ModuleT extends NerdModule> NerdCustomSketchBuilder setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass,
			final Supplier<NerdModuleSettings<?>> p_settingsSupplier) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settingsSupplier.get());
		return this;
	}
	// endregion

	// region Window behaviors and properties.
	public NerdCustomSketchBuilder canResize() {
		this.BUILD_SETTINGS.canResize = true;
		return this;
	}

	public NerdCustomSketchBuilder startFullscreenInitially() {
		this.BUILD_SETTINGS.startedFullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder disableFullscreenInitially() {
		this.BUILD_SETTINGS.cannotFullscreen = false;
		return this;
	}

	public NerdCustomSketchBuilder preventCloseOnEscapeInitially() {
		this.BUILD_SETTINGS.preventCloseOnEscape = true;
		return this;
	}

	public NerdCustomSketchBuilder disableF11FullscreenInitially() {
		this.BUILD_SETTINGS.cannotF11Fullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder disableAltEnterFullscreenInitially() {
		this.BUILD_SETTINGS.cannotAltEnterFullscreen = true;
		return this;
	}

	public NerdCustomSketchBuilder setIconPath(final String p_pathString) {
		this.BUILD_SETTINGS.iconPath = p_pathString;
		return this;
	}
	// endregion

}
