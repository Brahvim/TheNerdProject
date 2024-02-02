package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.awt.PGraphicsJava2D;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.javafx.PGraphicsFX2D;
import processing.opengl.PGraphics2D;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.pdf.PGraphicsPDF;
import processing.svg.PGraphicsSVG;

/**
 * Want to hack into the {@link NerdSketch} class and control its inner workings
 * beyond just... <i>using callbacks from it?</i>
 * <p>
 * ...Then extend it!
 *
 * <p>
 * Override/Implement {@linkplain NerdSketchBuilder#build()
 * NerdSketchBuilder::build()} (or the builder for your favorite
 * renderer-specific subclass of {@code NerdSketch}), and return an instance of
 * your own {@linkplain NerdSketch NerdSketch<SketchPGraphicsT>} subclass!
 * <p>
 * This is it! This is how you can hack more things in!
 *
 * @apiNote See {@link NerdSketchSettings} for specific details on data being
 *          passed here.
 */
public abstract class NerdSketchBuilder<SketchPGraphicsT extends PGraphics> {

	public static interface NerdSketchModulesSetConsumer<SketchPGraphicsT extends PGraphics>
			extends Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>>> {
	}

	public static interface NerdSketchConstructorFunction<SketchPGraphicsT extends PGraphics>
			extends Function<NerdSketchSettings<SketchPGraphicsT>, NerdSketch<SketchPGraphicsT>> {
	}

	// region Fields and constructor!
	protected static final String NULL_SETTS_ERR_MSG = "A listener passed to `NerdSketchSettings` cannot be `null`";

	protected final NerdSketchSettings<SketchPGraphicsT> BUILD_SETTINGS;

	protected NerdSketchModulesSetConsumer<SketchPGraphicsT> modulesConsumer = null;
	protected NerdSketchConstructorFunction<SketchPGraphicsT> sketchConstructor = null;

	/**
	 * @see {@linkplain NerdSketchBuilder#supplyDefaultModules()
	 *      NerdSketchBuilder::supplyDefaultModules()}.
	 */
	private static final int NUM_DEFAULT_MODULES = 4;

