package com.brahvim.nerd.processing_wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdStringsTable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class NerdSketchSettings<SketchPGraphicsT extends PGraphics> {

	/**
	 * @see {@linkplain NerdSketchBuilder#supplyDefaultModules()
	 *      NerdSketchBuilder::supplyDefaultModules()}.
	 */
	protected final int NUM_DEFAULT_MODULES;

	public NerdSketchSettings(final int p_numModules) {
		this.NUM_DEFAULT_MODULES = p_numModules;
	}

	protected final class NerdModulesAndSettingsMap {

		protected final Map<Class<? extends NerdModule<SketchPGraphicsT>>, Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>>>
		/*   */ CLASSES_TO_MODULES_MAP = new LinkedHashMap<>(NerdSketchSettings.this.NUM_DEFAULT_MODULES);
		protected final Map<Class<? extends NerdModule<SketchPGraphicsT>>, NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>>
		/*   */ MODULE_CLASSES_TO_SETTINGS_MAP = new LinkedHashMap<>(NerdSketchSettings.this.NUM_DEFAULT_MODULES);

		@SafeVarargs
		public final void addNerdModules(final Class<? extends NerdModule<SketchPGraphicsT>>... p_moduleClasses) {
			for (final var c : p_moduleClasses)
				this.addNerdModule(c);
		}

		@SafeVarargs
		public final void setNerdModuleSettings(
				final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>>... p_settings) {
			for (final var i : p_settings)
				this.setNerdModuleSettings(i);
		}

		public void setNerdModuleSettings(
				final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings) {
			this.MODULE_CLASSES_TO_SETTINGS_MAP.put(p_settings.getNerdModuleClass(), p_settings);
		}

		public void addNerdModule(final Class<? extends NerdModule<SketchPGraphicsT>> p_moduleClass) {
			this.CLASSES_TO_MODULES_MAP.put(p_moduleClass,
					s -> {
						try {
							return p_moduleClass.getDeclaredConstructor(NerdSketch.class).newInstance(s);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
						return null;
					});
		}

		public void addNerdModule(
				final Class<? extends NerdModule<SketchPGraphicsT>> p_moduleClass,
				final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings) {
			this.addNerdModule(p_moduleClass);
			this.MODULE_CLASSES_TO_SETTINGS_MAP.put(p_moduleClass, p_settings);
		}

		public void addNerdModule(
				final Class<? extends NerdModule<SketchPGraphicsT>> p_moduleClass,
				final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
			this.CLASSES_TO_MODULES_MAP.put(p_moduleClass, p_moduleSupplier);
		}

		public void addNerdModule(
				final Class<? extends NerdModule<SketchPGraphicsT>> p_moduleClass,
				final NerdModuleSettings<SketchPGraphicsT, NerdModule<SketchPGraphicsT>> p_settings,
				final Function<NerdSketch<SketchPGraphicsT>, NerdModule<SketchPGraphicsT>> p_moduleSupplier) {
			this.CLASSES_TO_MODULES_MAP.put(p_moduleClass, p_moduleSupplier);
			this.MODULE_CLASSES_TO_SETTINGS_MAP.put(p_moduleClass, p_settings);
		}

	}

	// region Non-Boolean settings.
	/**
	 * What {@linkplain NerdSketch#setup() NerdSketch::setup()}
	 * passes to {@linkplain PApplet#frameRate() PApplet::frameRate()}.
	 * <p>
	 * {@code 0} by default.
	 * 
	 * @apiNote
	 *          Any values <i>below</i> {@code 1} indicate a request for the
	 *          framerate to be set according to the <b>default</b> display's
	 *          refresh rate.
	 *          <p>
	 *          Processing automatically sets the framerate to {@code 1} if it is
	 *          lesser, and logs a message about it.
	 */
	public int frameRateLimit = 0;

	/**
	 * What {@linkplainNerdSketch#settings() NerdSketch::settings()}
	 * passes to {@linkplain PApplet#smooth() PApplet::smooth()}.
	 * <p>
	 * {@code 2} by default.
	 */
	public int antiAliasing = 2;

	/**
	 * One of the initial dimensions for the sketch's window - they're
	 * {@code 640x480} by default!
	 */
	public int width = 640, height = 480;

	/**
	 * The path to the icon for the window started by Processing.
	 * <p>
	 * {@code ""} by default.
	 */
	public String windowIconPath = "";

	/**
	 * The path to the global {@link NerdStringsTable} within the
	 * {@link NerdSketch}.
	 * <p>
	 * {@code ""} by default.
	 */
	public String stringTablePath = "";

	/**
	 * The title of the window in the beginning.
	 * <p>
	 * {@code "The Nerd Project"} by default.
	 */
	public String initialWindowTitle = "The Nerd Project";

	/**
	 * The {@code PConstants} constant that describes what renderer the sketch will
	 * use. {@linkplain NerdSketch#sketchRenderer() NerdSketch::sketchRenderer()}
	 * a.k.a. {@linkplain PApplet#sketchRenderer() PApplet::sketchRenderer()}
	 * returns the same.
	 *
	 * <p>
	 * {@linkplain PConstants#P3D PConstants.P3D} by default.
	 */
	public String renderer = PConstants.P3D;

	/**
	 * This holds the {@link NerdModule} {@link NerdModuleSettings} of the
	 * {@link NerdModule}s you want the engine to use.
	 */
	public NerdSketchSettings<SketchPGraphicsT>.NerdModulesAndSettingsMap nerdModulesAndSettings = this.new NerdModulesAndSettingsMap();
	// endregion

	// region Booleans.
	/**
	 * Dictates if the sketch can ever resize the window at all.
	 * <p>
	 * {@code true} by default.
	 */
	public boolean canResize = true;

	/**
	 * Dictates if the sketch can ever enter fullscreen at all.
	 * <p>
	 * {@code true} by default.
	 */
	public boolean canFullscreen = true;

	/**
	 * Dictates if the sketch can enter fullscreen via the {@code F11} key.
	 * <p>
	 * {@code true} by default.
	 */
	public boolean canF11Fullscreen = true;

	/**
	 * Should the sketch stop the default behavior of exiting when {@code Esc} is
	 * pressed?
	 * <p>
	 * {@code true} by default.
	 */
	public boolean preventCloseOnEscape = true;

	/**
	 * Should the sketch be started in fullscreen?
	 * <p>
	 * {@code false} by default.
	 */
	public boolean shouldStartFullscreen = false;

	/**
	 * Dictates if the sketch can enter fullscreen via the {@code Alt} +
	 * {@code Enter} key combination.
	 * <p>
	 * {@code true} by default.
	 */
	public boolean canAltEnterFullscreen = true;

	/**
	 * Dictates if the sketch should start in the portrait orientation on Android.
	 * <p>
	 * {@code false} by default.
	 */
	public boolean shouldStartPortraitMode = false;
	// endregion

}
