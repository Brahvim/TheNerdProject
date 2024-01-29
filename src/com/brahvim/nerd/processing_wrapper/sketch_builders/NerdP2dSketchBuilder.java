package com.brahvim.nerd.processing_wrapper.sketch_builders;

import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdSketchSettings;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP2dSketch;

import processing.awt.PGraphicsJava2D;
import processing.opengl.PGraphics2D;

public class NerdP2dSketchBuilder extends NerdSketchBuilder<PGraphics2D> {

    public NerdP2dSketchBuilder() {
        super(PGraphicsJava2D.class);
        super.sketchConstructor = NerdP2dSketch::new;
    }

    public NerdP2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics2D>, NerdSketch<PGraphics2D>> p_sketchConstructor) {
        super(PGraphicsJava2D.class, p_sketchConstructor);
    }

    public NerdP2dSketchBuilder(
            final Function<NerdSketchSettings<PGraphics2D>, NerdSketch<PGraphics2D>> p_sketchConstructor,
            final Consumer<LinkedHashSet<Function<NerdSketch<PGraphics2D>, NerdModule<PGraphics2D>>>> p_modulesSet) {
        super(PGraphicsJava2D.class, p_sketchConstructor, p_modulesSet);
    }

}
