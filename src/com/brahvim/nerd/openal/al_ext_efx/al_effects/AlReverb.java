package com.brahvim.nerd.openal.al_ext_efx.al_effects;

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

}
