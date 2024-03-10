package com.brahvim.nerd.framework.sketch_builders;

import com.brahvim.nerd.framework.sketches.NerdP2dSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;

import processing.awt.PGraphicsJava2D;
import processing.opengl.PGraphics2D;

public class NerdP2dSketchBuilder extends NerdSketchBuilder<PGraphics2D> {

    public NerdP2dSketchBuilder() {
        super(PGraphicsJava2D.class);
        super.sketchConstructor = NerdP2dSketch::new;
    }

    public NerdP2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphics2D> p_modulesSet) {
        super(PGraphicsJava2D.class, p_modulesSet);
    }

    public NerdP2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphics2D> p_sketchConstructor) {
        super(PGraphicsJava2D.class, p_sketchConstructor);
    }

    public NerdP2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphics2D> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphics2D> p_modulesSet) {
        super(PGraphicsJava2D.class, p_sketchConstructor, p_modulesSet);
    }

}
