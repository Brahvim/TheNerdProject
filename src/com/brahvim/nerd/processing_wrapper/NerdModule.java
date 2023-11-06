package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareListener;
import com.brahvim.nerd.processing_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import com.brahvim.nerd.utils.NerdReflectionUtils;

public abstract class NerdModule implements NerdSketchAllWorkflowsListener, NerdSketchHardwareListener {

	private NerdModule() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

}
