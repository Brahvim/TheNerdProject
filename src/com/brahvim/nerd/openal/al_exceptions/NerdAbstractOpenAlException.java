package com.brahvim.nerd.openal.al_exceptions;

public abstract class NerdAbstractOpenAlException extends RuntimeException {

	protected final int ERROR_CODE;
	protected final String ERROR_STRING;

	public NerdAbstractOpenAlException(final String p_message, final int p_alErrorCode) {
		super("\"" + p_message + "\""
				+ " - Error Code: `"
				+ p_alErrorCode
				+ "`.");

		this.ERROR_STRING = p_message;
		this.ERROR_CODE = p_alErrorCode;
	}

	// region Methods.
	public int getAlcErrorCode() {
		return this.ERROR_CODE;
	}

	public String getAlcErrorString() {
		return this.ERROR_STRING;
	}
	// endregion

}