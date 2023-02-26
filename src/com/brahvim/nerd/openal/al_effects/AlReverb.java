package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlReverb extends AlEffect {

	public AlReverb(NerdAl p_alMan) {
		super(p_alMan);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_REVERB;
	}

	// region Getters.
	public float getDensity() {
		return super.getFloat(EXTEfx.AL_REVERB_DENSITY);
	}

	public float getDiffusion() {
		return super.getFloat(EXTEfx.AL_REVERB_DIFFUSION);
	}

	public float getGain() {
		return super.getFloat(EXTEfx.AL_REVERB_GAIN);
	}

	public float getGainHf() {
		return super.getFloat(EXTEfx.AL_REVERB_GAINHF);
	}

	public float getDecayTime() {
		return super.getFloat(EXTEfx.AL_REVERB_DECAY_TIME);
	}

	public boolean getDecayHfLimit() {
		return super.getInt(EXTEfx.AL_REVERB_DECAY_HFRATIO) == AL11.AL_TRUE;
	}

	public float getDecayHfRatio() {
		return super.getFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO);
	}

	public float getLateReverbGain() {
		return super.getFloat(EXTEfx.AL_REVERB_LATE_REVERB_GAIN);
	}

	public float getLateReverbDelay() {
		return super.getFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY);
	}

	public float getReverbReflectionsGain() {
		return super.getFloat(EXTEfx.AL_REVERB_REFLECTIONS_GAIN);
	}

	public float getReverbReflectionsDelay() {
		return super.getFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY);
	}

	public float getAirAbsorptionGainHf() {
		return super.getFloat(EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF);
	}

	public float getRoomRolloffFactor() {
		return super.getFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR);
	}
	// endregion

	// region Setters.
	public void setDensity(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DENSITY, p_value);
	}

	public void setDiffusion(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DIFFUSION, p_value);
	}

	public void setGain(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_GAIN, p_value);
	}

	public void setGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_GAINHF, p_value);
	}

	public void setDecayTime(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DECAY_TIME, p_value);
	}

	public void setDecayHfLimit(boolean p_value) {
		super.setInt(EXTEfx.AL_REVERB_DECAY_HFLIMIT, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}

	public void setDecayHfRatio(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO, p_value);
	}

	public void setLateReverbGain(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_LATE_REVERB_GAIN, p_value);
	}

	public void setLateReverbDelay(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY, p_value);
	}

	public void setReverbReflectionsGain(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_REFLECTIONS_GAIN, p_value);
	}

	public void setReverbReflectionsDelay(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY, p_value);
	}

	public void setAirAbsorptionGainHf(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF, p_value);
	}

	public void setRoomRolloffFactor(float p_value) {
		super.setFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR, p_value);
	}
	// endregion

}