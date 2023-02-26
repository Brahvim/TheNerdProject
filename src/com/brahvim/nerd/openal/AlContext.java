package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlExtException;
import com.brahvim.nerd.openal.al_exceptions.AlcException;

public class AlContext {

	// region Fields.
	private long id;
	private NerdAl manager;
	private final long deviceId;
	private ArrayList<AlBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public AlContext(NerdAl p_manager) {
		this.manager = p_manager;
		this.buffers = new ArrayList<>();
		this.deviceId = p_manager.getDeviceId();

		this.createCtx();
	}

	public AlContext(AlContext p_ctx) {
		this.manager = p_ctx.manager;
		this.buffers = p_ctx.getBuffers();

		// Get the newer device's ID / handle:
		this.deviceId = p_ctx.deviceId;
		this.createCtx();
	}
	// endregion

	public int checkAlcErrors() throws AlcException {
		int alcError = ALC11.alcGetError(this.deviceId);
		if (alcError != 0)
			throw new AlcException(alcError);

		return alcError;
	}

	/* `package` */ void dispose() {
		// Unlink the current context object:
		if (!ALC11.alcMakeContextCurrent(0))
			throw new RuntimeException("Could not change the OpenAL context!");

		this.manager.checkAlErrors();
		this.checkAlcErrors();

		// *Actually* destroy the context object:
		ALC11.alcDestroyContext(this.id);

		this.manager.checkAlErrors();
		this.checkAlcErrors();
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
	public ArrayList<AlBuffer<?>> getBuffers() {
		return (ArrayList<AlBuffer<?>>) this.buffers.clone();
	}
	// endregion

	private void createCtx() {
		this.id = ALC11.alcCreateContext(this.deviceId, new int[] { 0 });

		if (this.id == 0 || !ALC11.alcMakeContextCurrent(this.id)) {
			this.dispose();
			throw new RuntimeException();
		}

		if (!ALC11.alcIsExtensionPresent(this.deviceId, "ALC_EXT_disconnect"))
			throw new AlExtException("ALC_EXT_disconnect");

	}
}
