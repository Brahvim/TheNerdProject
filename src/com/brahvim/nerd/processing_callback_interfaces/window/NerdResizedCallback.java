package com.brahvim.nerd.processing_callback_interfaces.window;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdResizedCallback extends NerdSketchHardwareCallback {

	public void resized();

}
