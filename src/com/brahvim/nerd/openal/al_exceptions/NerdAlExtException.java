package com.brahvim.nerd.openal.al_exceptions;

public class NerdAlExtException extends RuntimeException {

	private static final long serialVersionUID = -8468435964L;

	public NerdAlExtException(String p_extName, String p_problem) {
		super("Issue with extension " + p_extName + " : " + p_problem);
	}

	public NerdAlExtException(String p_extName) {
		super("Issue with extension " + p_extName + ". Does it exist..?");
	}

}
