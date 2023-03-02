package com.brahvim.nerd.openal;

public abstract class AlResourceHolder {
	protected boolean hasDisposed;
	private AlNativeResource resource;

	protected AlResourceHolder() {
		// When the GC determines that this object can be collected, do this!:
		this.resource = new AlNativeResource(this, () -> {
			this.dispose(); // This method is for Nerd's OpenAL classes.
			this.hasDisposed = true;
		});
	}

	public final boolean isDisposed() {
		return this.hasDisposed;
	}

	public abstract void dispose();

	// Is this unsafe?
	protected AlNativeResource getNativeResource() {
		return this.resource;
	}

}
