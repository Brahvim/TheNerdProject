package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdJava2dSketch;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dSketchBuilder extends NerdSketchBuilder<PGraphicsJava2D> {

    public NerdJava2dSketchBuilder() {
        super(PGraphicsJava2D.class);
        super.sketchConstructor = NerdJava2dSketch::new;
    }

    public NerdJava2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_sketchConstructor) {
        super(PGraphicsJava2D.class, p_sketchConstructor);
    }

    public NerdJava2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule<PGraphicsJava2D>>>> p_modulesSet) {
        super(PGraphicsJava2D.class, p_sketchConstructor, p_modulesSet);
    }

}
