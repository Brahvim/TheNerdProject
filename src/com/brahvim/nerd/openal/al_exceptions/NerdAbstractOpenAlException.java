package com.brahvim.nerd.openal.al_exceptions;

public abstract class NerdAbstractOpenAlException extends RuntimeException {

	public final static String ERR_CODE_MIDFIX = " - Error Code: `";
	public final static String ERR_CODE_POSTFIX = "`.";

	protected final int ERROR_CODE;
	protected final String ERROR_STRING;

	public NerdAbstractOpenAlException(String p_message, int p_alErrorCode) {
		super(p_message
				+ NerdAbstractOpenAlException.ERR_CODE_MIDFIX
				+ p_alErrorCode
				+ NerdAbstractOpenAlException.ERR_CODE_POSTFIX);

		this.ERROR_STRING = p_message;
		this.ERROR_CODE = p_alErrorCode;
	}

	// region Methods.
	public int getAlErrorCode() {
		return this.ERROR_CODE;
	}

	public String getAlErrorString() {
		return this.ERROR_STRING;
	}
	// endregion

}