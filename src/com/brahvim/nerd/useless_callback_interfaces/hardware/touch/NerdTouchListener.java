package com.brahvim.nerd.useless_callback_interfaces.hardware.touch;

public interface NerdTouchListener extends NerdTouchStartedCallback, NerdTouchMovedCallback, NerdTouchEndedCallback {

	@Override
	default void touchStarted() {
	}

	@Override
	default void touchMoved() {
	}

	@Override
	default void touchEnded() {
	}

}
