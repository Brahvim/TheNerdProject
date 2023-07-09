package com.brahvim.nerd.processing_callback_interfaces.window;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdFocusGainedCallback extends NerdSketchHardwareCallback {

	public void focusGained();

}
