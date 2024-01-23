package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.pdf.PGraphicsPDF;

public class NerdPdfSketchBuilder extends NerdSketchBuilder<PGraphicsPDF> {

    public NerdPdfSketchBuilder() {
        super();
    }

    public NerdPdfSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsPDF>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdPdfSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsPDF>, NerdSketch<PGraphicsPDF>> p_object) {
        super(p_object);
    }

    public NerdPdfSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsPDF>, NerdSketch<PGraphicsPDF>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsPDF>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
