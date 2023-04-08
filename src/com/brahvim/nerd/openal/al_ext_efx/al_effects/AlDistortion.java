package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlDistortion extends AlEffect {

	public AlDistortion(final NerdAl p_NerdAl) {
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
	public AlDistortion setDistortionEdge(final float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EDGE, p_value);
		return this;
	}

	public AlDistortion setDistortionGain(final float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_GAIN, p_value);
		return this;
	}

	public AlDistortion setDistortionLowpassCutoff(final float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_LOWPASS_CUTOFF, p_value);
		return this;
	}

	public AlDistortion setDistortionEqCenter(final float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EQCENTER, p_value);
		return this;
	}

	public AlDistortion setDistortionEqBandwidth(final float p_value) {
		super.setFloat(EXTEfx.AL_DISTORTION_EQBANDWIDTH, p_value);
		return this;
	}
	// endregion

}
