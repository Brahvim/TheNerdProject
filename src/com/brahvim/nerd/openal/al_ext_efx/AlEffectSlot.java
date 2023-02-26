package com.brahvim.nerd.openal.al_ext_efx;

import org.lwjgl.openal.EXTEfx;

public class AlEffectSlot {
	private int id;
	private AlEffect effect;

	public AlEffectSlot() {
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();
	}

	public AlEffect getEffect() {
		return this.effect;
	}

	public AlEffect setEffect(AlEffect p_effect) {
		return this.effect = p_effect;
	}

	public void dispose() {
		EXTEfx.alDeleteAuxiliaryEffectSlots(this.id);
	}

}
