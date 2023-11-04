package com.brahvim.nerd.processing_callback_interfaces.workflow.regular;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchCallback;

public interface NerdSketchWorkflowsListener extends NerdSketchCallback {

	public default void settings() {
	}

	public default void setup() {
	}

	public default void draw() {
	}

	public default void exit() {
	}

	public default void dispose() {
	}

}
