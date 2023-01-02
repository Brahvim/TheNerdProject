package com.brahvim.nerd_test.layers;

import com.brahvim.nerd.scene_api.Layer;
import com.brahvim.nerd.scene_api.Scene.LayerKey;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class RevolvingParticlesLayer extends Layer {
    private PGraphics particleGraphics;
    private final int PARTICLE_SIZE = 60,
            PARTICLE_CONC = 20,
            PARTICLE_SIZE_HALF = this.PARTICLE_SIZE / 2;

    public RevolvingParticlesLayer(LayerKey p_initializer) {
        super(p_initializer);
    }

    @Override
    protected void setup() {
        // region Particle image.
        PGraphics g = SKETCH.createGraphics(this.PARTICLE_SIZE);
        g.beginDraw();
        g.noStroke();

        for (int i = 0; i < this.PARTICLE_CONC; i++) {
            // g.fill(235, 174, 52, PApplet.map(i, 0, this.PARTICLE_CONC, 0, 255));
            g.fill(235, 174, 52, 255 * PApplet.sin(PConstants.PI * ((float) i / (float) this.PARTICLE_CONC)));
            g.circle(this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF,
                    PApplet.map(i, 0, this.PARTICLE_CONC, this.PARTICLE_SIZE, 0));
        }

        g.endDraw();
        this.particleGraphics = g;
        // endregion

    }

    @Override
    protected void draw() {
        if (SKETCH.frameCount % 60 == 0)
            System.out.println(SKETCH.frameRate);

        // SKETCH.circle(super.SKETCH.mouseX, super.SKETCH.mouseY, 20);
        // SKETCH.translate(0, 0,
        // -(SKETCH.currentCamera.pos.z + SKETCH.currentCamera.center.z));
        SKETCH.noStroke();

        for (int i = 0; i < 20; i++) {
            float angle = PConstants.TAU / i;
            SKETCH.image(this.particleGraphics,
                    ((i & 1) == 0 ? 1 : -1) * SKETCH.cx
                            + 200 * angle
                                    * PApplet.cos(
                                            PApplet.sin(SKETCH.millis() * 0.001f) * SKETCH.millis() * 0.001f * angle),
                    ((i & 1) == 0 ? 1 : -1) * SKETCH.cy
                            + 200 * angle
                                    * PApplet.sin(
                                            PApplet.cos(SKETCH.millis() * 0.001f) * SKETCH.millis() * 0.001f * angle),
                    0.5f);
        }

    }

}
