package com.brahvim.nerd.processing_callback_interfaces.mouse;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMouseReleasedCallback extends NerdSketchHardwareCallback {

	public void mouseReleased();

}
