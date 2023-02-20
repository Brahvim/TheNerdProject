package com.brahvim.nerd.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import com.brahvim.nerd.openal.al_exceptions.AlException;
import com.brahvim.nerd.openal.al_exceptions.AlcException;

public class NerdAl {

	// region Fields.
	private AlDevice device;
	private ALCapabilities alCap;
	private AlContext context;
	private ALCCapabilities alCtxCap;
	// endregion

	// region Constructors.
	public NerdAl() {
		this(AlDevice.getDefaultDeviceName());
	}

	public NerdAl(String p_deviceName) {
		this.createAl(p_deviceName);
	}

	public NerdAl(AlContext p_ctx) {
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
	public void changeDevice(AlDevice p_dv, AlContext p_ctx) {
		this.device = p_dv;
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) != p_dv.getId())
			throw new AlException(ALC11.ALC_INVALID_CONTEXT);
		this.context = p_ctx;
	}

	public void setContext(AlContext p_ctx) {
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) != this.device.getId())
			throw new AlException(ALC11.ALC_INVALID_CONTEXT);
		this.context = p_ctx;
	}
	// endregion

	public int checkAlErrors() throws AlException {
		int alError = AL11.alGetError();
		if (alError != 0)
			throw new AlException(alError);

		return alError;
	}

	public int checkAlcErrors() throws AlcException {
		int alcError = ALC11.alcGetError(this.device.getId());
		if (alcError != 0)
			throw new AlcException(alcError);

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
		this.device = new AlDevice(this);
		this.checkAlcErrors();

		this.context = new AlContext(this);
		this.checkAlcErrors();

		// LWJGL objects:
		this.alCtxCap = ALC.createCapabilities(this.device.getId());
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkAlErrors();
		this.checkAlcErrors();
	}

	// Copies the previous context's buffers and reuses them here.
	protected void createAl(AlContext p_ctx) {
		this.device = new AlDevice(this);
		this.checkAlcErrors();

		this.context = new AlContext(p_ctx);
		this.checkAlcErrors();

		// LWJGL objects:
		this.alCtxCap = ALC.createCapabilities(this.device.getId());
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkAlErrors();
		this.checkAlcErrors();
	}
	// endregion

}
