package com.brahvim.nerd.openal.al_exceptions;

public abstract class NerdAbstractOpenAlException extends RuntimeException {
	protected int errCode;

	public NerdAbstractOpenAlException(String p_message) {
		super(p_message);
	}
}