package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdFx2dSketch;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dSketchBuilder extends NerdSketchBuilder<PGraphicsFX2D> {

    public NerdFx2dSketchBuilder() {
        super(PGraphicsFX2D.class);
        super.sketchConstructor = NerdFx2dSketch::new;
    }

    public NerdFx2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsFX2D>, NerdSketch<PGraphicsFX2D>> p_sketchConstructor) {
        super(PGraphicsFX2D.class, p_sketchConstructor);
    }

    public NerdFx2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphicsFX2D>, NerdSketch<PGraphicsFX2D>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphicsFX2D>, NerdModule<PGraphicsFX2D>>>> p_modulesSet) {
        super(PGraphicsFX2D.class, p_sketchConstructor, p_modulesSet);
    }

}
