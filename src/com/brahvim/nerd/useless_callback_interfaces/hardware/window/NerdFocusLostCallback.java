package com.brahvim.nerd.useless_callback_interfaces.hardware.window;

import com.brahvim.nerd.useless_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdFocusLostCallback extends NerdSketchHardwareCallback {

	void focusLost();

}
