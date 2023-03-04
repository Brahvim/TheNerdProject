package com.brahvim.nerd.openal;

public abstract class AlNativeResource {
	protected boolean hasDisposed, willDispose = true;

	protected AlNativeResource() {
	}

	// Yes, there is no `getId()` method here...

	public final boolean shouldDispose(boolean p_value) {
		boolean toRet = this.willDispose;
		this.willDispose = p_value;
		return toRet;
	}

	public final boolean isDisposed() {
		return this.hasDisposed;
	}

	public final void dispose() {
		if (!this.willDispose)
			return;

		// They're the same, so-uhh,
		this.disposeForcibly();
	}

	public final void disposeForcibly() {
		if (this.hasDisposed)
			return;

		this.hasDisposed = true;
		this.disposeImpl();
	}

	protected abstract void disposeImpl();

}
