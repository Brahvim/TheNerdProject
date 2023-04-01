package com.brahvim.nerd.openal.al_exceptions;

import org.lwjgl.openal.ALC11;

public class AlcException extends NerdAbstractOpenAlException {

	private static final long serialVersionUID = -79354861264L;

	public AlcException(long p_deviceId, int p_alErrorCode) {
		super(ALC11.alcGetString(p_deviceId, ALC11.alcGetError(p_alErrorCode)), p_alErrorCode);
	}

}
