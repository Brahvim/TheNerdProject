package com.brahvim.nerd.useless_callback_interfaces.hardware.window;

import com.brahvim.nerd.useless_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdResizedCallback extends NerdSketchHardwareCallback {

	void resized();

}
