package com.brahvim.nerd.math.easings;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

// It is better to extend this class than to have `java.util.Function`s in it.
// How else would we have stuff like `NerdSineEase::freqMult`, then?!

public abstract class NerdEasingFunction {

	// region Fields.
	/**
	 * Time at which the internal timer used as a parameter to the underlying
	 * function will be stopped!
	 */
	public float endTime = -1;

	/**
	 * Amount of time for which this function has been in calculation.
	 */
	public float aliveTime;

	/**
	 * The parameter to be passed to the underlying function to sample it.
	 */
	public float parameter;

	/**
	 * And offset value <i>added</i> to the parameter, after which it is passed to
	 * the underlying function.
	 */
	public float parameterOffset;

	/**
	 * Returned by {@linkplain NerdEasingFunction#get() NerdEasingFunction::get()}
	 * if {@linkplain NerdEasingFunction#useInactValue
	 * NerdEasingFunction::useInactValue} is {@code true}.
	 */
	public float inactValue = 0;

	/**
	 * Determines if a {@link NerdEasingFunction} object should be performing
	 * calculations or not.
	 *
	 * @apiNote {@code false} by default. Call
	 *          {@linkplain NerdEasingFunction#start() NerdEasingFunction::start()}
	 *          to make the function actively processing again.
	 */
	public boolean active = false;

	/**
	 * Makes {@linkplain NerdEasingFunction#get() NerdEasingFunction::get()} output
	 * {@linkplain NerdEasingFunction#inactValue NerdEasingFunction::inactValue}
	 * when the wave has ended - <i>id est</i>, when
	 * {@linkplain NerdEasingFunction#active NerdEasingFunction::active}
	 * has turned {@code false}.
	 *
	 * @apiNote {@code true} by default. {@linkplain NerdEasingFunction#inactValue
	 *          NerdEasingFunction::inactValue} is {@code 0} by default.
	 */
	public boolean useInactValue = false;

	/**
	 * Makes {@linkplain NerdEasingFunction#get() NerdEasingFunction::get()} output
	 * its absolute value. This may make the function behave as if the periodicity
	 * of its parameter has doubled. Toggling this {@code boolean} while the wave is
	 * active may show unwantedly large, sudden changes in the value given by the
	 * function.
	 *
	 * @apiNote {@code false} by default.
	 */
	public boolean absoluteValue = false;

	protected final NerdSketch<?> SKETCH;

	protected Runnable onEnd;
	protected float lastValue;
	protected int lastValueFrameCount;
	protected boolean pactive = false;
	// endregion

	protected NerdEasingFunction(final NerdSketch<?> p_parentSketch) {
		this.SKETCH = p_parentSketch;
	}

	// region `start()` overloads and `setParameterOffset()`.
	public NerdEasingFunction resetValues() {
		this.endTime = -1;
		this.aliveTime = 0;
		this.parameter = 0;
		this.parameterOffset = 0;
		return this;
	}

	public NerdEasingFunction resetSettings() {
		this.inactValue = 0;
		this.active = false;
		this.useInactValue = false;
		this.absoluteValue = false;
		this.pactive = false;
		this.onEnd = null;
		return this;
	}

	public NerdEasingFunction removeOnEndCallback() {
		this.onEnd = null;
		return this;
	}

	public NerdEasingFunction completeReset() {
		this.resetValues();
		this.resetSettings();
		return this;
	}

	public NerdEasingFunction start() {
		this.aliveTime = 0;
		this.active = true;
		return this;
	}

	public NerdEasingFunction start(final Runnable p_onEnd) {
		this.onEnd = p_onEnd;
		this.aliveTime = 0;
		this.active = true;
		return this;
	}

	public NerdEasingFunction start(final float p_paramOffset) {
		this.parameterOffset = p_paramOffset;
		this.aliveTime = 0;
		this.active = true;
		return this;
	}

	public NerdEasingFunction start(final float p_paramOffset, final Runnable p_onEnd) {
		this.parameterOffset = p_paramOffset;
		this.onEnd = p_onEnd;
		this.aliveTime = 0;
		this.active = true;
		return this;
	}
	// endregion

	// region End and extend!
	public NerdEasingFunction stop() {
		this.endTime = 0;
		return this;
	}

	public NerdEasingFunction endAfterMillis(final int p_millis) {
		this.endTime = this.aliveTime + p_millis;
		return this;
	}

	public NerdEasingFunction extendEndByMillis(final int p_millis) {
		this.endTime += p_millis;
		return this;
	}
	// endregion

	// *Abstractness!:tm:*
	protected abstract float apply();

	// region Getters.
	public float getStartTime() {
		return this.SKETCH.millis() - this.aliveTime;
	}

	public float getTimeSinceStart() {
		return this.aliveTime;
	}

	public float getLastValue() {
		return this.lastValue;
	}

	public float getEndTime() {
		return this.endTime;
	}

	public boolean wasActive() {
		return this.pactive;
	}

	/**
	 * @return Returns a cached sample of the function at the time value recorded at
	 *         the start of the frame.
	 */
	public float getFramelyValue() {
		// If we've already calculated this value,
		return this.SKETCH.frameCount == this.lastValueFrameCount
				// ...We just return it! (`this.lastValue` does not store the absolute value
				// version so users can turn that setting off and we can still use this cache!):
				? this.absoluteValue ? Math.abs(this.lastValue) : this.lastValue
				// ...Else we fetch and cache a new value for this frame:
				: this.get();
	}

	/**
	 * @return Resamples the function at with the current time value to give you the
	 *         latest value.
	 */
	public float get() {
		this.pactive = this.active;

		if (this.endTime != -1)
			this.active = this.aliveTime <= this.endTime;

		if (this.active)
			this.aliveTime += this.SKETCH.getFrameTime();

		// ^^^ `frameTime` comes from "the engine" by the way. (Hey - that's "Nerd"!)
		else { // If no longer active,
			if (this.pactive)
				if (this.onEnd != null)
					this.onEnd.run();

			return this.useInactValue ? this.inactValue : this.lastValue;
		}

		this.lastValue = this.apply();
		this.lastValueFrameCount = this.SKETCH.frameCount;
		return this.absoluteValue ? Math.abs(this.lastValue) : this.lastValue;
	}
	// endregion

}
