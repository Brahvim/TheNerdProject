package com.brahvim.nerd.openal;

import org.lwjgl.openal.ALC11;

public class NerdAlException extends RuntimeException {
	private static final long serialVersionUID = -565443463464L;

	public NerdAlException(int p_alErrorCode) {
		System.err.println(ALC11.alcGetString(0, ALC11.alcGetError(p_alErrorCode)));
	}
}
