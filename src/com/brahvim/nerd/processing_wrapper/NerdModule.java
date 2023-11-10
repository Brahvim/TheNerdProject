package com.brahvim.nerd.processing_wrapper;

import java.util.List;
import java.util.Map;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareListener;
import com.brahvim.nerd.processing_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import com.brahvim.nerd.utils.NerdReflectionUtils;

public abstract class NerdModule implements NerdSketchAllWorkflowsListener, NerdSketchHardwareListener {

	protected final NerdSketch SKETCH;

	@SuppressWarnings("unused")
	private NerdModule() {
		this.SKETCH = null;
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	protected NerdModule(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	protected List<NerdModule> getSketchModules() {
		return this.SKETCH.MODULES;
	}

	protected Map<Class<? extends NerdModule>, NerdModule> getSketchModulesMap() {
		return this.SKETCH.MODULES_TO_CLASSES_MAP;
	}

}
