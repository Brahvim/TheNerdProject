package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlChorus extends AlEffect {

	public AlChorus(final NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

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
	public AlChorus setChorusWaveform(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_WAVEFORM, p_value);
		return this;
	}

	public AlChorus setChorusWaveformTriangle(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_WAVEFORM_TRIANGLE, p_value);
		return this;
	}

	public AlChorus setChorusPhase(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_PHASE, p_value);
		return this;
	}

	public AlChorus setChorusRate(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_RATE, p_value);
		return this;
	}

	public AlChorus setChorusDepth(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_DEPTH, p_value);
		return this;
	}

	public AlChorus setChorusFeedback(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_FEEDBACK, p_value);
		return this;
	}

	public AlChorus setChorusDelay(final float p_value) {
		super.setFloat(EXTEfx.AL_CHORUS_DELAY, p_value);
		return this;
	}
	// endregion

}
