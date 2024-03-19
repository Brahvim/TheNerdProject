package com.brahvim.nerd.framework.dod_lights;

import java.util.ArrayList;

import processing.core.PVector;

public class NerdSpotLightQueue implements NerdLightSlotEntry {

    private final ArrayList<NerdSpotLight> QUEUE = new ArrayList<>(2);

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
