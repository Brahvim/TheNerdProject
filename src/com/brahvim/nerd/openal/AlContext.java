package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlcException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlContext extends AlNativeResource {

	// region Fields.
	public final static ArrayList<AlContext> ALL_INSTANCES = new ArrayList<>();

	private long id;
	private NerdAl alMan;
	private final AlDevice device;
	private ArrayList<AlBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public AlContext(NerdAl p_manager) {
		this.alMan = p_manager;
		this.buffers = new ArrayList<>();
		this.device = p_manager.getDevice();

		this.createCtx();
	}

	/**
	 * <h1><b><i>Not</b></i> a copy constructor.</h1>
	 * This constructor creates a new context object inside OpenAL, disposing off
	 * the one that is passed in.
	 */
	public AlContext(AlContext p_ctx) {
		this.alMan = p_ctx.alMan;
		this.buffers = p_ctx.getBuffers();

		// Get the newer device's ID / handle:
		this.device = p_ctx.device;
		this.createCtx();

		p_ctx.dispose();
	}
	// endregion

	public int checkAlcErrors() throws AlcException {
		int alcError = ALC11.alcGetError(this.device);
		if (alcError != 0)
			throw new AlcException(this.device, alcError);

		return alcError;
	}

	@Override
	protected void disposeImpl() {
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

		AlContext.ALL_INSTANCES.remove(this);
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
		this.id = ALC11.alcCreateContext(this.device, new int[] { 0 });

		if (this.id == 0 || !ALC11.alcMakeContextCurrent(this.id)) {
			this.disposeImpl();
			this.checkAlcErrors();
		}

	}

}
