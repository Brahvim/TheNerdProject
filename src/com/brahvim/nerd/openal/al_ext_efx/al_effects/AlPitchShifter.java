package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlPitchShifter extends AlEffect {

	public AlPitchShifter(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_PITCH_SHIFTER;
	}

	// region Getters.
	public float getPitchShifterCoarseTune() {
		return super.getFloat(EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE);
	}

	public float getPitchShifterFineTune() {
		return super.getFloat(EXTEfx.AL_PITCH_SHIFTER_FINE_TUNE);
	}
	// endregion

	// region Setters.
	public void setPitchShifterCoarseTune(float p_value) {
		super.setFloat(EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE, p_value);
	}

	public void setPitchShifterFineTune(float p_value) {
		super.setFloat(EXTEfx.AL_PITCH_SHIFTER_FINE_TUNE, p_value);
	}
	// endregion

}
