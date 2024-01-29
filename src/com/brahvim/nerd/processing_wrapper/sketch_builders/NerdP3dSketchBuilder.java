package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP3dSketch;

import processing.opengl.PGraphics3D;

public class NerdP3dSketchBuilder extends NerdSketchBuilder<PGraphics3D> {

    public NerdP3dSketchBuilder() {
        super(PGraphics3D.class);
        super.sketchConstructor = NerdP3dSketch::new;
    }

    public NerdP3dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics3D>, NerdSketch<PGraphics3D>> p_sketchConstructor) {
        super(PGraphics3D.class, p_sketchConstructor);
    }

    public NerdP3dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics3D>, NerdSketch<PGraphics3D>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics3D>, NerdModule<PGraphics3D>>>> p_modulesSet) {
        super(PGraphics3D.class, p_sketchConstructor, p_modulesSet);
    }

}
