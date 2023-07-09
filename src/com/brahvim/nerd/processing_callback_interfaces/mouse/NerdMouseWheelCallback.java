package com.brahvim.nerd.processing_callback_interfaces.mouse;

import com.brahvim.nerd.processing_callback_interfaces.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMouseWheelCallback extends NerdSketchHardwareCallback {

	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent);

}
