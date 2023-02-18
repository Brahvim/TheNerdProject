package com.brahvim.nerd.openal;

import java.util.List;
import java.util.function.Function;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;

public class NerdAl {

	// region Fields.
	public final static Function<Void, String> DEFAULT_DISCONNECTION_CALLBACK = (Void a) -> {
		return NerdAl.getDefaultDeviceName();
	};

	private String dvName;
	private long dvId, ctxId;
	private ALCapabilities alCap;
	private ALCCapabilities alCtxCap;
	private boolean isDvDefault = true;
	private Function<Void, String> disconnectionCallback = NerdAl.DEFAULT_DISCONNECTION_CALLBACK;
	// endregion

	public NerdAl() {
		this(NerdAl.getDefaultDeviceName());
	}

	public NerdAl(String p_deviceName) {
		this.createAl(p_deviceName);
	}

	public static boolean isDeviceConnected(String p_deviceName) {
		final List<String> DEVICES = NerdAl.getDevices();
		return DEVICES.contains(p_deviceName);
	}

	// region Getters.
	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	}

	public static List<String> getDevices() {
		return ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
	}

	public long getDeviceId() {
		return this.dvId;
	}

	public String getDeviceName() {
		return this.dvName;
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

	// region Setters.
	// region Event callback setters.
	public void setDisconnectionCallback(Function<Void, String> p_callback) {
		this.disconnectionCallback = p_callback;
	}
	// endregion
	// endregion

	public int checkForErrors() throws NerdAlException {
		int error = ALC11.alcGetError(this.dvId);
		if (error != 0)
			throw new NerdAlException(error);
		return error;
	}

	private void createAl(String p_deviceName) {
		this.isDvDefault = p_deviceName.equals(NerdAl.getDefaultDeviceName());

		// TODO: Copy over the previous context's info to the new device!

		this.dvId = ALC11.alcOpenDevice(p_deviceName);
		this.ctxId = ALC11.alcCreateContext(this.dvId, new int[] { 0 });
		this.verifyContext();

		this.alCtxCap = ALC.createCapabilities(this.dvId);
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkForErrors();
	}

	// Should probably *not* have a 'default' version for this.
	public boolean deviceDisconnectionCheck() {
		String deviceName = this.disconnectionCallback.apply(null);
		boolean canUseNewDevice = NerdAl.isDeviceConnected(deviceName);

		if (canUseNewDevice)
			this.createAl(deviceName);

		return canUseNewDevice;
	}

	public void framelyCallback() {
		this.deviceDisconnectionCheck();
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

	public boolean isUsingDefaultDevice() {
		return this.isDvDefault;
	}

	private void verifyContext() {
		if (this.ctxId == 0 || !ALC11.alcMakeContextCurrent(this.ctxId))
			this.dispose();

		if (ALC11.alcIsExtensionPresent(this.dvId, "AL_EXT_FLOAT32"))
			throw new RuntimeException("`ALC_EXT_FLOAT32` not found...");
	}

}
