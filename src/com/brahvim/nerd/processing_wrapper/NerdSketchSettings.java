package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdStringTable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class NerdSketchSettings<SketchPGraphicsT extends PGraphics> {

	// region Non-Boolean settings.
	/** What {@link NerdSketch#settings()} passes to {@link PApplet#smooth()} */
	public int antiAliasing = 2;

	/**
	 * One of the initial dimensions for the sketch's window - they're
	 * {@code 640x480} by default!
	 */
	public int width = 640, height = 480;

	/** The path to the icon for the window started by Processing. */
	public String windowIconPath = "";

	/**
	 * The path to the global {@link NerdStringTable} within the {@link NerdSketch}.
	 */
	public String stringTablePath = "";

	/** */
	public String initialWindowTitle = "";

	/** */
	public String renderer = PConstants.P3D;

	/**
	 * Holds the method that is called when the {@link NerdSketch} needs to know
	 * what {@link NerdModule}s are to be loaded.
	 */
	public Consumer<LinkedHashSet<Function<NerdSketch<SketchPGraphicsT>, NerdModule>>> nerdModulesInstantiator;

	/** */
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
