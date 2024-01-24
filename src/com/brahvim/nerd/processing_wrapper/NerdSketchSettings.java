package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdStringsTable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class NerdSketchSettings<SketchPGraphicsT extends PGraphics> {

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
	 * Holds the method that is called when the {@link NerdSketch} needs to know
	 * what {@link NerdModule}s are to be loaded.
	 * <p>
	 * {@code true} by default.
	 */
	public Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>>> nerdModulesInstantiator;

	/**
	 * This holds the {@link NerdModuleSettings} of the {@link NerdModule}s you want
	 * the engine to use.
	 *
	 * @implNote Initialize using a {@link HashMap} with no allocation
	 *           ({@code 0} elements' worth of allocation):
	 *
	 *           <pre>
	 *           new HashMap<>(0);
	 *           </pre>
	 */
	public Map<Class<? extends NerdModule>, NerdModuleSettings<?>> nerdModulesSettings = new HashMap<>(0);
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
