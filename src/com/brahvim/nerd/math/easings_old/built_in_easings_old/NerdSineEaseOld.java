package com.brahvim.nerd.math.easings_old.built_in_easings_old;

import com.brahvim.nerd.math.easings.NerdEasingFunction;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;

public class NerdSineEaseOld extends NerdEasingFunction {

	/**
	 * @apiNote {@code 0.001f} by default.
	 */
	public float parameterCoef = 0.001f;

	// region Constructors.
	public NerdSineEaseOld(final NerdSketch<?> p_parentSketch) {
		super(p_parentSketch);
	}

	public NerdSineEaseOld(final NerdSketch<?> p_parentSketch, final float p_freqMult) {
		super(p_parentSketch);
		this.parameterCoef = p_freqMult;
	}

	/**
	 * @apiNote {@code p_angleOffset} is assumed to be in radians.
	 */
	public NerdSineEaseOld(final NerdSketch<?> p_parentSketch, final float p_freqMult, final float p_angleOffset) {
		super(p_parentSketch);
		this.parameterCoef = p_freqMult;
		super.parameterOffset = p_angleOffset;
		// super.parameterOffset = Math.abs((float) Math.toRadians(p_angleOffset));
	}
	// endregion

	@Override
	public NerdSineEaseOld start() {
		super.start();
		return this;
	}

	// region End and extend!
	/**
	 * By calling this method, you can modify the time it takes this
	 * {@link NerdSineEaseOld} instance to deactivate automatically - by directly
	 * incrementing the amount of time it takes! <i>But do note</i>, that by using
	 * this method, any calculations made by the
	 * {@link NerdSineEaseOld#endWhenAngleIncrementsBy(float)} and
	 * {@link NerdSineEaseOld#endWhenAngleIncrementsToWithin(float, float)} methods are
	 * ignored, and the wave will no longer end at the angle you specified there.
	 *
	 * @param p_millis is the amount of time the angle parameter will be incremented
	 *                 by, in terms of radians. <i>Yes, this internally tracked
	 *                 'angle-parameter' is in terms of time. In milliseconds.</i>
	 */
	@Override
	public NerdSineEaseOld endAfterMillis(final int p_millis) {
		super.endTime = super.aliveTime + p_millis;
		return this;
	}

	/**
	 * By calling this method, you can make this {@link NerdSineEaseOld} instance
	 * deactivate automatically, when the angle parameter <i>(passed to the
	 * sine-based function)</i>, would've been incremented by what you've passed in.
	 * <p>
	 * Yes, the <i>angle parameter</i> in context is actually just a time value,
	 * tracked internally.
	 *
	 * @param p_angle is the angle to increment the parameter by, in radians.
	 */
	public NerdSineEaseOld endWhenAngleIncrementsBy(final float p_angle) {
		// Don't ask me why it took me `10` months to get this to work, even with the
		// right formula in my head, since day `1`!
		// ...totally didn't forget to convert to radians or something!

		// p_angle = PApplet.radians(PApplet.abs(p_angle));
		super.endTime = super.aliveTime + (p_angle - super.parameterOffset) / this.parameterCoef;

		// "Debug Laag":
		// System.out.printf("Alive-time: `%f`, end-time: `%f`, angle: `%f`.%n",
		// super.aliveTime, super.endTime, PApplet.degrees(this.freq % PConstants.TAU));

		return this;
	}

	/**
	 * By calling this method, you can make this {@link NerdSineEaseOld} instance
	 * deactivate automatically, when the angle parameter <i>(passed to the
	 * sine-based function)</i>, would've been incremented by what you've passed in,
	 * <i><b>within</i></b> the amount of time you provided.
	 * <p>
	 * Yes, the <i>angle parameter</i> in context is actually just a time value,
	 * tracked internally.
	 *
	 * @param p_angle                  is the angle, <i>in radians,</i> that the
	 *                                 wave will reach, and,
	 * @param p_timeConstraintDistance is the amount of time this
	 *                                 {@link NerdSineEaseOld} will stop after, at the
	 *                                 very angle you've given!
	 */
	// Method to put the wave at a given angle within given time.
	public NerdSineEaseOld endWhenAngleIncrementsToWithin(final float p_angle, final float p_timeConstraintDistance) {
		// p_angle = PApplet.radians(PApplet.abs(p_angle));

		final float offsetDiff = p_angle - super.parameterOffset;
		this.parameterCoef = offsetDiff / p_timeConstraintDistance;
		super.endTime = super.aliveTime + offsetDiff / this.parameterCoef;
		return this;

		// if (super.endTime < super.aliveTime + p_before) {
		// super.endTime -= p_before;
		// return this;
		// } else // If they're equal, this still takes place!
		// return this.end();
	}

	/**
	 * By calling this method, you can modify the time it takes this
	 * {@link NerdSineEaseOld} instance to deactivate automatically - by incrementing
	 * the angle parameter <i>(passed to the sine-based function)</i>! Passing
	 * negative numbers decreases it instead.
	 *
	 * @param p_angle is the angle to increment the parameter by, in radians.
	 */
	public NerdSineEaseOld extendEndByAngle(final float p_angle) {
		final float endTimeNew = this.endTime; // Benefits of using `this`!
		this.endWhenAngleIncrementsBy(p_angle);
		this.endTime += endTimeNew;
		return this;
	}
	// endregion

	@Override
	protected float apply() {
		return PApplet.sin(super.aliveTime * this.parameterCoef + super.parameterOffset);
	}

}
