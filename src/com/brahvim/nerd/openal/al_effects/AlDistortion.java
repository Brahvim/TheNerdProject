package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlDistortion extends AlEffect {

	public AlDistortion(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_DISTORTION;
	}

	// region Getters.
	public float getDistortionEdge() {
		return super.getFloat(EXTEfx.AL_DISTORTION_EDGE);
	}

	public float getDistortionGain() {
		return super.getFloat(EXTEfx.AL_DISTORTION_GAIN);
	}

	public float getDistortionLowpassCutoff() {
		return super.getFloat(EXTEfx.AL_DISTORTION_LOWPASS_CUTOFF);
	}

	public float getDistortionEqCenter() {
		return super.getFloat(EXTEfx.AL_DISTORTION_EQCENTER);
	}

	public float getDistortionEqBandwidth() {
		return super.getFloat(EXTEfx.AL_DISTORTION_EQBANDWIDTH);
	}
	// endregion

	// region Setters.
	public void setDistortionEdge(float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EDGE, p_value);
	}

	public void setDistortionGain(float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_GAIN, p_value);
	}

	public void setDistortionLowpassCutoff(float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_LOWPASS_CUTOFF, p_value);
	}

	public void setDistortionEqCenter(float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EQCENTER, p_value);
	}

	public void setDistortionEqBandwidth(float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EQBANDWIDTH, p_value);
	}
	// endregion

}
