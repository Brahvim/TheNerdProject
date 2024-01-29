package com.brahvim.nerd.processing_wrapper;

import processing.core.PGraphics;

public abstract class NerdModuleSettings<SketchPGraphicsT extends PGraphics, ModuleT extends NerdModule<SketchPGraphicsT>> { // NOSONAR

    // @SuppressWarnings("unchecked")
    public <RetModuleClassT extends ModuleT> Class<RetModuleClassT> getModuleClass() {
        return null; // (Class<RetModuleClassT>) NerdModule.class;
    }

}
