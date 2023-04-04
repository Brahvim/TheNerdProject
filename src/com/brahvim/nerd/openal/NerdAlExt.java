package com.brahvim.nerd.openal;

import com.brahvim.nerd.ext.NerdExt;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class NerdAlExt extends NerdExt {

	private NerdAl alInst;

	public NerdAlExt(final Sketch p_sketch) {
		super(p_sketch);
	}

	@Override
	protected void initExt() {
		this.alInst = new NerdAl(super.SKETCH);
	}

}
