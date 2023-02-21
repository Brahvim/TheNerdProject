package com.brahvim.nerd.openal;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlException;
import com.brahvim.nerd.openal.al_exceptions.AlcException;
import com.brahvim.nerd.openal.al_exceptions.NerdAbstractOpenAlException;

public class NerdAl {

	// region Inner classes, interfaces and enums.
	@FunctionalInterface
	interface DeviceUse {
		public void use(AlDevice p_device);
	}

	@FunctionalInterface
	interface ContextUse {
		public void use(AlContext p_context);
	}

	@FunctionalInterface
	interface DeviceAndContextUse {
		public void use(AlDevice p_device, AlContext p_context);
	}
	// endregion

	// region Fields.
	private final ArrayList<AlBuffer<?>> deviceBuffers = new ArrayList<>();
	private final ArrayList<AlSource> contextSources = new ArrayList<>();

	private volatile AlDevice device;
	private volatile AlContext context;
	private ALCapabilities alCap;
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

	// region Getters.
	public ArrayList<AlBuffer<?>> getDeviceBuffers() {
		return this.deviceBuffers;
	}

	public ArrayList<AlSource> getContextSources() {
		return this.contextSources;
	}

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

	// region `using()` methods.
	public void usingDevice(NerdAl.DeviceUse p_use) {
		synchronized (this.device) {
			p_use.use(this.device);
		}
	}

	public void usingContext(NerdAl.ContextUse p_use) {
		synchronized (this.context) {
			p_use.use(this.context);
		}
	}

	public void usingContextAndDevice(NerdAl.DeviceAndContextUse p_use) {
		synchronized (this.device) {
			synchronized (this.context) {
				p_use.use(this.device, this.context);
			}
		}
	}
	// endregion

	// region Error checks.
	public static int errorStringToCode(String p_errorString) {
		return AL11.alGetEnumValue(p_errorString
				.split(NerdAbstractOpenAlException.ERR_CODE_MIDFIX, 0)[0]);
	}

	public int checkAlErrors() throws AlException {
		int alError = AL11.alGetError();

		if (!(alError == 0)) // || alError == 40964))
			throw new AlException(alError);

		return alError;
	}

	public int checkAlcErrors() throws AlcException {
		int alcError;

		synchronized (this.device) {
			alcError = ALC11.alcGetError(this.device.getId());
		}

		if (alcError != 0)
			throw new AlcException(alcError);

		return alcError;
	}
	// endregion

	// region Loading.
	public AlSource sourceFromOgg(File p_file) {
		return new AlSource(this, new AlOggBuffer(this).loadFrom(p_file));
	}

	public AlSource sourceFromWav(File p_file) {
		return new AlSource(this, new AlWavBuffer(this).loadFrom(p_file));
	}

	public AlSource sourceFromOgg(String p_filePath) {
		return new AlSource(this, new AlOggBuffer(this).loadFrom(p_filePath));
	}

	public AlSource sourceFromWav(String p_filePath) {
		return new AlSource(this, new AlWavBuffer(this).loadFrom(p_filePath));
	}
	// endregion

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
