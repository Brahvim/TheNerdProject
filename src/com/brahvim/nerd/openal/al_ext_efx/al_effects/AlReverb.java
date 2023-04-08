package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlReverb extends AlEffect {

	public AlReverb(final NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_REVERB;
	}

	// region Getters.
	public float getReverbDensity() {
		return super.getFloat(EXTEfx.AL_REVERB_DENSITY);
	}

	public float getReverbDiffusion() {
		return super.getFloat(EXTEfx.AL_REVERB_DIFFUSION);
	}

	public float getReverbGain() {
		return super.getFloat(EXTEfx.AL_REVERB_GAIN);
	}

	public float getReverbGainHf() {
		return super.getFloat(EXTEfx.AL_REVERB_GAINHF);
	}

	public float getReverbDecayTime() {
		return super.getFloat(EXTEfx.AL_REVERB_DECAY_TIME);
	}

	public float getReverbDecayHfRatio() {
		return super.getFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO);
	}

	public float getReverbReflectionsGain() {
		return super.getFloat(EXTEfx.AL_REVERB_REFLECTIONS_GAIN);
	}

	public float getReverbReflectionsDelay() {
		return super.getFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY);
	}

	public float getReverbLateReverbGain() {
		return super.getFloat(EXTEfx.AL_REVERB_LATE_REVERB_GAIN);
	}

	public float getReverbLateReverbDelay() {
		return super.getFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY);
	}

	public float getReverbAirAbsorptionGainHf() {
		return super.getFloat(EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF);
	}

	public float getReverbRoomRolloffFactor() {
		return super.getFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR);
	}

	public boolean getReverbDecayHfLimit() {
		return super.getInt(EXTEfx.AL_REVERB_DECAY_HFLIMIT) == AL11.AL_TRUE;
	}
	// endregion

	// region Setters.
	public AlReverb setReverbDensity(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DENSITY, p_value);
		return this;
	}

	public AlReverb setReverbDiffusion(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DIFFUSION, p_value);
		return this;
	}

	public AlReverb setReverbGain(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_GAIN, p_value);
		return this;
	}

	public AlReverb setReverbGainHf(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_GAINHF, p_value);
		return this;
	}

	public AlReverb setReverbDecayTime(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DECAY_TIME, p_value);
		return this;
	}

	public AlReverb setReverbDecayHfRatio(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO, p_value);
		return this;
	}

	public AlReverb setReverbReflectionsGain(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_REFLECTIONS_GAIN, p_value);
		return this;
	}

	public AlReverb setReverbReflectionsDelay(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY, p_value);
		return this;
	}

	public AlReverb setReverbLateReverbGain(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_LATE_REVERB_GAIN, p_value);
		return this;
	}

	public AlReverb setReverbLateReverbDelay(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY, p_value);
		return this;
	}

	public AlReverb setReverbAirAbsorptionGainHf(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF, p_value);
		return this;
	}

	public AlReverb setReverbRoomRolloffFactor(final float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR, p_value);
		return this;
	}

	public AlReverb setReverbDecayHfLimit(final boolean p_value) {
		super.setInt(EXTEfx.AL_REVERB_DECAY_HFLIMIT, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
		return this;
	}
	// endregion

}