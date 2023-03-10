package com.brahvim.nerd.openal.al_exceptions;

public class AlExtAbsentException extends RuntimeException {

	private final static long serialVersionUID = -8468435964L;

	public AlExtAbsentException(String p_extName, String p_problem) {
		super("Issue with extension " + p_extName + " : " + p_problem);
	}

	public AlExtAbsentException(String p_extName) {
		super("OpenAL Extension \"" + p_extName + "\" does not exist!");
	}

}
