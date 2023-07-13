package com.brahvim.nerd.processing_callback_interfaces.workflow;

import com.brahvim.nerd.processing_callback_interfaces.workflow.regular.NerdSketchWorkflowsListener;

public interface NerdSketchAllWorkflowsListener extends NerdSketchWorkflowsListener {

	public default void pre() {
	}

	public default void preDraw() {
	}

	public default void postDraw() {
	}

	public default void post() {
	}

}
