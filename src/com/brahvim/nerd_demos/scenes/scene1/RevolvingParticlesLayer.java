package com.brahvim.nerd_demos.scenes.scene1;

import com.brahvim.nerd.framework.scene_api.NerdLayer;
import com.brahvim.nerd_demos.App;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class RevolvingParticlesLayer extends NerdLayer {
	private PGraphics particleGraphics;
	private final int PARTICLE_SIZE = 30, PARTICLE_CONC = 80,
			PARTICLE_SIZE_HALF = this.PARTICLE_SIZE / 2;

	@Override
	protected void setup() {
		final PGraphics g = SKETCH.createGraphics(this.PARTICLE_SIZE);
		g.beginDraw();
		g.noStroke();
		g.fill(235, 174, 52, 255.0f / this.PARTICLE_CONC * 1.15f);

		for (int i = 0; i != this.PARTICLE_CONC; i++)
			g.circle(this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF,
					PApplet.map(i, 0, this.PARTICLE_CONC, this.PARTICLE_SIZE, 0));

		/*
		 * for (int i = 0; i < this.PARTICLE_CONC; i++) {
		 * g.fill(235, 174, 52, 255 * Easings.exponential(
		 * (float) i / (float) this.PARTICLE_CONC));
		 * // Linearly interpolated fill:
		 * // g.fill(235, 174, 52, PApplet.map(i, 0, this.PARTICLE_CONC, 0, 1));
		 * // Squares when using `Easings.exponential(PConstants.PI *
		 * // ((float) i / (float) this.PARTICLE_CONC));` for the size!!!
		 * g.circle(this.PARTICLE_SIZE_HALF, this.PARTICLE_SIZE_HALF,
		 * PApplet.map(i, 0, this.PARTICLE_CONC, this.PARTICLE_SIZE, 0));
		 * }
		 */

		g.endDraw();
		this.particleGraphics = g;
	}

	@Override
	protected void draw() {
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
			 * ((i & 1) == 0 ? 1 : -1) * WINDOW.cx
			 * + 200 * PApplet.cos(SKETCH.millis() * 0.001f), // angle),
			 * ((i & 1) == 0 ? 1 : -1) * WINDOW.cy
			 * + 200 * PApplet.sin(SKETCH.millis() * 0.001f), // angle),
			 * 0.5f);
			 */

			// Parabola!
			/*
			 * SKETCH.image(this.particleGraphics,
			 * ((i & 1) == 0 ? 1 : -1) * WINDOW.cx
			 * + 200 * angle
			 * * PApplet.cos(
			 * PApplet.sin(SKETCH.millis() * 0.001f // * SKETCH.millis()
			 * * SKETCH.millis() * 0.001f * angle)),
			 * ((i & 1) == 0 ? 1 : -1) * WINDOW.cy
			 * + 200 * angle
			 * * PApplet.sin(
			 * PApplet.cos(SKETCH.millis() * 0.001f // * SKETCH.millis()
			 * * SKETCH.millis() * 0.001f * angle)),
			 * 0.5f);
			 */
		}

	}

}
