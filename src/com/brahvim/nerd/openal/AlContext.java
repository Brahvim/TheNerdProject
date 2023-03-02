package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlContext extends AlNativeResource {

	/* `package` */ static class AlContextSettings {

		public int frequency = 44100, monoSources = 32, stereoSources = 8, refresh = 40;
		public boolean sync;

		public int[] asAttribArray() {
			return new int[] {
					ALC11.ALC_FREQUENCY, this.frequency,
					ALC11.ALC_MONO_SOURCES, this.monoSources,
					ALC11.ALC_STEREO_SOURCES, this.stereoSources,
					ALC11.ALC_REFRESH, this.refresh,
					ALC11.ALC_SYNC, this.sync ? ALC11.ALC_TRUE : ALC11.ALC_FALSE
			};
		}
	}

	// region Fields.
	public final static ArrayList<AlContext> ALL_INSTANCES = new ArrayList<>();

	private long id;
	private NerdAl alMan;
	private final long deviceId;
	private final AlDevice device;
	private ArrayList<AlBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public AlContext(NerdAl p_manager) {
		this(p_manager, new AlContextSettings());
	}

	public AlContext(NerdAl p_manager, AlContextSettings p_settings) {
		this.alMan = p_manager;
		this.buffers = new ArrayList<>();
		this.device = p_manager.getDevice();
		this.deviceId = this.device.getId();

		this.createCtx(p_settings.asAttribArray());
	}
	// endregion

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

	private void createCtx(int[] p_attributes) {
		this.id = ALC11.alcCreateContext(this.deviceId, p_attributes);

		if (this.id == 0 || !ALC11.alcMakeContextCurrent(this.id)) {
			this.disposeImpl();
			this.alMan.checkAlcError();
		}
	}

	@Override
	protected void disposeImpl() {
		// Unlink the current context object:
		if (!ALC11.alcMakeContextCurrent(0))
			throw new NerdAlException("Could not change the OpenAL context (whilst disposing)!");

		this.alMan.checkAlcError();

		// *Actually* destroy the context object:
		ALC11.alcDestroyContext(this.id);

		this.id = 0;
		this.alMan.checkAlcError();
		AlContext.ALL_INSTANCES.remove(this);
	}

}
