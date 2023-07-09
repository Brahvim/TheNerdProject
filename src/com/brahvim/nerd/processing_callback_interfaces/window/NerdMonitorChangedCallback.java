package com.brahvim.nerd.processing_callback_interfaces.window;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMonitorChangedCallback extends NerdSketchHardwareCallback {

	public void monitorChanged();

}
