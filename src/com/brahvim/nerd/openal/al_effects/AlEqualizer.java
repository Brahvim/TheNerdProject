package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlEqualizer extends AlEffect {

	public AlEqualizer(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_EQUALIZER;
	}

	// region Getters.
	public float getEqualizerLowGain() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_LOW_GAIN);
	}

	public float getEqualizerLowCutoff() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_LOW_CUTOFF);
	}

	public float getEqualizerMid1Gain() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID1_GAIN);
	}

	public float getEqualizerMid1Center() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID1_CENTER);
	}

	public float getEqualizerMid1Width() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID1_WIDTH);
	}

	public float getEqualizerMid2Gain() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID2_GAIN);
	}

	public float getEqualizerMid2Center() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID2_CENTER);
	}

	public float getEqualizerMid2Width() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_MID2_WIDTH);
	}

	public float getEqualizerHighGain() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_HIGH_GAIN);
	}

	public float getEqualizerHighCutoff() {
		return super.getFloat(EXTEfx.AL_EQUALIZER_HIGH_CUTOFF);
	}
	// endregion

	// region Setters.
	public void setEqualizerLowGain(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_LOW_GAIN, p_value);
	}

	public void setEqualizerLowCutoff(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_LOW_CUTOFF, p_value);
	}

	public void setEqualizerMid1Gain(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID1_GAIN, p_value);
	}

	public void setEqualizerMid1Center(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID1_CENTER, p_value);
	}

	public void setEqualizerMid1Width(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID1_WIDTH, p_value);
	}

	public void setEqualizerMid2Gain(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID2_GAIN, p_value);
	}

	public void setEqualizerMid2Center(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID2_CENTER, p_value);
	}

	public void setEqualizerMid2Width(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_MID2_WIDTH, p_value);
	}

	public void setEqualizerHighGain(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_HIGH_GAIN, p_value);
	}

	public void setEqualizerHighCutoff(float p_value) {
		super.setFloat(EXTEfx.AL_EQUALIZER_HIGH_CUTOFF, p_value);
	}
	// endregion

}
