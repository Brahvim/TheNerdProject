package com.brahvim.nerd.openal.al_exceptions;

import org.lwjgl.openal.ALC11;

public class AlcException extends NerdAbstractOpenAlException {

	private static final long serialVersionUID = -79354861264L;

	public AlcException(int p_alErrorCode) {
		super(ALC11.alcGetString(0, ALC11.alcGetError(p_alErrorCode)));
		super.errCode = p_alErrorCode;
	}

	public AlcException(String p_error) {
		super(p_error);
	}

}
