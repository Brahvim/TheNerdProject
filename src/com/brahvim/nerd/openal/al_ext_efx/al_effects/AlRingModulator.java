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
}
