package com.brahvim.nerd.processing_wrapper;

public interface NerdModuleCallback<ParameterT, ReturnT> {

	public ReturnT call(ParameterT p_parameters);

}
