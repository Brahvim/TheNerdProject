package com.brahvim.nerd.openal;

public abstract class AlNativeResource {
	protected boolean hasDisposed;

	protected AlNativeResource() {
	}

	// Yes, there is no `getId()` method here...

	public final boolean isDisposed() {
		return this.hasDisposed;
	}

	public void dispose() {
		this.hasDisposed = true;
		this.disposeImpl();
	}

	protected abstract void disposeImpl();

}
