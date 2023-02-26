package com.brahvim.nerd.openal.al_ext_efx;

import org.lwjgl.openal.EXTEfx;

public class AlEffect {
	private int id;

	public AlEffect() {
		this.id = EXTEfx.alGenEffects();
	}

	public int getId() {
		return this.id;
	}

	public void dispose() {
		EXTEfx.alDeleteEffects(this.id);
	}
}
