package com.brahvim.nerd.processing_wrapper;

public abstract class NerdSketchBuilder {

	private final NerdSketchSettings SKETCH_SETTINGS;

	protected NerdSketchBuilder() {
		this.SKETCH_SETTINGS = new NerdSketchSettings();
	}

	public abstract NerdSketch build();

	public abstract void configureNerdModules();

}
