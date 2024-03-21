package com.brahvim.nerd.framework.colors;

import processing.core.PConstants;

public enum NerdProcessingColorSpace implements NerdColorSpace {

    RGB(),
    HSB();

    // region Class stuff.
    public int toPConstant() {
        return switch (this) {
            case RGB -> PConstants.RGB;
            case HSB -> PConstants.HSB;
        };
    }
    // endregion

}
