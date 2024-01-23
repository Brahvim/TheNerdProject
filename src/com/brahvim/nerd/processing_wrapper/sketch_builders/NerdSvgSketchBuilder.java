package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.svg.PGraphicsSVG;

public class NerdSvgSketchBuilder extends NerdSketchBuilder<PGraphicsSVG> {

    public NerdSvgSketchBuilder() {
        super();
    }

    public NerdSvgSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsSVG>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdSvgSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsSVG>, NerdSketch<PGraphicsSVG>> p_object) {
        super(p_object);
    }

    public NerdSvgSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsSVG>, NerdSketch<PGraphicsSVG>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsSVG>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
