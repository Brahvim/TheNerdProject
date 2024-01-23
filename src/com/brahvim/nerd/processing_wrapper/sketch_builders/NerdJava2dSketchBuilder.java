package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dSketchBuilder extends NerdSketchBuilder<PGraphicsJava2D> {

    public NerdJava2dSketchBuilder() {
        super();
    }

    public NerdJava2dSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdJava2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_object) {
        super(p_object);
    }

    public NerdJava2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
