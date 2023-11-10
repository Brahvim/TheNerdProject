package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdStringTable;

import processing.core.PApplet;
import processing.core.PConstants;

public class NerdSketchSettings {

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
	public Consumer<LinkedHashSet<Function<NerdSketch, NerdModule>>> nerdModulesInstantiator;

	/** */
	public Map<Class<? extends NerdModule>, NerdModuleSettings<?>> nerdModulesSettings = new HashMap<>(0);

	// region Booleans.
	/** Dictates if the sketch can ever resize the window at all. */
	public boolean canResize = true;

	/** Dictates if the sketch can ever enter fullscreen at all. */
	public boolean canFullscreen = false;

	/** Dictates if the sketch can enter fullscreen via the {@code F11} key. */
	public boolean canF11Fullscreen = false;

	/**
	 * Should the sketch stop the default behavior of exiting when {@code Esc} is
	 * pressed?
	 */
	public boolean preventCloseOnEscape = false;

	/** Should the sketch be started in fullscreen? */
	public boolean shouldStartFullscreen = false;

	/**
	 * Dictates if the sketch can enter fullscreen via the {@code Alt} +
	 * {@code Enter} key combination.
	 */
	public boolean canAltEnterFullscreen = false;
	// endregion

}
