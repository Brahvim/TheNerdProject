package com.brahvim.nerd.openal.al_effects;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.al_ext_efx.AlEffect;

public class AlCompressor extends AlEffect {

	@Override
	protected int getEffectType() {
		return EXTEfx.AL_EFFECT_COMPRESSOR;
	}

}
