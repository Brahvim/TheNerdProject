package com.brahvim.nerd.processing_wrapper.sketch_builders;

import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.sketches.NerdPdfSketch;

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
