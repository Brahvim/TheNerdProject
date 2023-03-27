package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlRingModulator extends AlEffect {

	public AlRingModulator(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_RING_MODULATOR;
	}

	// region Getters.
	public float getRingModulatorFrequency() {
		return super.getFloat(EXTEfx.AL_RING_MODULATOR_FREQUENCY);
	}

	public float getRingModulatorWaveform() {
		return super.getFloat(EXTEfx.AL_RING_MODULATOR_WAVEFORM);
	}

	public float getRingModulatorHighpassCutoff() {
		return super.getFloat(EXTEfx.AL_RING_MODULATOR_HIGHPASS_CUTOFF);
	}
	// endregion

	// region Setters.
	public AlRingModulator setRingModulatorFrequency(float p_value) {
		super.setFloat(EXTEfx.AL_RING_MODULATOR_FREQUENCY, p_value);
		return this;
	}

	public AlRingModulator setRingModulatorWaveform(float p_value) {
		super.setFloat(EXTEfx.AL_RING_MODULATOR_WAVEFORM, p_value);
		return this;
	}

	public AlRingModulator setRingModulatorHighpassCutoff(float p_value) {
		super.setFloat(EXTEfx.AL_RING_MODULATOR_HIGHPASS_CUTOFF, p_value);
		return this;
	}
	// endregion

}
