package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;

public class AlLowpassFilter extends AlFilter {

	public AlLowpassFilter(NerdAl p_alMan) {
		super(p_alMan, EXTEfx.AL_FILTER_LOWPASS);
	}

	// region Getters.
	public float getLowpassGain() {
		return super.getFloat(EXTEfx.AL_LOWPASS_GAIN);
	}

	public float getLowpassGainHf() {
		return super.getFloat(EXTEfx.AL_LOWPASS_GAINHF);
	}
	// endregion

	// region Setters.
	public void setLowpassGain(float p_value) {
		super.setFloat(EXTEfx.AL_LOWPASS_GAIN, p_value);
	}

	public void setLowpassGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_LOWPASS_GAINHF, p_value);
	}
	// endregion

}
