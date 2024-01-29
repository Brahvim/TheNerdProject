package com.brahvim.nerd.processing_wrapper.sketch_builders;

import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.sketches.NerdFx2dSketch;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dSketchBuilder extends NerdSketchBuilder<PGraphicsFX2D> {

    public NerdFx2dSketchBuilder() {
        super(PGraphicsFX2D.class);
        super.sketchConstructor = NerdFx2dSketch::new;
    }

    public NerdFx2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsFX2D> p_modulesSet) {
        super(PGraphicsFX2D.class, p_modulesSet);
    }

    public NerdFx2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsFX2D> p_sketchConstructor) {
        super(PGraphicsFX2D.class, p_sketchConstructor);
    }

    public NerdFx2dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsFX2D> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsFX2D> p_modulesSet) {
        super(PGraphicsFX2D.class, p_sketchConstructor, p_modulesSet);
    }

}
