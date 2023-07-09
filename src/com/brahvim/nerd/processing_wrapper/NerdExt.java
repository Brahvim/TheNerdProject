package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareListener;

public abstract class NerdExt extends NerdSketchProtectedWorkflowListener
		implements NerdSketchHardwareListener {

	protected NerdExt() {
	}

	protected abstract Object init(final NerdCustomSketchBuilder p_builder);

}
