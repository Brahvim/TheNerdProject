package com.brahvim.nerd.openal.al_ext_efx;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;

public class AlEffectSlot {

	private int id;
	private NerdAl alMan;
	private AlEffect effect;

	public AlEffectSlot(NerdAl p_alMan) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public int getId() {
		return this.id;
	}

	public AlEffect getEffect() {
		return this.effect;
	}

	public AlEffect setEffect(AlEffect p_effect) {
		EXTEfx.alAuxiliaryEffectSloti(id, id, id);
		return this.effect = p_effect;
	}

	public void dispose() {
		EXTEfx.alDeleteAuxiliaryEffectSlots(this.id);
	}

}
