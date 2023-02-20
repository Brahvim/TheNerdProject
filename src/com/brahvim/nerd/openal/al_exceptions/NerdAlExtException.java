package com.brahvim.nerd.openal.al_exceptions;

public class NerdAlExtException extends RuntimeException {
	private static final long serialVersionUID = -8468435964L;

	public NerdAlExtException(String p_extName, String p_problem) {
		System.err.printf("Issue with extension `%s`: %s\n", p_extName, p_problem);
	}

	public NerdAlExtException(String p_extName) {
		System.err.printf("Issue with extension `%s`. Does it exist..?", p_extName);
	}
}
