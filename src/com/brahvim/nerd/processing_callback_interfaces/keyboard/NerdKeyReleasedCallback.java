package com.brahvim.nerd.processing_callback_interfaces.keyboard;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

public interface NerdKeyReleasedCallback extends NerdSketchHardwareCallback {

	public default void keyReleased() {
	}

}
