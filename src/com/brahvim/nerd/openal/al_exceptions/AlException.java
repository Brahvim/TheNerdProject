package com.brahvim.nerd.openal.al_exceptions;

import org.lwjgl.openal.ALC11;

public class AlException extends NerdAbstractOpenAlException {

	private static final long serialVersionUID = -565443463464L;

	public AlException(int p_alErrorCode) {
		super(ALC11.alcGetString(0, ALC11.alcGetError(p_alErrorCode)));
		super.errCode = p_alErrorCode;
	}

	public AlException(String p_error) {
		super(p_error);
	}

}
