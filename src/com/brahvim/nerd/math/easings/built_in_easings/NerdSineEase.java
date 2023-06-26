package com.brahvim.nerd.math.easings.built_in_easings;

import com.brahvim.nerd.math.easings.NerdEasingFunction;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;

public class NerdSineEase extends NerdEasingFunction {

	// region Fields.
	/**
	 * @apiNote {@code 0.001f} by default.
	 */
	public float parameterCoef = 0.001f;

	/**
	 * Makes {@code SineEase::get()} output its absolute value. This may make
	 * the
	 * wave behave as if it has doubled in frequency. Toggling this {@code boolean}
	 * while the wave is active may show unwantedly large, sudden changes in the
	 * value returned by {@code SineEase::get()}.
	 *
	 * @apiNote {@code false} by default.
	 */
	public boolean absoluteValue = false;
	// endregion

	// region Constructors.
	public NerdSineEase(final NerdSketch p_parentSketch) {
		super(p_parentSketch);
	}

	public NerdSineEase(final NerdSketch p_parentSketch, final float p_freqMult) {
		super(p_parentSketch);
		this.parameterCoef = p_freqMult;
	}

	/**
	 * @apiNote {@code p_angleOffset} are treated as radians.
	 */
	public NerdSineEase(final NerdSketch p_parentSketch, final float p_freqMult, final float p_angleOffset) {
		super(p_parentSketch);
		this.parameterCoef = p_freqMult;
		super.parameterOffset = p_angleOffset;
		// super.parameterOffset = Math.abs((float) Math.toRadians(p_angleOffset));
	}
	// endregion

	@Override
	public NerdSineEase start() {
		super.start();
		return this;
	}

	// region End and extend!
	@Override
	public NerdSineEase endAfterMillis(final int p_millis) {
		super.endTime = super.aliveTime + p_millis;
		return this;
	}

	public NerdSineEase endWhenAngleIncrementsBy(float p_angle) {
		// Don't ask me why it took me `10` months to get this to work, even with the
		// right formula in my head, since day `1`!
		// ...totally didn't forget to convert to radians or something!

		p_angle = PApplet.radians(PApplet.abs(p_angle));
		super.endTime = super.aliveTime + (p_angle - super.parameterOffset) / this.parameterCoef;

		// "Debug Laag":
		// System.out.printf("Alive-time: `%f`, end-time: `%f`, angle: `%f`.%n",
		// super.aliveTime, super.endTime, PApplet.degrees(this.freq % PConstants.TAU));

		return this;
	}

	/**
	 * Method to put the wave at a given angle within given time.
	 */
	public NerdSineEase endWhenAngleIncrementsToWithin(float p_angle, final float p_before) {
		p_angle = PApplet.radians(PApplet.abs(p_angle));
		this.parameterCoef = (p_angle - super.parameterOffset) / p_before;
		return this;

		// if (super.endTime < super.aliveTime + p_before) {
		// super.endTime -= p_before;
		// return this;
		// } else // If they're equal, this still takes place!
		// return this.end();
	}

	public NerdSineEase extendEndByAngle(final float p_angle) {
		final float endTime = this.endTime; // Benefits of using `this.`!~
		this.endWhenAngleIncrementsBy(p_angle);
		this.endTime += endTime;
		return this;
	}
	// endregion

	@Override
	protected float apply() {
		return PApplet.sin(super.aliveTime * this.parameterCoef + super.parameterOffset);
	}

}
