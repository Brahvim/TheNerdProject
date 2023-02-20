package com.brahvim.nerd.openal.al_exceptions;

import org.lwjgl.openal.ALC11;

public class NerdAlcException extends NerdAbstractOpenAlException {
	private static final long serialVersionUID = -79354861264L;

	public NerdAlcException(int p_alErrorCode) {
		super(ALC11.alcGetString(0, ALC11.alcGetError(p_alErrorCode)));
		super.errCode = p_alErrorCode;
	}

	public int getAlErrorCode() {
		return super.errCode;
	}
}
