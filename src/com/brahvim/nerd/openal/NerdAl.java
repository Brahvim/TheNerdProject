package com.brahvim.nerd.openal;

import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;

public class NerdAl {

	// region Fields.
	// region Constants.
	// public static final int NULL_DEVICE = 0;

	// Nope! Not a thing that I can do!:
	// public final static int DEFAULT_OPENAL_DEVICE_ID = ALC11.alcGetInteger(0,
	// ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	public final static String DEFAULT_OPENAL_DEVICE_NAME = ALC11
			.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	// endregion

	private long dvId, ctxId;
	private ALCapabilities alCap;
	private ALCCapabilities alCtxCap;
	// endregion

	public NerdAl() {
		this.dvId = ALC11.alcOpenDevice(NerdAl.DEFAULT_OPENAL_DEVICE_NAME);
		this.ctxId = ALC11.alcCreateContext(this.dvId, new int[] { 0 });
		this.verifyContext();

		this.alCtxCap = ALC.createCapabilities(this.dvId);
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkForErrors();
	}

	// region Getters.
	public static List<String> getOpenAlDevices() {
		return ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
	}

	public long getDeviceId() {
		return this.dvId;
	}

	public long getContextId() {
		return this.ctxId;
	}

	public ALCapabilities getCapabilities() {
		return this.alCap;
	}

	public ALCCapabilities getContextCapabilities() {
		return this.alCtxCap;
	}
	// endregion

	public int checkForErrors() throws NerdAlException {
		int error = ALC11.alcGetError(this.dvId);
		if (error != 0)
			throw new NerdAlException(error);
		return error;
	}

	public void dispose() {
		ALC11.alcMakeContextCurrent(0);

		ALC11.alcDestroyContext(this.ctxId);
		this.checkForErrors();
		this.ctxId = 0;

		// TODO: Cleanup for the buffers!

		ALC11.alcCloseDevice(this.dvId);
		this.checkForErrors();
		this.dvId = 0;
	}

	public void framelyCall() {
	}

	private void verifyContext() {
		if (this.ctxId == 0 || !ALC11.alcMakeContextCurrent(this.ctxId))
			this.dispose();

		if (ALC11.alcIsExtensionPresent(this.dvId, "AL_EXT_FLOAT32"))
			throw new RuntimeException("`ALC_EXT_FLOAT32` not found...");
	}

}
