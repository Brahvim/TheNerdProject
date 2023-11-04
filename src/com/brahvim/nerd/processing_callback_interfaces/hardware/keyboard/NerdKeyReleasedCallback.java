package com.brahvim.nerd.processing_callback_interfaces.hardware.keyboard;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdKeyReleasedCallback extends NerdSketchHardwareCallback {

	void keyReleased();

}
