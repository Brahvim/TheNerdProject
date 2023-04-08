package com.brahvim.nerd.openal.al_exceptions;

public class AlExtAbsentException extends RuntimeException {

	private static final long serialVersionUID = -8468435964L;

	public AlExtAbsentException(final String p_extName, final String p_problem) {
		super("Issue with extension " + p_extName + " : " + p_problem);
	}

	public AlExtAbsentException(final String p_extName) {
		super("OpenAL Extension \"" + p_extName + "\" does not exist!");
	}

}
