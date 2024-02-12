package com.brahvim.nerd.processing_wrapper;

import processing.core.PConstants;

public enum NerdSketchRenderer {

    // Enum entries:
    GENERIC(""),
    P3D(PConstants.P3D),
    P2D(PConstants.P2D),
    PDF(PConstants.PDF),
    SVG(PConstants.SVG),
    FX2D(PConstants.FX2D),
    JAVA2D(PConstants.JAVA2D);

    // region Class stuff.
    private final String processingConstant;

    private NerdSketchRenderer(final String p_processingConstant) {
        this.processingConstant = p_processingConstant;
    }

    public String getPConstant() {
        return this.processingConstant;
    }
    // endregion

}
