package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdStringTable;

import processing.core.PConstants;

public class NerdSketchSettings {

	/** Should the sketch be started in fullscreen? */
	public boolean fullScreen;

	/**
	 * What {@link NerdSketch}{@code::settings()} passes to
	 * <a href="https://processing.org/reference/smooth_.html">PApplet::smooth()</a>
	 */
	public int antiAliasing = 2;

	/**
	 * One of the initial dimensions for the sketch's window. Is {@code 640x480} by default!
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

	/** */

	/** */
	public Consumer<LinkedHashSet<Function<NerdSketch, NerdModule>>> nerdModulesInstantiator;

	/** */
	public Map<Class<? extends NerdModule>, NerdModuleSettings> nerdModulesSettings = new HashMap<>(0);

	/** */
	public boolean canResize;

	/** */
	public boolean cannotFullscreen;

	/** */
	public boolean startedFullscreen;

	/** */
	public boolean cannotF11Fullscreen;

	/** */
	public boolean preventCloseOnEscape;

	/** */
	public boolean cannotAltEnterFullscreen;

	// region Initial dimensions.
	public void startInFullscreen() {
		this.fullScreen = true;
	}

	public void setHeight(final int p_height) {
		this.height = p_height;
	}

	public void setWidth(final int p_width) {
		this.width = p_width;
	}
	// endregion

}
