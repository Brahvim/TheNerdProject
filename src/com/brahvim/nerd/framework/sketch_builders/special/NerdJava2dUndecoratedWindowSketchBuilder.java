package com.brahvim.nerd.framework.sketch_builders.special;

import com.brahvim.nerd.framework.sketch_builders.NerdJava2dSketchBuilder;
import com.brahvim.nerd.framework.sketches.special.NerdJava2dUndecoratedWindowSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dUndecoratedWindowSketchBuilder extends NerdJava2dSketchBuilder {

    public NerdJava2dUndecoratedWindowSketchBuilder() {
        super();
        super.sketchConstructor = NerdJava2dUndecoratedWindowSketch::new;
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsJava2D> p_modulesSet) {
        super(p_modulesSet);
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsJava2D> p_sketchConstructor) {
        super(p_sketchConstructor);
    }

    public NerdJava2dUndecoratedWindowSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsJava2D> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsJava2D> p_modulesSet) {
        super(p_sketchConstructor, p_modulesSet);
    }

}
