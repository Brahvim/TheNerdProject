package com.brahvim.nerd.math;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;

public class SineFunction extends EasingFunction {

	// region Fields.
	/**
	 * @apiNote {@code 0.001f} by default.
	 */
	public float freqMult = 0.001f;

	/**
	 * Makes {@code SineFunction::get()} output its absolute value. This may make
	 * the
	 * wave behave as if it has doubled in frequency. Toggling this {@code boolean}
	 * while the wave is active may show unwantedly large, sudden changes in the
	 * value returned by {@code SineFunction::get()}.
	 *
	 * @apiNote {@code false} by default.
	 */
	public boolean absoluteValue = false;
	// endregion

	// region Constructors.
	public SineFunction(Sketch p_parentSketch) {
		super(p_parentSketch);
	}

	public SineFunction(Sketch p_parentSketch, float p_freqMult) {
		super(p_parentSketch);
		this.freqMult = p_freqMult;
	}

	/**
	 * @apiNote {@code p_angleOffset} are treated as radians.
	 */
	public SineFunction(Sketch p_parentSketch, float p_freqMult, float p_angleOffset) {
		super(p_parentSketch);
		this.freqMult = p_freqMult;
		super.parameterOffset = p_angleOffset;
		// super.parameterOffset = Math.abs((float) Math.toRadians(p_angleOffset));
	}
	// endregion

	// region End and extend!
	@Override
	public SineFunction endIn(int p_millis) {
		super.endTime = super.aliveTime + p_millis;
		return this;
	}

	public SineFunction endWhenAngleIncrementsBy(float p_angle) {
		// Don't ask me why it took me `10` months to get this to work, even with the
		// right formula in my head, since day `1`!
		// ...totally didn't forget to convert to radians or something!

		p_angle = PApplet.radians(PApplet.abs(p_angle));
		super.endTime = super.aliveTime + (p_angle - super.parameterOffset) / this.freqMult;

		// "Debug Laag":
		// System.out.printf("Alive-time: `%f`, end-time: `%f`, angle: `%f`.\n",
		// super.aliveTime, super.endTime, PApplet.degrees(this.freq % PConstants.TAU));

		return this;
	}

	/**
	 * Method to put the wave at a given angle within given time.
	 */
	public SineFunction endWhenAngleIncrementsToWithin(float p_angle, float p_before) {
		p_angle = PApplet.radians(PApplet.abs(p_angle));
		this.freqMult = (p_angle - super.parameterOffset) / p_before;
		return this;

		// if (super.endTime < super.aliveTime + p_before) {
		// super.endTime -= p_before;
		// return this;
		// } else // If they're equal, this still takes place!
		// return this.end();
	}

	public SineFunction extendEndByAngle(float p_angle) {
		float endTime = this.endTime; // Benefits of using `this.`!~
		this.endWhenAngleIncrementsBy(p_angle);
		this.endTime += endTime;
		return this;
	}
	// endregion

	@Override
	protected float apply() {
		return PApplet.sin(super.aliveTime * this.freqMult + super.parameterOffset);
	}

}
