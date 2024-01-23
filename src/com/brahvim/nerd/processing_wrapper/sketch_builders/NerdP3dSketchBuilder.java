package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;

import processing.opengl.PGraphics3D;

public class NerdP3dSketchBuilder extends NerdSketchBuilder<PGraphics3D> {

    public NerdP3dSketchBuilder() {
        super();
    }

    public NerdP3dSketchBuilder(
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics3D>, NerdModule>>> p_set) {
        this();
        this.modulesConsumer = p_set;
    }

    public NerdP3dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics3D>, NerdSketch<PGraphics3D>> p_object) {
        super(p_object);
    }

    public NerdP3dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics3D>, NerdSketch<PGraphics3D>> p_object,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics3D>, NerdModule>>> p_set) {
        super(p_object, p_set);
    }

}
