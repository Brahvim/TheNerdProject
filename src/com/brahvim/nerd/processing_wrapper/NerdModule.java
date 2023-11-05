package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.utils.NerdReflectionUtils;

public class NerdModule {

	private NerdModule() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

}
