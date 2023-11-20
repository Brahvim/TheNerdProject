package com.brahvim.nerd.useless_callback_interfaces.hardware.keyboard;

import com.brahvim.nerd.useless_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdKeyReleasedCallback extends NerdSketchHardwareCallback {

	void keyReleased();

}
