package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.opengl.PGraphics2D;

public class NerdP2dSketchBuilder extends NerdSketchBuilder<PGraphics2D> {

    public NerdP2dSketchBuilder() {
        super();
    }

    public NerdP2dSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics2D>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdP2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics2D>, NerdSketch<PGraphics2D>> p_object) {
        super(p_object);
    }

    public NerdP2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics2D>, NerdSketch<PGraphics2D>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics2D>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
