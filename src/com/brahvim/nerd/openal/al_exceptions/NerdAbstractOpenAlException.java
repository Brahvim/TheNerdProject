package com.brahvim.nerd.openal.al_exceptions;

public abstract class NerdAbstractOpenAlException extends RuntimeException {

	protected int errCode = -1;

	public NerdAbstractOpenAlException(String p_message) {
		super(p_message);
	}

	public int getAlErrorCode() {
		return this.errCode;
	}

}