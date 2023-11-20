package com.brahvim.nerd.useless_callback_interfaces.hardware;

import com.brahvim.nerd.useless_callback_interfaces.hardware.keyboard.NerdKeyboardListener;
import com.brahvim.nerd.useless_callback_interfaces.hardware.mouse.NerdMouseListener;
import com.brahvim.nerd.useless_callback_interfaces.hardware.touch.NerdTouchListener;
import com.brahvim.nerd.useless_callback_interfaces.hardware.window.NerdWindowListener;

public interface NerdSketchHardwareListener
		extends NerdWindowListener, NerdMouseListener, NerdKeyboardListener, NerdTouchListener {

}
