package com.brahvim.nerd.framework.sketches;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.pdf.PGraphicsPDF;

public class NerdPdfSketch extends NerdSketch<PGraphicsPDF> {

	public NerdPdfSketch(final NerdSketchSettings<PGraphicsPDF> p_settings) {
		super(p_settings);
	}

}
