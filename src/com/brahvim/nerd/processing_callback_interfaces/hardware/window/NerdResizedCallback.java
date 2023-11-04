package com.brahvim.nerd.processing_callback_interfaces.hardware.window;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdResizedCallback extends NerdSketchHardwareCallback {

	void resized();

}
