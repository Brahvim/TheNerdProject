package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;

public class AlBandpassFilter extends AlFilter {

	public AlBandpassFilter(NerdAl p_alMan) {
		super(p_alMan, EXTEfx.AL_FILTER_BANDPASS);
	}

	// region Getters.
	public float getBandpassGain() {
		return super.getFloat(EXTEfx.AL_BANDPASS_GAIN);
	}

	public float getBandpassGainLf() {
		return super.getFloat(EXTEfx.AL_BANDPASS_GAINLF);
	}

	public float getBandpassGainHf() {
		return super.getFloat(EXTEfx.AL_BANDPASS_GAINHF);
	}
	// endregion

	// region Setters.
	public void setBandpassGain(float p_value) {
		super.setFloat(EXTEfx.AL_BANDPASS_GAIN, p_value);
	}

	public void setBandpassGainLf(float p_value) {
		super.setFloat(EXTEfx.AL_BANDPASS_GAINLF, p_value);
	}

	public void setBandpassGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_BANDPASS_GAINHF, p_value);
	}
	// endregion

}
