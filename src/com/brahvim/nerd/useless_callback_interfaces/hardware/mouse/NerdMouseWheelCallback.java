package com.brahvim.nerd.useless_callback_interfaces.hardware.mouse;

import com.brahvim.nerd.useless_callback_interfaces.hardware.NerdSketchHardwareCallback;

@FunctionalInterface
public interface NerdMouseWheelCallback extends NerdSketchHardwareCallback {

	void mouseWheel(final processing.event.MouseEvent p_mouseEvent);

}
