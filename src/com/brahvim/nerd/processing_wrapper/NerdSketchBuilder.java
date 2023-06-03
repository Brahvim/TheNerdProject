package com.brahvim.nerd.processing_wrapper;

public final class NerdSketchBuilder extends NerdCustomSketchBuilder {

	@Override
	protected NerdSketch buildImpl(final String[] p_javaMainArgs) {
		return new NerdSketch(super.SKETCH_KEY);
	}

}
