package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlFlanger extends AlEffect {

	public AlFlanger(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_FLANGER;
	}

	// region Getters.
	public float getFlangerWaveform() {
		return super.getFloat(EXTEfx.AL_FLANGER_WAVEFORM);
	}

	public float getFlangerPhase() {
		return super.getFloat(EXTEfx.AL_FLANGER_PHASE);
	}

	public float getFlangerRate() {
		return super.getFloat(EXTEfx.AL_FLANGER_RATE);
	}

	public float getFlangerDepth() {
		return super.getFloat(EXTEfx.AL_FLANGER_DEPTH);
	}

	public float getFlangerFeedback() {
		return super.getFloat(EXTEfx.AL_FLANGER_FEEDBACK);
	}

	public float getFlangerDelay() {
		return super.getFloat(EXTEfx.AL_FLANGER_DELAY);
	}
	// endregion

	// region Setters.
	public void setFlangerWaveform(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_WAVEFORM, p_value);
	}

	public void setFlangerPhase(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_PHASE, p_value);
	}

	public void setFlangerRate(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_RATE, p_value);
	}

	public void setFlangerDepth(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_DEPTH, p_value);
	}

	public void setFlangerFeedback(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_FEEDBACK, p_value);
	}

	public void setFlangerDelay(float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_DELAY, p_value);
	}
	// endregion

}
