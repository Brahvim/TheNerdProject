package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brahvim.nerd.processing_wrapper.NerdSketch.NerdSketchOnlyAssetsModule;
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
			extends
			Consumer<LinkedHashSet<BiFunction<NerdSketch<SketchPGraphicsT>, NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>>>> {
	}

	public static interface NerdSketchConstructorFunction<SketchPGraphicsT extends PGraphics>
			extends Function<NerdSketchSettings<SketchPGraphicsT>, NerdSketch<SketchPGraphicsT>> {
	}

	// region Fields and constructor!
	protected static final String NULL_SETTS_ERR_MSG = "A listener passed to `NerdSketchSettings` cannot be `null`";

	protected final NerdSketchSettings<SketchPGraphicsT> BUILD_SETTINGS;

	protected NerdSketchModulesSetConsumer<SketchPGraphicsT> modulesConsumer = null;
	protected NerdSketchConstructorFunction<SketchPGraphicsT> sketchConstructor = null;

	protected NerdSketchBuilder(final Class<? extends PGraphics> p_rendererClass) {
		this.BUILD_SETTINGS = new NerdSketchSettings<>(this.getNumDefaultModules());
		this.verifyRenderer(p_rendererClass);
		this.addDefaultModules();
	}

	protected void verifyRenderer(final Class<? extends PGraphics> p_rendererClass) {
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

	@SuppressWarnings("unchecked")
	protected void addDefaultModules() {
		// TODO: Get renderer-specific instances of these via abstract methods:

		this.addNerdModule(NerdSketchOnlyAssetsModule.class);
		this.addNerdModule(NerdDisplayModule.class);
		this.addNerdModule(NerdWindowModule.class, NerdWindowModule::createWindowModule);
		this.addNerdModule(NerdInputModule.class);
	}

	/**
	 * This is present only in the form of a method so the value can be modified
	 * without being passed into the constructor.
	 */
	protected int getNumDefaultModules() {
		return 4;
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
		// Faster than passing an empty array/:
		return this.build(null); // this.build(new String[0]);
	}

	public final NerdSketch<SketchPGraphicsT> build(final String[] p_sketchArgs) {
		// `NerdModule`s are constructed by the `NerdSketch` constructor:
		return NerdSketchBuilder.runSketch(this.createNerdSketch(this.BUILD_SETTINGS), p_sketchArgs);
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
			final LinkedHashSet<BiFunction<NerdSketch<SketchPGraphicsT>, NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>>> p_modulesSet) {
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
		// TODO
		if (this.sketchConstructor == null)
			return new NerdSketch<>(p_settings);

		return this.sketchConstructor.apply(p_settings);
	}

	// region `addNerdModule()` overloads.
	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> moduleClass) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(moduleClass);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> p_moduleClass,
			final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(p_moduleClass, p_settings);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> p_moduleClass,
			final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(p_moduleClass, p_moduleSupplier);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> p_moduleClass,
			final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings,
			final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(p_moduleClass, p_settings, p_moduleSupplier);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> p_moduleClass,
			final BiFunction<NerdSketch<SketchPGraphicsT>, NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(p_moduleClass, p_moduleSupplier);
		return this;
	}

	public <ModuleT extends NerdModule<SketchPGraphicsT>> NerdSketchBuilder<SketchPGraphicsT> addNerdModule(
			final Class<ModuleT> p_moduleClass,
			final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings,
			final BiFunction<NerdSketch<SketchPGraphicsT>, NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry.addNerdModule(p_moduleClass, p_settings, p_moduleSupplier);
		return this;
	}
	// endregion

	// region `set*()`.
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

	public NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry
				.setNerdModuleSettings(p_settings);
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final Supplier<NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>> p_settingsSupplier) {
		this.BUILD_SETTINGS.nerdModulesBuilderRegistry
				.setNerdModuleSettings(p_settingsSupplier.get());
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setFrameRateLimit(final int p_value) {
		this.BUILD_SETTINGS.frameRateLimit = p_value;
		return this;
	}

	public NerdSketchBuilder<SketchPGraphicsT> setAntiAliasing(final int p_value) {
		this.BUILD_SETTINGS.antiAliasing = p_value;
		return this;
	}
	// endregion

	// region Window behaviors and properties.
	public NerdSketchBuilder<SketchPGraphicsT> canResizeWindow() {
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
