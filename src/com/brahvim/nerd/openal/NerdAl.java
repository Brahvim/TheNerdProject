package com.brahvim.nerd.openal;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTDisconnect;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_exceptions.NerdAlException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlcException;

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

	// region Constructors.
	public NerdAl() {
		this(NerdAl.getDefaultDeviceName());
	}

	public NerdAl(String p_deviceName) {
		this.createAl(p_deviceName);
	}
	// endregion

	// region `static` methods.
	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	}

	public static List<String> getDevices() {
		return ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
	}
	// endregion

	// region [DEPRECATED] Locks.
	// public void setDeviceLock(boolean p_lockStatus) {
	// this.dvLock = p_lockStatus;
	// }

	// public void setContextLock(boolean p_lockStatus) {
	// this.ctxLock = p_lockStatus;
	// }
	// endregion

	// region Getters.
	public long getDeviceId() {
		// while (!this.dvLock);
		return this.dvId;
	}

	public String getDeviceName() {
		return this.dvName;
	}

	public long getContextId() {
		// while (!this.ctxLock);
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

	public int checkAlError() {
		int alError = AL11.alGetError();
		if (alError != 0)
			throw new NerdAlException(alError);
	}

	public int[] checkAlcErrors() throws NerdAlException {
		int alcError = ALC11.alcGetError(this.dvId);
		if (alcError != 0)
			throw new NerdAlcException(alcError);

		return new int[] { alError, alcError };

		// Returning a `Map.Entry<Integer, Integer>` because there is no "pair" utility
		// class, :joy:!
		/*
		 * return new Map.Entry<Integer, Integer>() {
		 * 
		 * @Override
		 * public Integer getKey() {
		 * return alError;
		 * }
		 * 
		 * @Override
		 * public Integer getValue() {
		 * return alcError;
		 * }
		 * 
		 * @Override
		 * public Integer setValue(Integer p_value) {
		 * }
		 * };
		 */

	}

	// region Framely callbacks.
	public void framelyCallback() {
		this.deviceDisconnectionCheck();
	}

	// Should probably *not* have a 'default' version for this.
	public boolean deviceDisconnectionCheck() {
		boolean connected = this.isDeviceConnected();

		if (!connected) {
			System.out.println("Device disconnected!");
			this.createAl(this.disconnectionCallback.apply(null));
		}

		return connected;
	}
	// endregion

	public void dispose() {
		ALC11.alcMakeContextCurrent(0);

		ALC11.alcDestroyContext(this.ctxId);
		this.checkAlcErrors();
		this.ctxId = 0;

		// TODO: Cleanup for the buffers!

		ALC11.alcCloseDevice(this.dvId);
		this.checkAlcErrors();
		this.dvId = 0;
	}

	// region `is()`.
	// This uses device handles and not device names. Thus, no `static` version.
	public boolean isDeviceConnected() {
		// No idea why this bad stack read works.
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);
		ALC11.alcGetIntegerv(this.dvId, EXTDisconnect.ALC_CONNECTED, buffer);
		MemoryStack.stackPop();

		return buffer.get() == 1;
	}

	public boolean isUsingDefaultDevice() {
		return this.isDvDefault;
	}
	// endregion

	// region `private` methods.
	private void createAl(String p_deviceName) {
		this.dvName = p_deviceName;
		this.isDvDefault = p_deviceName.equals(NerdAl.getDefaultDeviceName());

		// TODO: Copy over the previous context's info to the new device!

		this.dvId = ALC11.alcOpenDevice(this.dvName);
		this.ctxId = ALC11.alcCreateContext(this.dvId, new int[] { 0 });
		this.verifyContext();

		this.alCtxCap = ALC.createCapabilities(this.dvId);
		this.alCap = AL.createCapabilities(this.alCtxCap);

		// Throws no exception!
		// if (!ALC11.alcIsExtensionPresent(this.dvId, "ALC_EXT_disconnect"))
		// throw new NerdAlException(0);

		this.checkAlcErrors();
	}

	private void verifyContext() {
		if (this.ctxId == 0 || !ALC11.alcMakeContextCurrent(this.ctxId))
			this.dispose();

		if (ALC11.alcIsExtensionPresent(this.dvId, "AL_EXT_FLOAT32"))
			throw new RuntimeException("`ALC_EXT_FLOAT32` not found...");
	}
	// endregion

}
