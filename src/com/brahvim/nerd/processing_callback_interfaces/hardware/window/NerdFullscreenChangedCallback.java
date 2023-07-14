package com.brahvim.nerd.processing_callback_interfaces.hardware.window;

import com.brahvim.nerd.processing_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdFullscreenChangedCallback extends NerdSketchHardwareCallback {

	void fullscreenChanged(final boolean p_state);

}