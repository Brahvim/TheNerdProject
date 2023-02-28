package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlEcho extends AlEffect {

	public AlEcho(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_ECHO;
	}

	// region Getters.
	public float getEchoDelay() {
		return super.getFloat(EXTEfx.AL_ECHO_DELAY);
	}

	public float getEchoLrDelay() {
		return super.getFloat(EXTEfx.AL_ECHO_LRDELAY);
	}

	public float getEchoDamping() {
		return super.getFloat(EXTEfx.AL_ECHO_DAMPING);
	}

	public float getEchoFeedback() {
		return super.getFloat(EXTEfx.AL_ECHO_FEEDBACK);
	}

	public float getEchoSpread() {
		return super.getFloat(EXTEfx.AL_ECHO_SPREAD);
	}
	// endregion

	// region Setters.
	public void setEchoDelay(float p_value) {
		super.setFloat(EXTEfx.AL_ECHO_DELAY, p_value);
	}

	public void setEchoLrDelay(float p_value) {
		super.setFloat(EXTEfx.AL_ECHO_LRDELAY, p_value);
	}

	public void setEchoDamping(float p_value) {
		super.setFloat(EXTEfx.AL_ECHO_DAMPING, p_value);
	}

	public void setEchoFeedback(float p_value) {
		super.setFloat(EXTEfx.AL_ECHO_FEEDBACK, p_value);
	}

	public void setEchoSpread(float p_value) {
		super.setFloat(EXTEfx.AL_ECHO_SPREAD, p_value);
	}
	// endregion

}
