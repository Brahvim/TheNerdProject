package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import processing.core.PConstants;

public class NerdSketchBuilderSettings {

	public int width = 400, height = 400, antiAliasing = 2;
	public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
	public Consumer<LinkedHashSet<Function<NerdSketch, NerdModule>>> nerdModulesInstantiator;
	public HashMap<Class<? extends NerdModule>, NerdModuleSettings<?>> nerdModulesSettings = new HashMap<>(0);
	public boolean canResize, preventCloseOnEscape, startedFullscreen, cannotFullscreen, cannotAltEnterFullscreen,
			cannotF11Fullscreen;

}