	protected NerdSketchBuilder(final Class<? extends PGraphics> p_rendererClass) {
		this.BUILD_SETTINGS = new NerdSketchSettings<>();

		if (p_rendererClass == PGraphics2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.P2D;
		else if (p_rendererClass == PGraphics3D.class)
			this.BUILD_SETTINGS.renderer = PConstants.P3D;
		else if (p_rendererClass == PGraphicsJava2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.JAVA2D;
		else if (p_rendererClass == PGraphicsFX2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.FX2D;

		// Unsupported renderers!...:
		else if (p_rendererClass == PGraphicsSVG.class)
			// this.BUILD_SETTINGS.renderer = PConstants.SVG;
			throw new IllegalArgumentException(String.format(
					"`%s` is an unsupported renderer. Sorry.",
					PGraphicsSVG.class.getName()));

		else if (p_rendererClass == PGraphicsPDF.class)
			// this.BUILD_SETTINGS.renderer = PConstants.PDF;
			throw new IllegalArgumentException(String.format(
					"`%s` is an unsupported renderer. Sorry.",
					PGraphicsPDF.class.getName()));
		// ...
		// ...Unreal:
		else if (p_rendererClass == PGraphics.class)
			throw new IllegalArgumentException("Those renderers are not real!");
		else if (p_rendererClass == PGraphicsOpenGL.class)
			throw new IllegalArgumentException("Those renderers are not real!");
		else
			throw new IllegalArgumentException("That's not a real type...");
	}

	protected NerdSketchBuilder(
			final Class<? extends PGraphics> p_rendererClass,
			final NerdSketchBuilder.NerdSketchConstructorFunction<SketchPGraphicsT> p_sketchConstructor) {
		this(p_rendererClass);
		this.sketchConstructor = p_sketchConstructor;
	}

	protected NerdSketchBuilder(
			final Class<? extends PGraphics> p_rendererClass,
			final NerdSketchModulesSetConsumer<SketchPGraphicsT> p_modulesSet) {
		this(p_rendererClass);
		this.modulesConsumer = p_modulesSet;
	}

	protected NerdSketchBuilder(
			final Class<? extends PGraphics> p_rendererClass,
			final NerdSketchConstructorFunction<SketchPGraphicsT> p_sketchConstructor,
			final NerdSketchModulesSetConsumer<SketchPGraphicsT> p_modulesSet) {
		this(p_rendererClass);
		this.modulesConsumer = p_modulesSet;
		this.sketchConstructor = p_sketchConstructor;
	}
	// endregion

	// region Building.
	// Getting `p_sketchArgs` passed in here to allow for fake arguments.
	// I know `sun.java.command` exists!
	// (System property containing the strings passed to `main` with spaces between
	// them. Accessible via `System.getProperty()` like all other ones!)

	public final NerdSketch<SketchPGraphicsT> build() {
		// Faster than passing an empty array:
		return this.build(null); // this.build(new String[0]);
	}

	public final NerdSketch<SketchPGraphicsT> build(final String[] p_sketchArgs) {
		// `NerdModule`s are constructed by the `NerdSketch<SketchPGraphicsT>`
		// constructor.

		// In order to decrease code over there, and make it easier to modify
		// the `NerdModule`s used, ...or their order, we keep the code for initializing
		// them ready, beforehand!:

		this.BUILD_SETTINGS.nerdModulesInstantiator = s -> {
			final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>>
			/*	*/ userDefinedModules = new LinkedHashSet<>(0, 1);
			this.supplyUserDefinedModules(userDefinedModules);

			for (final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> f : this
					.supplyDefaultModules())
				s.add(f);

			for (final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> f : userDefinedModules)
				s.add(f);
		};

		final NerdSketch<SketchPGraphicsT>
		/*   */ constructedSketch = this.createNerdSketch(this.BUILD_SETTINGS);

		NerdSketchBuilder.runSketch(constructedSketch, p_sketchArgs);
		return constructedSketch;
	}

	protected static int getNumDefaultModules() {
		return NerdSketchBuilder.NUM_DEFAULT_MODULES;
	}

	private LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>> supplyDefaultModules() {
		final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>>
		/*   */ toRet = new LinkedHashSet<>(NerdSketchBuilder.getNumDefaultModules());

		toRet.add(NerdSketch.NerdSketchOnlyAssetsModule::new);

		toRet.add(NerdDisplayModule::new);

		toRet.add(NerdWindowModule::createWindowModule);

		toRet.add(NerdInputModule::new);

		return toRet;
	}

	public static <SketchPGraphicsT extends PGraphics> NerdSketch<SketchPGraphicsT> //
			runSketch(final NerdSketch<SketchPGraphicsT> p_sketch) {
		return NerdSketchBuilder.runSketch(p_sketch, null);
	}

	public static <SketchPGraphicsT extends PGraphics> NerdSketch<SketchPGraphicsT> //
			runSketch(final NerdSketch<SketchPGraphicsT> p_sketch, final String[] p_sketchArgs) {
		final String[] args = new String[] { p_sketch.getClass().getName() };

		if (p_sketchArgs == null || p_sketchArgs.length == 0)
			PApplet.runSketch(args, p_sketch);
		else
			PApplet.runSketch(PApplet.concat(args, p_sketchArgs), p_sketch);
		return p_sketch;
	}
	// endregion

	/**
	 * Provides a {@link LinkedHashSet}, to be filled with
	 * {@link NerdModule} instances, given a {@link NerdSketch}.
	 *
	 * <p>
	 * The order in which the {@link NerdModule}s are added, is the order the
	 * {@link NerdSketch} will process them in. If the same
	 * {@link NerdModule} is added twice, only the latter instance will be retained.
	 *
	 * <p>
	 * Some {@link NerdModule}s are provided by default in the
	 * {@link NerdSketch}. If those are added once again, their order in the
	 * {@link NerdModule} calling pipeline will change. Duplicates are not allowed.
	 * Once added, the {@link NerdModule} stays until removed.
	 *
	 * @apiNote This method now simply calls {@linkplain Consumer#accept(Object)
	 *          Consumer::accept(Object)} on
	 *          {@linkplain NerdSketchBuilder#modulesConsumer
	 *          NerdSketchBuilder::modulesConsumer} if it isn't {@code null}.
	 */
	protected void supplyUserDefinedModules(
			final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>> p_modulesSet) {
	}

	/**
	 * This <i>actually</i> creates the {@link NerdSketch} instance! (<i>Id est,</i>
	 * does the builder object's work.)
	 *
	 * @param p_settings are the {@link NerdSketchSettings} you want to use.
	 * @return The {@link NerdSketch} instance you want to create using this builder
	 *         object.
	 *
	 * @apiNote The default implementation simply calls
	 *          {@linkplain Function#apply(Object) Function::apply()} on
	 *          {@linkplain NerdSketchBuilder#sketchConstructor
	 *          NerdSketchBuilder::sketchFxn}, given it is not {@code null}. If it
	 *          is, the default constructor of {@link NerdSketch},
	 *          {@linkplain NerdSketch#NerdSketch(NerdSketchSettings)
	 *          NerdSketch::NerdSketch(NerdSketchSettings)} is called, and the
	 *          created instance is returned - no custom classes!
	 */
	protected NerdSketch<SketchPGraphicsT> createNerdSketch(
			final NerdSketchSettings<SketchPGraphicsT> p_settings) {
		if (this.sketchConstructor == null)
			return new NerdSketch<>(p_settings);

		return this.sketchConstructor.apply(p_settings);
	}

	// region `set()`.
	// region Window settings!
	public NerdSketchBuilder<SketchPGraphicsT> setWidth(final int p_width) {
		this.BUILD_SETTINGS.width = p_width;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setHeight(final int p_height) {
		this.BUILD_SETTINGS.height = p_height;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setInitialWindowTitle(final String p_name) {
		this.BUILD_SETTINGS.initialWindowTitle = p_name;
		return this;
	}
	// endregion

	public NerdSketchBuilder<SketchPGraphicsT> setFrameRateLimit(final int p_value) {
		this.BUILD_SETTINGS.frameRateLimit = p_value;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setAntiAliasing(final int p_value) {
		this.BUILD_SETTINGS.antiAliasing = p_value;
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final NerdModuleSettings<SketchPGraphicsT, ModuleT> p_settings) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_settings.getModuleClass(), p_settings);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final Supplier<NerdModuleSettings<SketchPGraphicsT, ModuleT>> p_settingsSupplier) {
		final NerdModuleSettings<SketchPGraphicsT, ModuleT> settings = p_settingsSupplier.get();
		this.BUILD_SETTINGS.nerdModulesSettings.put(settings.getModuleClass(), settings);
		return this;
	}

	// public <ModuleT extends NerdModule<SketchPGraphicsT>>
	// NerdSketchBuilder<SketchPGraphicsT>
	// setNerdModuleSettings(
	// final Class<ModuleT> p_moduleClass, final
	// NerdModuleSettings<SketchPGraphicsT, ModuleT>
	// p_settings) {
	// this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settings);
	// return this;
	// }

	// public <ModuleT extends NerdModule<SketchPGraphicsT>>
	// NerdSketchBuilder<SketchPGraphicsT>
	// setNerdModuleSettings(
	// final Class<ModuleT> p_moduleClass, final
	// Supplier<NerdModuleSettings<SketchPGraphicsT, ModuleT>> p_settingsSupplier) {
	// this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass,
	// p_settingsSupplier.get());
	// return this;
	// }
	// endregion

	// region Window behaviors and properties.
	public NerdSketchBuilder<SketchPGraphicsT> canResize() {
		this.BUILD_SETTINGS.canResize = true;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> startFullscreenInitially() {
		this.BUILD_SETTINGS.shouldStartFullscreen = true;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> disableFullscreenInitially() {
		this.BUILD_SETTINGS.canFullscreen = false;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> closeOnPressingEscapeInitially() {
		this.BUILD_SETTINGS.preventCloseOnEscape = false;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> disableF11FullscreenInitially() {
		this.BUILD_SETTINGS.canF11Fullscreen = false;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> disableAltEnterFullscreenInitially() {
		this.BUILD_SETTINGS.canAltEnterFullscreen = false;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setWindowIconPath(final String p_pathString) {
		this.BUILD_SETTINGS.windowIconPath = p_pathString;
		return this;
	}
	// endregion

}
