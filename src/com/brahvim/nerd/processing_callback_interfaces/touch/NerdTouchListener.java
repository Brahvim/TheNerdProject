package com.brahvim.nerd.processing_callback_interfaces.touch;

public interface NerdTouchListener
		extends NerdTouchStartedCallback, NerdTouchMovedCallback, NerdTouchEndedCallback {

	@Override
	public default void touchStarted() {
	}

	@Override
	public default void touchMoved() {
	}

	@Override
	public default void touchEnded() {
	}

}
