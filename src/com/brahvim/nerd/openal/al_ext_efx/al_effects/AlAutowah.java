package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlAutowah extends AlEffect {

	public AlAutowah(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_AUTOWAH;
	}

	// region Getters.
	public float getAutowahAttackTime() {
		return super.getFloat(EXTEfx.AL_AUTOWAH_ATTACK_TIME);
	}

	public float getAutowahReleaseTime() {
		return super.getFloat(EXTEfx.AL_AUTOWAH_RELEASE_TIME);
	}

	public float getAutowahResonance() {
		return super.getFloat(EXTEfx.AL_AUTOWAH_RESONANCE);
	}

	public float getAutowahPeakGain() {
		return super.getFloat(EXTEfx.AL_AUTOWAH_PEAK_GAIN);
	}
	// endregion

	// region Setters.
	public void setAutowahAttackTime(float p_value) {
		super.setFloat(EXTEfx.AL_AUTOWAH_ATTACK_TIME, p_value);
	}

	public void setAutowahReleaseTime(float p_value) {
		super.setFloat(EXTEfx.AL_AUTOWAH_RELEASE_TIME, p_value);
	}

	public void setAutowahResonance(float p_value) {
		super.setFloat(EXTEfx.AL_AUTOWAH_RESONANCE, p_value);
	}

	public void setAutowahPeakGain(float p_value) {
		super.setFloat(EXTEfx.AL_AUTOWAH_PEAK_GAIN, p_value);
	}
	// endregion

}