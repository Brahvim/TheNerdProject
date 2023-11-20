package com.brahvim.nerd.useless_callback_interfaces.hardware.mouse;

import com.brahvim.nerd.useless_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMouseReleasedCallback extends NerdSketchHardwareCallback {

	void mouseReleased();

}
