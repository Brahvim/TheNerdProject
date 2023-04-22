package com.brahvim.nerd_demos.effect_layers;

import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd_demos.scenes.DemoScene3;

public class CinematicBars extends NerdLayer {

    public float rectHeight = 40;
    private DemoScene3 SCENE; // Access to `public` fields from the scene instance.

    @Override
    protected void setup() {
        SCENE = DemoScene3.class.cast(super.SCENE);
    }

    @Override
    protected void draw() {
        System.out.println("CinematicBars.draw()");
        SKETCH.fill(0);
        SKETCH.begin2d();

        SKETCH.rect(0, this.rectHeight, SKETCH.width, this.rectHeight);
        final float lowerRectTop = SKETCH.height - this.rectHeight;
        // ^^^ Needs to recalculate WHENEVER size changes, not just window resizes!
        SKETCH.rect(0, lowerRectTop, SKETCH.width, lowerRectTop);

        SKETCH.end2d();
    }

}
