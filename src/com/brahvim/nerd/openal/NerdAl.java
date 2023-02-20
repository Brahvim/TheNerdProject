package com.brahvim.nerd.openal;

import java.nio.IntBuffer;
import java.util.List;
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
	private String dvName;
	private long dvId, ctxId;
	private ALCapabilities alCap;
	private ALCCapabilities alCtxCap;
	private boolean isDvDefault = true;
	// endregion

	// region Constructors.
	public NerdAl() {
		this(NerdAlDevice.getDefaultDeviceName());
	}

	public NerdAl(String p_deviceName) {
		this.createAl(p_deviceName);
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

	public int checkAlErrors() throws NerdAlException {
		int alError = AL11.alGetError();
		if (alError != 0)
			throw new NerdAlException(alError);

		return alError;
	}

	public int checkAlcErrors() throws NerdAlcException {
		int alcError = ALC11.alcGetError(this.dvId);
		if (alcError != 0)
			throw new NerdAlcException(alcError);

		return alcError;

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
		this.context.dispose();
		this.device.dispose();
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
		this.isDvDefault = p_deviceName.equals(NerdAlDevice.getDefaultDeviceName());

		// TODO: Copy over the previous context's info to the new device!

		this.dvId = ALC11.alcOpenDevice(this.dvName);
		this.ctxId = ALC11.alcCreateContext(this.dvId, new int[] { 0 });
		this.verifyContext();

		this.alCtxCap = ALC.createCapabilities(this.dvId);
		this.alCap = AL.createCapabilities(this.alCtxCap);

		// Throws no exception!
		// if (!ALC11.alcIsExtensionPresent(this.dvId, "ALC_EXT_disconnect"))
		// throw new NerdAlException(0);

		this.checkAlErrors();
		this.checkAlcErrors();
	}	
	// endregion

}
