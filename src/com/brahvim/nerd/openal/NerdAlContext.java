package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.NerdAlTypedBuffer;
import com.brahvim.nerd.openal.al_exceptions.NerdAlExtException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlcException;

public class NerdAlContext {

	// region Fields.
	private long id;
	private NerdAl manager;
	private final long deviceId;
	private ArrayList<NerdAlTypedBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public NerdAlContext(NerdAl p_manager) {
		this.manager = p_manager;
		this.buffers = new ArrayList<>();
		this.deviceId = p_manager.getDeviceId();

		this.createCtx();
	}

	public NerdAlContext(NerdAlContext p_ctx) {
		this.manager = p_ctx.manager;
		this.buffers = p_ctx.getBuffers();

		// Get the newer device's ID / handle:
		this.deviceId = this.manager.getDeviceId();

		this.createCtx();
	}
	// endregion

	public int checkForErrors() throws NerdAlcException {
		int alcError = ALC11.alcGetError(this.deviceId);
		if (alcError != 0)
			throw new NerdAlcException(alcError);

		return alcError;
	}

	public void dispose() {
		ALC11.alcMakeContextCurrent(0);

		ALC11.alcDestroyContext(this.id);
		this.checkForErrors();
		this.id = 0;
	}

	// region Getters.
	public long getId() {
		return this.id;
	}

	public NerdAl getManager() {
		return this.manager;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<NerdAlTypedBuffer<?>> getBuffers() {
		return (ArrayList<NerdAlTypedBuffer<?>>) this.buffers.clone();
	}
	// endregion

	private void createCtx() {
		this.id = ALC11.alcCreateContext(this.deviceId, new int[] { 0 });

		if (this.id == 0 || !ALC11.alcMakeContextCurrent(this.id)) {
			this.dispose();
			throw new RuntimeException();
		}

		if (!ALC11.alcIsExtensionPresent(this.deviceId, "ALC_EXT_disconnect"))
			throw new NerdAlExtException("ALC_EXT_disconnect");
	}

}
