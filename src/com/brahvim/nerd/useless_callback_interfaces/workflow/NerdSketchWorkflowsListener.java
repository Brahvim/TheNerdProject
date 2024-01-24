package com.brahvim.nerd.useless_callback_interfaces.workflow;

import com.brahvim.nerd.useless_callback_interfaces.NerdSketchCallback;

public interface NerdSketchWorkflowsListener extends NerdSketchCallback {

	public default void preSetup() {
	}

	public default void postSetup() {
	}

	public default void draw() {
	}

	public default void exit() {
	}

	public default void dispose() {
	}

}
