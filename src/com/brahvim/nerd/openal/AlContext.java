package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlcException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlContext extends AlNativeResource {

	// region Fields.
	private long id;
	private NerdAl alMan;
	private final long deviceId;
	private ArrayList<AlBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public AlContext(NerdAl p_manager) {
		this.alMan = p_manager;
		this.buffers = new ArrayList<>();
		this.deviceId = p_manager.getDeviceId();

		this.createCtx();
	}

	public AlContext(AlContext p_ctx) {
		this.alMan = p_ctx.alMan;
		this.buffers = p_ctx.getBuffers();

		// Get the newer device's ID / handle:
		this.deviceId = p_ctx.deviceId;
		this.createCtx();
	}
	// endregion

	public int checkAlcErrors() throws AlcException {
		int alcError = ALC11.alcGetError(this.deviceId);
		if (alcError != 0)
			throw new AlcException(this.deviceId, alcError);

		return alcError;
	}

	public void dispose() {
		// Unlink the current context object:
		if (!ALC11.alcMakeContextCurrent(0))
			throw new NerdAlException("Could not change the OpenAL context!");

		this.alMan.checkAlErrors();
		this.checkAlcErrors();

		// *Actually* destroy the context object:
		ALC11.alcDestroyContext(this.id);

		this.alMan.checkAlErrors();
		this.checkAlcErrors();
		this.id = 0;
	}

	// region Getters.
	public long getId() {
		return this.id;
	}

	public NerdAl getAlMan() {
		return this.alMan;
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
			this.checkAlcErrors();
		}

	}

}
