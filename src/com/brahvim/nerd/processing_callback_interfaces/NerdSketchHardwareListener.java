package com.brahvim.nerd.processing_callback_interfaces;

import com.brahvim.nerd.processing_callback_interfaces.keyboard.NerdKeyboardListener;
import com.brahvim.nerd.processing_callback_interfaces.mouse.NerdMouseListener;
import com.brahvim.nerd.processing_callback_interfaces.touch.NerdTouchListener;
import com.brahvim.nerd.processing_callback_interfaces.window.NerdWindowListener;

public interface NerdSketchHardwareListener
		extends NerdWindowListener, NerdMouseListener, NerdKeyboardListener, NerdTouchListener {

}
