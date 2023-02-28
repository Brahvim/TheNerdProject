package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlChorus extends AlEffect {

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_CHORUS;
	}

	// region Getters.
	public float getChorusWaveform() {
		return super.getFloat(EXTEfx.AL_CHORUS_WAVEFORM);
	}

	public float getChorusWaveformTriangle() {
		return super.getFloat(EXTEfx.AL_CHORUS_WAVEFORM_TRIANGLE);
	}

	public float getChorusPhase() {
		return super.getFloat(EXTEfx.AL_CHORUS_PHASE);
	}

	public float getChorusRate() {
		return super.getFloat(EXTEfx.AL_CHORUS_RATE);
	}

	public float getChorusDepth() {
		return super.getFloat(EXTEfx.AL_CHORUS_DEPTH);
	}

	public float getChorusFeedback() {
		return super.getFloat(EXTEfx.AL_CHORUS_FEEDBACK);
	}

	public float getChorusDelay() {
		return super.getFloat(EXTEfx.AL_CHORUS_DELAY);
	}
	// endregion

	// region Setters.
	public void setChorusWaveform(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_WAVEFORM, p_value);
	}

	public void setChorusWaveformTriangle(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_WAVEFORM_TRIANGLE, p_value);
	}

	public void setChorusPhase(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_PHASE, p_value);
	}

	public void setChorusRate(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_RATE, p_value);
	}

	public void setChorusDepth(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_DEPTH, p_value);
	}

	public void setChorusFeedback(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_FEEDBACK, p_value);
	}

	public void setChorusDelay(float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_DELAY, p_value);
	}
	// endregion

}
