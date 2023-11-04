package com.brahvim.nerd.processing_callback_interfaces.hardware.mouse;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMousePressedCallback extends NerdSketchHardwareCallback {

	void mousePressed();

}
