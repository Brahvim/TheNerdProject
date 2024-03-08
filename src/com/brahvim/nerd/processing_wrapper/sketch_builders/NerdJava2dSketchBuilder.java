package com.brahvim.nerd.processing_wrapper.sketch_builders;

import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.sketches.NerdJava2dSketch;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dSketchBuilder extends NerdSketchBuilder<PGraphicsJava2D> {

    public NerdJava2dSketchBuilder() {
        super(PGraphicsJava2D.class);
        super.sketchConstructor = NerdJava2dSketch::new;
    }

    public NerdJava2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsJava2D> p_sketchConstructor) {
        super(PGraphicsJava2D.class, p_sketchConstructor);
    }

    public NerdJava2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsJava2D> p_modulesSet) {
        super(PGraphicsJava2D.class, p_modulesSet);
    }

    public NerdJava2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsJava2D> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsJava2D> p_modulesSet) {
        super(PGraphicsJava2D.class, p_sketchConstructor, p_modulesSet);
    }

}
