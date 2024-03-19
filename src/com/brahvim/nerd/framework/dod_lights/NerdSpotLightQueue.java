package com.brahvim.nerd.framework.dod_lights;

import processing.core.PVector;

public class NerdSpotLightQueue implements NerdLightSlotEntry {

    public static class NerdSpotLight {

        public final PVector
        /*   */ COLOR = new PVector(),
                POSITION = new PVector(),
                DIRECTION = new PVector();

        public float angle, concentration = 4000;

        private final PVector PDIR = new PVector();

        public PVector getPreviousFrameDirection() {
            return this.PDIR;
        }

    }

}
