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
	public int getPitchShifterFineTune() {
		return super.getInt(EXTEfx.AL_PITCH_SHIFTER_FINE_TUNE);
	}

	public int getPitchShifterCoarseTune() {
		return super.getInt(EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE);
	}
	// endregion

	// region Setters.
	public AlPitchShifter setPitchShifterFineTune(int p_value) {
		super.setInt(EXTEfx.AL_PITCH_SHIFTER_FINE_TUNE, p_value);
		return this;
	}

	public AlPitchShifter setPitchShifterCoarseTune(int p_value) {
		super.setInt(EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE, p_value);
		return this;
	}
	// endregion

}
