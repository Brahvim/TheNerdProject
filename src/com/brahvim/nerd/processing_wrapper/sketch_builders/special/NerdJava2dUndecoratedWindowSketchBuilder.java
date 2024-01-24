package com.brahvim.nerd.processing_wrapper.sketch_builders.special;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdJava2dSketchBuilder;
import com.brahvim.nerd.processing_wrapper.sketches.special.NerdJava2dUndecoratedWindowSketch;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dUndecoratedWindowSketchBuilder extends NerdJava2dSketchBuilder {

    public NerdJava2dUndecoratedWindowSketchBuilder() {
        super();
        super.sketchConstructor = NerdJava2dUndecoratedWindowSketch::new;
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule>>> p_modulesSet) {
        this(); // Call this class's constructor because... It changes things, y'know?!
        super.modulesConsumer = p_modulesSet;
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_sketchConstructor) {
        super(p_sketchConstructor);
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsJava2D>, NerdSketch<PGraphicsJava2D>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsJava2D>, NerdModule>>> p_modulesSet) {
        super(p_sketchConstructor, p_modulesSet);
    }

}
