package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.special_sketches.NerdJava2dUndecoratedWindowSketch;

import processing.awt.PGraphicsJava2D;

/** You can add more modules, that's it... */
public class NerdUndecoratedJava2dSketchBuilder extends NerdJava2dSketchBuilder {

    public NerdUndecoratedJava2dSketchBuilder() {
        super();
        super.sketchFxn = NerdJava2dUndecoratedWindowSketch::new;
    }

    public NerdUndecoratedJava2dSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
        super.sketchFxn = NerdJava2dUndecoratedWindowSketch::new;
    }

}
