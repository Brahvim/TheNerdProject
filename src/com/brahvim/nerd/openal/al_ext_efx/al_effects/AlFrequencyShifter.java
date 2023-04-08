package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlFrequencyShifter extends AlEffect {

	public AlFrequencyShifter(final NerdAl p_alMan) {
		super(p_alMan);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER;
	}

	// region Getters.
	public float getFrequencyShifterFrequency() {
		return super.getFloat(EXTEfx.AL_FREQUENCY_SHIFTER_FREQUENCY);
	}

	public float getFrequencyShifterLeftDirection() {
		return super.getFloat(EXTEfx.AL_FREQUENCY_SHIFTER_LEFT_DIRECTION);
	}

	public float getFrequencyShifterRightDirection() {
		return super.getFloat(EXTEfx.AL_FREQUENCY_SHIFTER_RIGHT_DIRECTION);
	}
	// endregion

	// region Setters.
	public AlFrequencyShifter setFrequencyShifterFrequency(final float p_value) {
		super.setFloat(EXTEfx.AL_FREQUENCY_SHIFTER_FREQUENCY, p_value);
		return this;
	}

	public AlFrequencyShifter setFrequencyShifterLeftDirection(final float p_value) {
		super.setFloat(EXTEfx.AL_FREQUENCY_SHIFTER_LEFT_DIRECTION, p_value);
		return this;
	}

	public AlFrequencyShifter setFrequencyShifterRightDirection(final float p_value) {
		super.setFloat(EXTEfx.AL_FREQUENCY_SHIFTER_RIGHT_DIRECTION, p_value);
		return this;
	}
	// endregion

}
