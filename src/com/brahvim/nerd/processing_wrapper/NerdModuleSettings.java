package com.brahvim.nerd.processing_wrapper;

import processing.core.PGraphics;

public abstract class NerdModuleSettings<SketchPGraphicsT extends PGraphics, ModuleT extends NerdModule<SketchPGraphicsT>> { // NOSONAR!

    protected NerdModuleSettings() {
    }

    public abstract <RetModuleClassT extends ModuleT> Class<RetModuleClassT> getNerdModuleClass();

}
