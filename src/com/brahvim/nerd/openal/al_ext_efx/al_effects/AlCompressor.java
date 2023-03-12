package com.brahvim.nerd.openal.al_ext_efx.al_effects;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlCompressor extends AlEffect {

	public AlCompressor(NerdAl p_NerdAl) {
		super(p_NerdAl);
	}

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_COMPRESSOR;
	}

	public boolean getStatus() {
		return super.getInt(EXTEfx.AL_COMPRESSOR_ONOFF) == AL11.AL_TRUE;
	}

	public void setStatus(boolean p_value) {
		super.setInt(EXTEfx.AL_COMPRESSOR_ONOFF, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}

}
