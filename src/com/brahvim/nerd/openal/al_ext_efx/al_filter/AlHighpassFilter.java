package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;

public class AlHighpassFilter extends AlFilter {

	public AlHighpassFilter(NerdAl p_alMan) {
		super(p_alMan);
	}

	@Override
	public int getName() {
		return EXTEfx.AL_FILTER_HIGHPASS;
	}

	// region Getters.
	public float getHighpassGain() {
		return super.getFloat(EXTEfx.AL_HIGHPASS_GAIN);
	}

	public float getHighpassGainLf() {
		return super.getFloat(EXTEfx.AL_HIGHPASS_GAINLF);
	}
	// endregion

	// region Setters.
	public AlHighpassFilter setHighpassGain(float p_value) {
		super.setFloat(EXTEfx.AL_HIGHPASS_GAIN, p_value);
		return this;
	}

	public AlHighpassFilter setHighpassGainLf(float p_value) {
		super.setFloat(EXTEfx.AL_HIGHPASS_GAINLF, p_value);
		return this;
	}
	// endregion

}
