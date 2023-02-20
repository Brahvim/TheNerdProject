package com.brahvim.nerd.openal;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_exceptions.NerdAlcException;

public class NerdAlContext {
	private long id;
	private NerdAl manager;
	private final long deviceId;

	public NerdAlContext(NerdAl p_manager) {
		this.manager = p_manager;
		this.deviceId = p_manager.getDeviceId();
	}

	public void dispose() {
	}

	public int checkAlcErrors() throws NerdAlcException {
		int alcError = ALC11.alcGetError(this.deviceId);
		if (alcError != 0)
			throw new NerdAlcException(alcError);

		return alcError;
	}

	private void verifyContext() {
		if (this.ctxId == 0 || !ALC11.alcMakeContextCurrent(this.ctxId))
			this.dispose();

		if (ALC11.alcIsExtensionPresent(this.dvId, "AL_EXT_FLOAT32"))
			throw new RuntimeException("`ALC_EXT_FLOAT32` not found...");
	}
}
