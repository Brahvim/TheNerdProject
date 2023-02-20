package com.brahvim.nerd.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import com.brahvim.nerd.openal.al_exceptions.NerdAlException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlcException;

public class NerdAl {

	// region Fields.
	private NerdAlDevice device;
	private ALCapabilities alCap;
	private NerdAlContext context;
	private ALCCapabilities alCtxCap;
	// endregion

	// region Constructors.
	public NerdAl() {
		this(NerdAlDevice.getDefaultDeviceName());
	}

	public NerdAl(String p_deviceName) {
		this.createAl(p_deviceName);
	}

	public NerdAl(NerdAlContext p_ctx) {
		this.createAl(p_ctx);
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
		return this.device.getId();
	}

	public String getDeviceName() {
		return this.device.getName();
	}

	public long getContextId() {
		return this.context.getId();
	}

	public ALCapabilities getCapabilities() {
		return this.alCap;
	}

	public ALCCapabilities getContextCapabilities() {
		return this.alCtxCap;
	}
	// endregion

	// region Setters.
	public void changeDevice(NerdAlDevice p_dv, NerdAlContext p_ctx) {
		this.device = p_dv;
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) != p_dv.getId())
			throw new NerdAlException(ALC11.ALC_INVALID_CONTEXT);
		this.context = p_ctx;
	}

	public void setContext(NerdAlContext p_ctx) {
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) != this.device.getId())
			throw new NerdAlException(ALC11.ALC_INVALID_CONTEXT);
		this.context = p_ctx;
	}
	// endregion

	public int checkAlErrors() throws NerdAlException {
		int alError = AL11.alGetError();
		if (alError != 0)
			throw new NerdAlException(alError);

		return alError;
	}

	public int checkAlcErrors() throws NerdAlcException {
		int alcError = ALC11.alcGetError(this.device.getId());
		if (alcError != 0)
			throw new NerdAlcException(alcError);

		return alcError;
	}

	public void framelyCallback() {
		this.device.disconnectionCheck();
	}

	public void dispose() {
		this.context.dispose();
		this.device.dispose();
	}

	// region `private` and `protected` methods.
	protected void createAl(String p_deviceName) {
		this.device = new NerdAlDevice(this);
		this.checkAlcErrors();

		this.context = new NerdAlContext(this);
		this.checkAlcErrors();

		// LWJGL objects:
		this.alCtxCap = ALC.createCapabilities(this.device.getId());
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkAlErrors();
		this.checkAlcErrors();
	}

	// Copies the previous context's buffers and reuses them here.
	protected void createAl(NerdAlContext p_ctx) {
		this.device = new NerdAlDevice(this);
		this.checkAlcErrors();

		this.context = new NerdAlContext(p_ctx);
		this.checkAlcErrors();

		// LWJGL objects:
		this.alCtxCap = ALC.createCapabilities(this.device.getId());
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkAlErrors();
		this.checkAlcErrors();
	}
	// endregion

}
