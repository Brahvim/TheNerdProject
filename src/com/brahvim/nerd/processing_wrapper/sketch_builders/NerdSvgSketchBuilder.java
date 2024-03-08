package com.brahvim.nerd.processing_wrapper.sketch_builders;

import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.sketches.NerdSvgSketch;

import processing.svg.PGraphicsSVG;

public class NerdSvgSketchBuilder extends NerdSketchBuilder<PGraphicsSVG> {

    public NerdSvgSketchBuilder() {
        super(PGraphicsSVG.class);
        super.sketchConstructor = NerdSvgSketch::new;
    }

    public NerdSvgSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsSVG> p_modulesSet) {
        super(PGraphicsSVG.class, p_modulesSet);
    }

    public NerdSvgSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsSVG> p_sketchConstructor) {
        super(PGraphicsSVG.class, p_sketchConstructor);
    }

    public NerdSvgSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphicsSVG> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphicsSVG> p_modulesSet) {
        super(PGraphicsSVG.class, p_sketchConstructor, p_modulesSet);
    }

}
