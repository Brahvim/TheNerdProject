package com.brahvim.nerd.processing_callback_interfaces.window;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

public interface NerdFullscreenChangedCallback extends NerdSketchHardwareCallback {

	public void fullscreenChanged(final boolean p_state);

}