package com.brahvim.nerd_demos.scenes.scene1;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

import processing.core.PApplet;
import processing.core.PGraphics;

public class RevolvingParticlesLayer extends NerdLayer {
	private PGraphics particleGraphics;

	private static final int
	/*	 */ PARTICLE_SIZE = 30,
			PARTICLE_CONC = 80,
			PARTICLE_SIZE_HALF = RevolvingParticlesLayer.PARTICLE_SIZE / 2;

	@Override
	protected void setup() {
		this.particleGraphics = createParticleGraphics();
	}

	private PGraphics createParticleGraphics() {
		final PGraphics g = SKETCH.createGraphics(RevolvingParticlesLayer.PARTICLE_SIZE);
		g.beginDraw();
		g.noStroke();
		g.fill(235, 174, 52, 255.0f / RevolvingParticlesLayer.PARTICLE_CONC * 1.15f);

		for (int i = 0; i != RevolvingParticlesLayer.PARTICLE_CONC; i++)
			g.circle(RevolvingParticlesLayer.PARTICLE_SIZE_HALF, RevolvingParticlesLayer.PARTICLE_SIZE_HALF,
					PApplet.map(i, 0, RevolvingParticlesLayer.PARTICLE_CONC, RevolvingParticlesLayer.PARTICLE_SIZE, 0));

		/*
		 * for (int i = 0; i < RevolvingParticlesLayer.PARTICLE_CONC; i++) {
		 * g.fill(235, 174, 52, 255 * Easings.exponential(
		 * (float) i / (float) RevolvingParticlesLayer.PARTICLE_CONC));
		 * // Linearly interpolated fill:
		 * // g.fill(235, 174, 52, PApplet.map(i, 0,
		 * RevolvingParticlesLayer.PARTICLE_CONC, 0, 1));
		 * // Squares when using `Easings.exponential(PConstants.PI *
		 * // ((float) i / (float) RevolvingParticlesLayer.PARTICLE_CONC));` for the
		 * size!!!
		 * g.circle(RevolvingParticlesLayer.PARTICLE_SIZE_HALF,
		 * RevolvingParticlesLayer.PARTICLE_SIZE_HALF,
		 * PApplet.map(i, 0, RevolvingParticlesLayer.PARTICLE_CONC,
		 * RevolvingParticlesLayer.PARTICLE_SIZE,
		 * 0));
		 * }
		 */

		g.endDraw();
		return g;
	}

	@Override
	protected void draw() {
		GRAPHICS.noStroke();

		for (int i = 0; i < 20; i++) {
			// float angle = PConstants.TAU / i;
			// GRAPHICS.rotate(App.getTickCount());

			// GRAPHICS.image(this.particleGraphics,
			// PApplet.cos(3 * PConstants.TAU * SKETCH.millis() * 0.0000016f * App.) * i *
			// 12,
			// PApplet.tan(3 * PConstants.TAU * SKETCH.millis() * 0.0000016f * App.BPM) * i
			// * 12,
			// 0.5f);

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
