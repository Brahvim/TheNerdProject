package com.brahvim.nerd.framework.sketch_builders;

import com.brahvim.nerd.framework.sketches.NerdPdfSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;

import processing.pdf.PGraphicsPDF;

public class NerdPdfSketchBuilder extends NerdSketchBuilder<PGraphicsPDF> {

	public NerdPdfSketchBuilder() {
		super(PGraphicsPDF.class);
		super.sketchConstructor = NerdPdfSketch::new;
	}

	public NerdPdfSketchBuilder(
			final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsPDF> p_modulesSet) {
		super(PGraphicsPDF.class, p_modulesSet);
	}

	public NerdPdfSketchBuilder(
			final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsPDF> p_sketchConstructor) {
		super(PGraphicsPDF.class, p_sketchConstructor);
	}

	public NerdPdfSketchBuilder(
			final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsPDF> p_sketchConstructor,
			final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsPDF> p_modulesSet) {
		super(PGraphicsPDF.class, p_sketchConstructor, p_modulesSet);
	}

}
