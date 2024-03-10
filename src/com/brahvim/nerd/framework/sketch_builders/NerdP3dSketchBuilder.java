package com.brahvim.nerd.framework.sketch_builders;

import com.brahvim.nerd.framework.sketches.NerdP3dSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilder;

import processing.opengl.PGraphics3D;

public class NerdP3dSketchBuilder extends NerdSketchBuilder<PGraphics3D> {

    public NerdP3dSketchBuilder() {
        super(PGraphics3D.class);
        super.sketchConstructor = NerdP3dSketch::new;
    }

    public NerdP3dSketchBuilder(
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphics3D> p_modulesSet) {
        super(PGraphics3D.class, p_modulesSet);
    }

    public NerdP3dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphics3D> p_sketchConstructor) {
        super(PGraphics3D.class, p_sketchConstructor);
    }

    public NerdP3dSketchBuilder(
            final NerdSketchBuilder.NerdSketchConstructorFunction<PGraphics3D> p_sketchConstructor,
            final NerdSketchBuilder.NerdSketchModulesSetConsumer<PGraphics3D> p_modulesSet) {
        super(PGraphics3D.class, p_sketchConstructor, p_modulesSet);
    }

}
