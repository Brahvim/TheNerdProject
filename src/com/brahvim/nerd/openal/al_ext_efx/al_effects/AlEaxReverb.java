package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlEaxReverb extends AlEffect {

	public AlEaxReverb(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_EAXREVERB;
	}

	// region Getters.
	public float getEaxReverbDensity() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DENSITY);
	}

	public float getEaxReverbDiffusion() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DIFFUSION);
	}

	public float getEaxReverbGain() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_GAIN);
	}

	public float getEaxReverbGainHf() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_GAINHF);
	}

	public float getEaxReverbGainLf() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_GAINLF);
	}

	public float getEaxReverbDecayTime() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DECAY_TIME);
	}

	public float getEaxReverbDecayHfRatio() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DECAY_HFRATIO);
	}

	public float getEaxReverbDecayLfRatio() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DECAY_LFRATIO);
	}

	public float getEaxReverbReflectionsGain() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN);
	}

	public float getEaxReverbReflectionsDelay() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY);
	}

	public float getEaxReverbReflectionsPan() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_PAN);
	}

	public float getEaxReverbLateReverbGain() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN);
	}

	public float getEaxReverbLateReverbDelay() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY);
	}

	public float getEaxReverbLateReverbPan() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_PAN);
	}

	public float getEaxReverbEchoTime() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_ECHO_TIME);
	}

	public float getEaxReverbEchoDepth() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_ECHO_DEPTH);
	}

	public float getEaxReverbModulationTime() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_MODULATION_TIME);
	}

	public float getEaxReverbModulationDepth() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_MODULATION_DEPTH);
	}

	public float getEaxReverbAirAbsorptionGainHf() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF);
	}

	public float getEaxReverbHfReference() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_HFREFERENCE);
	}

	public float getEaxReverbRoomRolloffFactor() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR);
	}

	public float getEaxReverbDecayHfLimit() {
		return super.getFloat(EXTEfx.AL_EAXREVERB_DECAY_HFLIMIT);
	}
	// endregion

	// region Setters.
	public AlEaxReverb setEaxReverbDensity(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DENSITY, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbDiffusion(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DIFFUSION, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbGain(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_GAIN, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_GAINHF, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbGainLf(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_GAINLF, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbDecayTime(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DECAY_TIME, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbDecayHfRatio(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbDecayLfRatio(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DECAY_LFRATIO, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbReflectionsGain(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbReflectionsDelay(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbReflectionsPan(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_PAN, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbLateReverbGain(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbLateReverbDelay(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbLateReverbPan(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_PAN, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbEchoTime(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_ECHO_TIME, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbEchoDepth(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_ECHO_DEPTH, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbModulationTime(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_MODULATION_TIME, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbModulationDepth(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_MODULATION_DEPTH, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbAirAbsorptionGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbHfReference(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_HFREFERENCE, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbRoomRolloffFactor(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, p_value);
		return this;
	}

	public AlEaxReverb setEaxReverbDecayHfLimit(float p_value) {
		super.setFloat(EXTEfx.AL_EAXREVERB_DECAY_HFLIMIT, p_value);
		return this;
	}
	// endregion

}
