package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlFlanger extends AlEffect {

	public AlFlanger(final NerdAl p_NerdAl) {
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
	public AlFlanger setFlangerWaveform(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_WAVEFORM, p_value);
		return this;
	}

	public AlFlanger setFlangerPhase(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_PHASE, p_value);
		return this;
	}

	public AlFlanger setFlangerRate(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_RATE, p_value);
		return this;
	}

	public AlFlanger setFlangerDepth(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_DEPTH, p_value);
		return this;
	}

	public AlFlanger setFlangerFeedback(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_FEEDBACK, p_value);
		return this;
	}

	public AlFlanger setFlangerDelay(final float p_value) {
		super.setFloat(EXTEfx.AL_FLANGER_DELAY, p_value);
		return this;
	}
	// endregion

}
