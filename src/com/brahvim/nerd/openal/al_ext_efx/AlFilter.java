package com.brahvim.nerd.openal.al_ext_efx;

import org.lwjgl.openal.EXTEfx;

import com.brahvim.nerd.openal.NerdAl;

public class AlFilter {

	private int id;
	private NerdAl alMan;

	public AlFilter(NerdAl p_alMan) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenFilters();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public int getId() {
		return this.id;
	}

	public void dispose() {
		EXTEfx.alDeleteFilters(this.id);
	}

}
