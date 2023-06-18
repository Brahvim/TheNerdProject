package com.brahvim.nerd_demos.scenes.scene3;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.math.easings.built_in_easings.NerdSineEase;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd_demos.App;
import com.brahvim.nerd.openal.AlBuffer;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class AnimatedCube extends TestEulerBody {

	// region Fields.
	public static int DEFAULT_LIFETIME = 8_000;

	public float size = 40;
	public int startTime, lifetime;
	public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

	private boolean visible = true;
	private NerdSineEase plopWave;
	private AlSource popSrc;
	// endregion

	public AnimatedCube(final NerdScene p_scene) {
		super(p_scene.getSketch());

		this.startTime = SKETCH.millis();
		this.lifetime = this.startTime + AnimatedCube.DEFAULT_LIFETIME;

		super.pos.set(
				super.SKETCH.getCamera().getPos().x
						+ super.SKETCH.random(-super.SKETCH.WINDOW.cx, super.SKETCH.WINDOW.cx),
				super.SKETCH.getCamera().getPos().y
						+ super.SKETCH.random(-super.SKETCH.WINDOW.cy, super.SKETCH.WINDOW.cy),
				super.SKETCH.getCamera().getPos().z
						+ super.SKETCH.random(-600, 600));

		super.acc.set(
				super.SKETCH.random(-0.01f, 0.01f),
				super.SKETCH.random(-0.01f, 0.01f),
				super.SKETCH.random(-0.01f, 0.01f));

		super.rot.set(
				super.SKETCH.random(PConstants.TAU),
				super.SKETCH.random(PConstants.TAU),
				super.SKETCH.random(PConstants.TAU));

		super.rotAcc.set(
				super.SKETCH.random(-0.0001f, 0.0001f),
				super.SKETCH.random(-0.0001f, 0.0001f),
				super.SKETCH.random(-0.0001f, 0.0001f));

		this.plopWave = new NerdSineEase(super.SKETCH);
		this.plopWave.inactValue = 1;
	}

	// region Getters and setters for superclass stuff!:
	public float getFrict() {
		return super.frict;
	}

	public void setFrict(final float p_frict) {
		super.frict = p_frict;
	}

	public float getRotFrict() {
		return super.rotFrict;
	}

	public void setRotFrict(final float p_rotFrict) {
		super.rotFrict = p_rotFrict;
	}

	public PVector getPos() {
		return super.pos;
	}

	public PVector getVel() {
		return super.vel;
	}

	public PVector getAcc() {
		return super.acc;
	}

	public PVector getRot() {
		return super.rot;
	}

	public PVector getRotVel() {
		return super.rotVel;
	}

	public PVector getRotAcc() {
		return super.rotAcc;
	}
	// endregion

	public AnimatedCube plopIn(final AlBuffer<?> p_popAudioBuffer) {
		if (p_popAudioBuffer != null)
			this.popSrc = new AlSource(App.openAl, p_popAudioBuffer);

		// this.popSrc.setPosition(super.pos.array());
		// this.popSrc.setPosition(PVector.mult(super.pos, 0.001f).array());
		this.visible = true;
		this.popSrc.setPosition(
				PVector.mult(super.pos, 0.0001f)
						// super.pos
						.array());

		this.popSrc.playThenDispose();

		this.plopWave.parameterCoef = 0.015f;
		this.plopWave.endWhenAngleIncrementsBy(90).start();
		return this;
	}

	public AnimatedCube plopOut(final Runnable p_runnable) {
		this.plopWave.parameterCoef = 0.00001f;
		this.plopWave.endWhenAngleIncrementsBy(90)
				.start(270, () -> {
					p_runnable.run();
					this.visible = false;
					this.popSrc.dispose();
				});
		return this;
	}

	public AlSource getAudioSource() {
		return this.popSrc;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void draw(final PShape p_shape) {
		if (!this.visible)
			return;

		if (!this.popSrc.isDisposed())
			this.popSrc.setPosition(
					// PVector.mult(super.pos, 0.0001f)
					PVector.div(super.pos, App.openAl.unitSize)
							// super.pos
							.array());

		super.integrate();
		super.SKETCH.push();
		super.SKETCH.translate(super.pos);
		super.SKETCH.rotate(super.rot);
		super.SKETCH.scale(this.size * this.plopWave.get());

		// Performance drop + doesn't work!:
		// for (int i = 0; i < p_shape.getVertexCount(); i++)
		// p_shape.setFill(i, SKETCH.color(255, PApplet.map(SKETCH.millis(),
		// this.startTime, this.lifetime, 0, 255)));

		super.SKETCH.shape(p_shape);
		super.SKETCH.pop();
	}

}
