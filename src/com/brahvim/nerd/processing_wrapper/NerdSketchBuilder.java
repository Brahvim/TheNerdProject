package com.brahvim.nerd.processing_wrapper;

public class NerdSketchBuilder {

	private final NerdSketchSettings SETTINGS;

	public NerdSketchBuilder() {
		this.SETTINGS = new NerdSketchSettings();
	}

	public NerdSketch build() {
		return new NerdSketch(this.SETTINGS);
	}

}
