package com.brahvim.nerd.openal;

import java.util.ArrayList;

import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlContext extends AlNativeResource {

	public static class AlContextSettings {

		// OpenAL default values:
		public int frequency = 44100, monoSources = 32, stereoSources = 8, refresh = 40;
		public boolean sync; // `false` by default for OpenAL.

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
	protected static final ArrayList<AlContext> ALL_INSTANCES = new ArrayList<>();

	private final NerdAl alMan;
	private final AlDevice device;
	private final long id, deviceId;
	private final ArrayList<AlBuffer<?>> buffers;
	// endregion

	// region Constructors.
	public AlContext(final NerdAl p_manager) {
		this(p_manager, new AlContext.AlContextSettings());
	}

	public AlContext(final NerdAl p_manager, final AlContext.AlContextSettings p_settings) {
		this.alMan = p_manager;
		this.buffers = new ArrayList<>();
		this.device = p_manager.getDevice();
		this.deviceId = this.device.getId();
		this.id = this.createCtx(p_settings);

		AlContext.ALL_INSTANCES.add(this);
	}
	// endregion

	// region Instance collection queries.
	public static int getNumInstances() {
		return AlContext.ALL_INSTANCES.size();
	}

	public static ArrayList<AlContext> getAllInstances() {
		return new ArrayList<>(AlContext.ALL_INSTANCES);
	}
	// endregion

	// region Getters.
	public long getId() {
		return this.id;
	}

	public NerdAl getAlMan() {
		return this.alMan;
	}

	public AlDevice getDevice() {
		return this.device;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AlBuffer<?>> getBuffers() {
		return (ArrayList<AlBuffer<?>>) this.buffers.clone();
	}
	// endregion

	private long createCtx(AlContext.AlContextSettings p_settings) {
		if (p_settings == null) {
			// System.err.println(
			// "`AlContext(NerdAl, AlContextSettings)` received a `null` settings object.");
			p_settings = new AlContextSettings();
		}

		final long toRet = ALC11.alcCreateContext(this.deviceId, p_settings.asAttribArray());
		this.alMan.checkAlcError();

		// Placing the check into a boolean to check for errors right away!
		final boolean ctxVerifStatus = ALC11.alcMakeContextCurrent(toRet);
		this.alMan.checkAlcError();

		if (toRet == 0 || !ctxVerifStatus)
			super.dispose();

		return toRet;
	}

	@Override
	protected void disposeImpl() {
		// Unlink the current context object:
		if (!ALC11.alcMakeContextCurrent(0))
			throw new NerdAlException(
					"Could not change the OpenAL context (whilst disposing one)!");

		this.alMan.checkAlcError();

		// *Actually* destroy the context object:
		ALC11.alcDestroyContext(this.id);

		this.alMan.checkAlcError();
		AlContext.ALL_INSTANCES.remove(this);
	}

}
