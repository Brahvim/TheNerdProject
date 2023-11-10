package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
// import com.brahvim.nerd.processing_wrapper.NerdSketch.NerdSketchOnlyAssetsModule;
import com.brahvim.nerd.necessary_modules.NerdDisplayModule;
import com.brahvim.nerd.necessary_modules.NerdInputModule;
import com.brahvim.nerd.necessary_modules.NerdWindowModule;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Want to hack into the {@link NerdSketch} class and control its inner workings
 * beyond just... using callbacks? Why not extend it!?
 *
 * <p>
 * Override/Implement {@link NerdSketchBuilder#build()}, and return an
 * instance of your own {@link NerdSketch} subclass!
 */
public abstract class NerdSketchBuilder {

	// region Fields and constructor!
	public static final String NULL_ERR_MSG = "A listener passed to `NerdSketchSettings` cannot be `null`";

	private final NerdSketchSettings BUILD_SETTINGS;

	protected NerdSketchBuilder() {
		this.BUILD_SETTINGS = new NerdSketchSettings();
	}
	// endregion

	// region Building.
	// Getting `p_javaMainArgs` passed in here to allow for fake arguments.
	// I know `sun.java.command` exists!
	// (System property containing the strings passed to `main` with spaces between
	// them. Accessible via `System.getProperty()` like all other ones!)

	public final NerdSketch build() {
		return this.build(new String[0]);
	}

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
		NerdSketchBuilder.runSketch(constructedSketch, p_javaMainArgs);
		return constructedSketch;
	}

	private LinkedHashSet<Function<NerdSketch, NerdModule>> supplyDefaultModules() {
		final LinkedHashSet<Function<NerdSketch, NerdModule>> toRet = new LinkedHashSet<>(5);

		// toRet.add(NerdSketchOnlyAssetsModule::new);

		toRet.add(NerdDisplayModule::new);

		toRet.add(NerdWindowModule::createWindowModule);

		toRet.add(NerdInputModule::new);

		toRet.add(NerdScenesModule::new);

		return toRet;
	}

	public static void runSketch(final NerdSketch p_sketch) {
		NerdSketchBuilder.runSketch(p_sketch, null);
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

	protected abstract NerdSketch createNerdSketch(String[] p_javaMainArgs, NerdSketchSettings p_settings);

	public NerdSketchBuilder setRenderer() {
		this.BUILD_SETTINGS.renderer = PConstants.JAVA2D;
		return this;
	}

	// region `set()`.
	// region Window settings!
	public NerdSketchBuilder setWidth(final int p_width) {
		this.BUILD_SETTINGS.width = p_width;
		return this;
	}

	public NerdSketchBuilder setHeight(final int p_height) {
		this.BUILD_SETTINGS.height = p_height;
		return this;
	}

	public NerdSketchBuilder setTitle(final String p_name) {
		this.BUILD_SETTINGS.initialWindowTitle = p_name;
		return this;
	}
	// endregion

	public NerdSketchBuilder setAntiAliasing(final int p_value) {
		this.BUILD_SETTINGS.antiAliasing = p_value;
		return this;
	}

	public <ModuleT extends NerdModule> NerdSketchBuilder setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass,
			final NerdModuleSettings<ModuleT> p_settings) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settings);
		return this;
	}

	public <ModuleT extends NerdModule> NerdSketchBuilder setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass,
			final Supplier<NerdModuleSettings<?>> p_settingsSupplier) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settingsSupplier.get());
		return this;
	}
	// endregion

	// region Window behaviors and properties.
	public NerdSketchBuilder canResize() {
		this.BUILD_SETTINGS.canResize = true;
		return this;
	}

	public NerdSketchBuilder startFullscreenInitially() {
		this.BUILD_SETTINGS.shouldStartFullscreen = true;
		return this;
	}

	public NerdSketchBuilder disableFullscreenInitially() {
		this.BUILD_SETTINGS.canFullscreen = false;
		return this;
	}

	public NerdSketchBuilder preventCloseOnEscapeInitially() {
		this.BUILD_SETTINGS.preventCloseOnEscape = true;
		return this;
	}

	public NerdSketchBuilder disableF11FullscreenInitially() {
		this.BUILD_SETTINGS.canF11Fullscreen = false;
		return this;
	}

	public NerdSketchBuilder disableAltEnterFullscreenInitially() {
		this.BUILD_SETTINGS.canAltEnterFullscreen = false;
		return this;
	}

	public NerdSketchBuilder setWindowIconPath(final String p_pathString) {
		this.BUILD_SETTINGS.windowIconPath = p_pathString;
		return this;
	}
	// endregion

}
