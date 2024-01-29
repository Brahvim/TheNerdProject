package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdPdfSketch;

import processing.pdf.PGraphicsPDF;

public class NerdPdfSketchBuilder extends NerdSketchBuilder<PGraphicsPDF> {

    public NerdPdfSketchBuilder() {
        super(PGraphicsPDF.class);
        super.sketchConstructor = NerdPdfSketch::new;
    }

    public NerdPdfSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsPDF>, NerdSketch<PGraphicsPDF>> p_sketchConstructor) {
        super(PGraphicsPDF.class, p_sketchConstructor);
    }

    public NerdPdfSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsPDF>, NerdSketch<PGraphicsPDF>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsPDF>, NerdModule<PGraphicsPDF>>>> p_modulesSet) {
        super(PGraphicsPDF.class, p_sketchConstructor, p_modulesSet);
    }

}
