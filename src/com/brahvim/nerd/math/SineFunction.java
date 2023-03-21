package com.brahvim.nerd.math;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;

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

	// No matter what order you put them in, the API Note comes after the Author
	// name. Oof.
	/**
	 * @author Brahvim
	 * @apiNote {@code p_angleOffset} must be in radians!
	 */
	public SineFunction(Sketch p_parentSketch, float p_freqMult, float p_angleOffset) {
		super(p_parentSketch);
		this.freqMult = p_freqMult;
		super.parameterOffset = p_angleOffset;
		// super.parameterOffset = Math.abs((float) Math.toRadians(p_angleOffset));
	}
	// endregion

	// region End and extend!
	public SineFunction end() {
		super.endTime = 0;
		return this;
	}

	public SineFunction endIn(float p_millis) {
		super.endTime = super.aliveTime + p_millis;
		return this;
	}

	public SineFunction endWhenAngleIs(float p_angle) {
		// Of course this magic-number stuff won't work everytime! REPLACE THIS?!
		// ~~(Also because... any multiple of `0.25` less than `1` works...)~~
		// Oh hey! That looks perfect!:
		this.endTime = 0.9f * // <--- The magic number!.
				Math.abs(((float) Math.toRadians(p_angle) - super.parameterOffset) / this.freqMult);
		return this;

		// Was an idea to avoid I don't get negative values and the frequency doesn't
		// double - but I didn't use it.. don't ask me why :joy::
		// super.endTime = ((float) Math.toRadians(p_angle) - super.parameterOffset)
		// / this.freqMult; // PS This was what I calculated on paper.
		// super.endTime = Math.abs((super.endTime - super.aliveTime) * 0.5f);

		// The calculation I used in the frametime version of this class, as seen in
		// the Nerd Game Engine:
		// super.endTime = super.aliveTime + ((float) (p_angle) * ((float)
		// Math.toRadians(p_angle) *
		// this.freqMult)) - super.parameterOffset;

		// From the early days - the framecount calculation!:
		// super.endTime = super.aliveTime + (int) (p_angle * (p_angle * this.freqMult)
		// - super.parameterOffset);

		// System.out.println(super.aliveTime);
		// System.out.println(super.endTime);
	}

	public SineFunction endWhenAngleIs(float p_angle, float p_before) {
		this.endWhenAngleIs(p_angle);

		if (super.endTime < super.aliveTime + p_before) {
			super.endTime -= p_before;
			return this;
		} else // If they're equal, this still takes place!
			return this.end();
	}

	public SineFunction extendEndBy(float p_millis) {
		super.endTime += p_millis;
		return this;
	}

	public SineFunction extendEndByAngle(float p_angle) {
		// ..here from, ChatGPT...
		float time = (p_angle / (2 * PConstants.PI * this.freqMult)) / super.parameter;
		super.endTime += time * 1000;

		// super.endTime += 0.9f *
		// Math.abs(((float) Math.toRadians(p_angle) - super.parameterOffset) /
		// this.freqMult)
		// - super.endTime;
		return this;

		// super.endTime += (p_angle * (p_angle * this.freqMult) -
		// super.parameterOffset);
	}
	// endregion

	@Override
	protected float apply() {
		return PApplet.sin(super.aliveTime * this.freqMult + super.parameterOffset);
	}

}
