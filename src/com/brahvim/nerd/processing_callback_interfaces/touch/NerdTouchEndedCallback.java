package com.brahvim.nerd.processing_callback_interfaces.touch;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdTouchEndedCallback extends NerdSketchHardwareCallback {

	public void touchEnded();

}
