package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareListener;

public abstract class NerdModule extends NerdSketchProtectedWorkflowListener
		implements NerdSketchHardwareListener {

	protected final NerdSketch SKETCH;

	// Only for classes that are serializable (for now, just `NerdEcsModule`).
	// Will go away soon!
	protected NerdModule() {
		this.SKETCH = null;
	}

	protected NerdModule(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

}
