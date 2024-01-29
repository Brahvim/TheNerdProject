package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdSvgSketch;

import processing.svg.PGraphicsSVG;

public class NerdSvgSketchBuilder extends NerdSketchBuilder<PGraphicsSVG> {

    public NerdSvgSketchBuilder() {
        super(PGraphicsSVG.class);
        super.sketchConstructor = NerdSvgSketch::new;
    }

    public NerdSvgSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsSVG>, NerdSketch<PGraphicsSVG>> p_sketchConstructor) {
        super(PGraphicsSVG.class, p_sketchConstructor);
    }

    public NerdSvgSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsSVG>, NerdSketch<PGraphicsSVG>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsSVG>, NerdModule<PGraphicsSVG>>>> p_modulesSet) {
        super(PGraphicsSVG.class, p_sketchConstructor, p_modulesSet);
    }

}
