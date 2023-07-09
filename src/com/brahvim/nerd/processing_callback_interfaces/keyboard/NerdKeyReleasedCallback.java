package com.brahvim.nerd.processing_callback_interfaces.keyboard;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdKeyReleasedCallback extends NerdSketchHardwareCallback {

	public void keyReleased();

}
