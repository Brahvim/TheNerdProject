package com.brahvim.nerd.openal.al_ext_efx;

import org.lwjgl.openal.EXTEfx;

public class AlFilter {
	private int id;

	public AlFilter() {
		this.id = EXTEfx.alGenFilters();
	}

	public int getId() {
		return this.id;
	}

	public void dispose() {
		EXTEfx.alDeleteFilters(this.id);
	}

}
