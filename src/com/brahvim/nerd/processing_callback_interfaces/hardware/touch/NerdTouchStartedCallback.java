package com.brahvim.nerd.processing_callback_interfaces.hardware.touch;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdTouchStartedCallback extends NerdSketchHardwareCallback {

	void touchStarted();

}
