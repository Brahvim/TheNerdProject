package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.utils.NerdReflectionUtils;
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
 * beyond just... <i>using callbacks from it?</i> Extend it!?
 *
 * <p>
 * Override/Implement {@linkplain NerdSketchBuilder#build()
 * NerdSketchBuilder::build()}, and return an instance of your own
 * {@linkplain NerdSketch NerdSketch<SketchPGraphicsT>} subclass!
 * <p>
 * This is it! This is how you can hack more things in!
 */
public class NerdSketchBuilder<SketchPGraphicsT extends PGraphics> {

	// region Fields and constructor!
	public static final String NULL_ERR_MSG = "A listener passed to `NerdSketchSettings` cannot be `null`";
	protected Function<NerdSketchSettings<SketchPGraphicsT>, NerdSketch<SketchPGraphicsT>> sketchFxn = null;
	protected Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>>> modulesConsumer = null;
	protected final NerdSketchSettings<SketchPGraphicsT> BUILD_SETTINGS;

	public NerdSketchBuilder() {
		final Class<? extends PGraphics> rendererClass = NerdReflectionUtils.getFirstTypeArg(this);

		this.BUILD_SETTINGS = new NerdSketchSettings<>();

		if (rendererClass == PGraphics2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.P2D;
		else if (rendererClass == PGraphics3D.class)
			this.BUILD_SETTINGS.renderer = PConstants.P3D;
		else if (rendererClass == PGraphicsJava2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.JAVA2D;
		else if (rendererClass == PGraphicsFX2D.class)
			this.BUILD_SETTINGS.renderer = PConstants.FX2D;

		// Unsupported renderers!...:
		else if (rendererClass == PGraphicsSVG.class)
			// this.BUILD_SETTINGS.renderer = PConstants.SVG;
			throw new IllegalArgumentException(String.format(
					"`%s` is an unsupported renderer. Sorry.",
					PGraphicsSVG.class.getName()));

		else if (rendererClass == PGraphicsPDF.class)
			// this.BUILD_SETTINGS.renderer = PConstants.PDF;
			throw new IllegalArgumentException(String.format(
					"`%s` is an unsupported renderer. Sorry.",
					PGraphicsPDF.class.getName()));
		// ...
		// ...Unreal:
		else if (rendererClass == PGraphics.class)
			throw new IllegalArgumentException("Those renderers are not real!");
		else if (rendererClass == PGraphicsOpenGL.class)
			throw new IllegalArgumentException("Those renderers are not real!");
		else
			throw new IllegalArgumentException("That's not a real type...");

	}

	public NerdSketchBuilder(
			final Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>>> p_set) {
		this();
		this.modulesConsumer = p_set;
	}

	public NerdSketchBuilder(
			final Function<NerdSketchSettings<SketchPGraphicsT>, NerdSketch<SketchPGraphicsT>> p_object) {
		this();
		this.sketchFxn = p_object;
	}

	public NerdSketchBuilder(
			final Function<NerdSketchSettings<SketchPGraphicsT>, NerdSketch<SketchPGraphicsT>> p_object,
			final Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>>> p_set) {
		this();
		this.sketchFxn = p_object;
		this.modulesConsumer = p_set;
	}
	// endregion

	// region Building.
	// Getting `p_javaMainArgs` passed in here to allow for fake arguments.
	// I know `sun.java.command` exists!
	// (System property containing the strings passed to `main` with spaces between
	// them. Accessible via `System.getProperty()` like all other ones!)

	public final NerdSketch<SketchPGraphicsT> build() {
		return this.build(new String[0]);
	}

	public final NerdSketch<SketchPGraphicsT> build(final String[] p_javaMainArgs) {
		// `NerdModule`s are constructed by the `NerdSketch<SketchPGraphicsT>`
		// constructor.

		// In order to decrease code over there, and make it easier to modify
		// the `NerdModule`s used, ...or their order, we keep the code for initializing
		// them ready, beforehand!:

		this.BUILD_SETTINGS.nerdModulesInstantiator = s -> {
			final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>> //
			/*	*/ userDefinedModules = new LinkedHashSet<>(0, 1);
			this.supplyUserDefinedModules(userDefinedModules);

			for (final Function<NerdSketch<SketchPGraphicsT>, NerdModule> f : this.supplyDefaultModules())
				s.add(f);

			for (final Function<NerdSketch<SketchPGraphicsT>, NerdModule> f : userDefinedModules)
				s.add(f);
		};

		final NerdSketch<SketchPGraphicsT> constructedSketch = this
				.createNerdSketch(p_javaMainArgs, this.BUILD_SETTINGS);
		NerdSketchBuilder.runSketch(constructedSketch, p_javaMainArgs);
		return constructedSketch;
	}

	private LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>> supplyDefaultModules() {
		final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>> toRet = new LinkedHashSet<>(5);

		toRet.add(NerdSketch.NerdSketchOnlyAssetsModule::new);

		toRet.add(NerdDisplayModule::new);

		toRet.add(NerdWindowModule::createWindowModule);

		toRet.add(NerdInputModule::new);

		toRet.add(NerdScenesModule::new);

		return toRet;
	}

	public static <SketchPGraphicsT extends PGraphics> NerdSketch<SketchPGraphicsT> runSketch(
			final NerdSketch<SketchPGraphicsT> p_sketch) {
		return NerdSketchBuilder.runSketch(p_sketch, null);
	}

	public static <SketchPGraphicsT extends PGraphics> NerdSketch<SketchPGraphicsT> runSketch(
			final NerdSketch<SketchPGraphicsT> p_sketch, final String[] p_javaMainArgs) {
		final String[] args = new String[] { p_sketch.getClass().getName() };

		if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
			PApplet.runSketch(args, p_sketch);
		else
			PApplet.runSketch(PApplet.concat(args, p_javaMainArgs), p_sketch);
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
			final LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>> p_set) {
		if (this.modulesConsumer == null)
			return;

		this.modulesConsumer.accept(p_set);
	}

	/**
	 * This <i>actually</i> creates the {@link NerdSketch} instance! (<i>Id est,</i>
	 * does the builder object's work.)
	 *
	 * @param p_javaMainArgs is an array of string that can be obtained through
	 *                       either:
	 *                       <ul>
	 *
	 *                       <li>The array of arguments passed to {@code main()},
	 *                       </li>
	 *
	 *                       <li>A fake array of strings you made yourself,</li>
	 *
	 *                       <li>
	 *
	 *                       <pre>
	 *                       System.getProperty("sun.java.command");
	 *                       </pre>
	 *
	 *                       </li>
	 *
	 *                       </ul>
	 *
	 * @param p_settings     are the {@link NerdSketchSettings} you want to use.
	 * @return The {@link NerdSketch} instance you want to create using this builder
	 *         object.
	 *
	 * @apiNote The default implementation simply calls
	 *          {@linkplain Function#apply(Object) Function::apply()} on
	 *          {@linkplain NerdSketchBuilder#sketchFxn
	 *          NerdSketchBuilder::sketchFxn}, given it is not {@code null}. If it
	 *          is, the default constructor of {@link NerdSketch},
	 *          {@linkplain NerdSketch#NerdSketch(NerdSketchSettings)
	 *          NerdSketch::NerdSketch(NerdSketchSettings)} is called, and the
	 *          created instance is returned - no custom classes!
	 */
	protected NerdSketch<SketchPGraphicsT> createNerdSketch(
			final NerdSketchSettings<SketchPGraphicsT> p_settings) {
		if (this.sketchFxn == null)
			return new NerdSketch<>(p_settings);

		return this.sketchFxn.apply(p_settings);
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

	public NerdSketchBuilder<SketchPGraphicsT> setTitle(final String p_name) {
		this.BUILD_SETTINGS.initialWindowTitle = p_name;
		return this;
	}
	// endregion

	public NerdSketchBuilder<SketchPGraphicsT> setAntiAliasing(final int p_value) {
		this.BUILD_SETTINGS.antiAliasing = p_value;
		return this;
	}

	public <ModuleT extends NerdModule> NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass, final NerdModuleSettings<ModuleT> p_settings) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settings);
		return this;
	}

	public <ModuleT extends NerdModule> NerdSketchBuilder<SketchPGraphicsT> setNerdModuleSettings(
			final Class<ModuleT> p_moduleClass, final Supplier<NerdModuleSettings<ModuleT>> p_settingsSupplier) {
		this.BUILD_SETTINGS.nerdModulesSettings.put(p_moduleClass, p_settingsSupplier.get());
		return this;
	}
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

	public NerdSketchBuilder<SketchPGraphicsT> preventCloseOnEscapeInitially() {
		this.BUILD_SETTINGS.preventCloseOnEscape = true;
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
