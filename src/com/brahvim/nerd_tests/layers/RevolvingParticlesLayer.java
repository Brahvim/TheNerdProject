package com.brahvim.nerd_tests.layers;

import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd_tests.App;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class RevolvingParticlesLayer extends NerdLayer {
    private PGraphics particleGraphics;
    private final int PARTICLE_SIZE = 30, PARTICLE_CONC = 200,
            PARTICLE_SIZE_HALF = this.PARTICLE_SIZE / 2;

    @Override
    protected void setup() {
        // region Particle image.
        final PGraphics g = SKETCH.createGraphics(this.PARTICLE_SIZE);

        SKETCH.colorMode(PConstants.ARGB);
        final int particleColor = SKETCH.color(235, 174, 52),
                noColor = SKETCH.color(0, 0);

        g.beginDraw();
        g.noStroke();

        for (int x = 0; x < g.width; x++) {
            for (int y = 0; y < g.height; y++) {
                final float dist = PApplet.dist(x, y,
                        this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF);

                if (dist < this.PARTICLE_SIZE)
                    g.set(x, y, PApplet.lerpColor(
                            particleColor, noColor,
                            dist / this.PARTICLE_SIZE,
                            PConstants.ARGB));
            }
        }

        SKETCH.colorMode(PConstants.RGB);

        // for (int i = 0; i < this.PARTICLE_CONC; i++) {
        // // g.fill(235, 174, 52, 255 * Easings.exponential(
        // // (float) i / (float) this.PARTICLE_CONC));

        // // Linearly interpolated fill:
        // g.fill(235, 174, 52, PApplet.map(i, 0, this.PARTICLE_CONC, 0, 1));

        // // Squares when using `Easings.exponential(PConstants.PI *
        // // ((float) i / (float) this.PARTICLE_CONC));` for the size!!!
        // g.circle(this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF,
        // PApplet.map(i, 0, this.PARTICLE_CONC, this.PARTICLE_SIZE, 0));
        // }

        g.endDraw();
        this.particleGraphics = g;
        // endregion

    }

    @Override
    protected void draw() {
        // if (SKETCH.frameCount % 150 == 0)
        // System.out.println(SKETCH.frameRate);

        // SKETCH.translate(0, 0,
        // -(SKETCH.currentCamera.pos.z + SKETCH.currentCamera.center.z));
        SKETCH.noStroke();

        for (int i = 0; i < 20; i++) {
            // float angle = PConstants.TAU / i;
            SKETCH.rotate(App.getTickCount());

            SKETCH.image(this.particleGraphics,
                    PApplet.cos(3 * PConstants.TAU * SKETCH.millis() * 0.0000016f * App.BPM) * i * 12,
                    PApplet.tan(3 * PConstants.TAU * SKETCH.millis() * 0.0000016f * App.BPM) * i * 12,
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
