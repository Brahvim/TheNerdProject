package com.brahvim.nerd_test.layers;

import com.brahvim.nerd.math.Easings;
import com.brahvim.nerd.scene_api.Layer;
import com.brahvim.nerd.scene_api.Scene.LayerKey;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class RevolvingParticlesLayer extends Layer {
    private PGraphics particleGraphics;
    private final int PARTICLE_SIZE = 120,
            PARTICLE_CONC = 20,
            PARTICLE_SIZE_HALF = this.PARTICLE_SIZE / 2;

    private final int BPM = 100,
            BPM_INT = (int) (this.BPM / 60_000.0f);

    private boolean tick;
    private int tickCount;

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
            g.fill(235, 174, 52, 255 * Easings.exponential(
                    (float) i / (float) this.PARTICLE_CONC));

            // Linearly interpolated fill:
            // g.fill(235, 174, 52, PApplet.map(i, 0, this.PARTICLE_CONC, 0, 255));

            // Squares when using `Easings.exponential(PConstants.PI *
            // ((float) i / (float) this.PARTICLE_CONC));` for the size!!!
            g.circle(this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF,
                    PApplet.map(i, 0, this.PARTICLE_CONC, this.PARTICLE_SIZE, 0));
        }

        g.endDraw();
        this.particleGraphics = g;
        // endregion

        final RevolvingParticlesLayer LAYER = this;
        new Thread(() -> {
            SKETCH.delay(BPM_INT);

            LAYER.tick = true;
            LAYER.tickCount++;
        }).start();

    }

    @Override
    protected void draw() {
        if (SKETCH.frameCount % 60 == 0)
            System.out.println(SKETCH.frameRate);

        // SKETCH.translate(0, 0,
        // -(SKETCH.currentCamera.pos.z + SKETCH.currentCamera.center.z));
        SKETCH.noStroke();

        SKETCH.translate(SKETCH.cx, SKETCH.cy);
        for (int i = 0; i < 20; i++) {
            float angle = PConstants.TAU / i;
            SKETCH.rotate(this.tickCount);

            SKETCH.image(this.particleGraphics,
                    PApplet.cos(PConstants.TAU * SKETCH.millis() * 0.0000016f * this.BPM) * i * 12,
                    PApplet.tan(PConstants.TAU * SKETCH.millis() * 0.0000016f * this.BPM) * i * 12,
                    0.5f);

            // It doesn't even move ._.
            /*
             * SKETCH.image(this.particleGraphics,
             * ((i & 1) == 0 ? 1 : -1) * SKETCH.cx
             * + 200 * PApplet.cos(SKETCH.millis() * 0.001f), // angle),
             * ((i & 1) == 0 ? 1 : -1) * SKETCH.cy
             * + 200 * PApplet.sin(SKETCH.millis() * 0.001f), // angle),
             * 0.5f);
             */

            // Parabola!
            /*
             * SKETCH.image(this.particleGraphics,
             * ((i & 1) == 0 ? 1 : -1) * SKETCH.cx
             * + 200 * angle
             * * PApplet.cos(
             * PApplet.sin(SKETCH.millis() * 0.001f // * SKETCH.millis()
             * * SKETCH.millis() * 0.001f * angle)),
             * ((i & 1) == 0 ? 1 : -1) * SKETCH.cy
             * + 200 * angle
             * * PApplet.sin(
             * PApplet.cos(SKETCH.millis() * 0.001f // * SKETCH.millis()
             * * SKETCH.millis() * 0.001f * angle)),
             * 0.5f);
             */
        }

    }

}
