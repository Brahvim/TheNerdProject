package com.brahvim.nerd.processing_callback_interfaces.hardware.keyboard;

public interface NerdKeyboardListener
		extends NerdKeyTypedCallback, NerdKeyPressedCallback, NerdKeyReleasedCallback {

	@Override
	public default void keyTyped() {
	}

	@Override
	public default void keyPressed() {
	}

	@Override
	public default void keyReleased() {
	}

}
