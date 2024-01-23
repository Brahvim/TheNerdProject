package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dSketchBuilder extends NerdSketchBuilder<PGraphicsFX2D> {

    public NerdFx2dSketchBuilder() {
        super();
    }

    public NerdFx2dSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsFX2D>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdFx2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsFX2D>, NerdSketch<PGraphicsFX2D>> p_object) {
        super(p_object);
    }

    public NerdFx2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsFX2D>, NerdSketch<PGraphicsFX2D>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsFX2D>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
