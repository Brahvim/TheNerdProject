package com.brahvim.nerd.openal.al_exceptions;

public abstract class NerdAbstractOpenAlException extends RuntimeException {

	public final static String ERR_CODE_MIDFIX = " - Error Code: `";
	public final static String ERR_CODE_POSTFIX = "`.";
	protected final int ERROR_CODE;

	public NerdAbstractOpenAlException(String p_message, int p_alErrorCode) {
		super(p_message
				+ NerdAbstractOpenAlException.ERR_CODE_MIDFIX
				+ p_alErrorCode
				+ NerdAbstractOpenAlException.ERR_CODE_POSTFIX);
		this.ERROR_CODE = p_alErrorCode;
	}

	public int getAlErrorCode() {
		return this.ERROR_CODE;
	}

}