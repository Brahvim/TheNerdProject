package com.brahvim.nerd.openal;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlException;
import com.brahvim.nerd.openal.al_exceptions.AlcException;
import com.brahvim.nerd.openal.al_exceptions.NerdAbstractOpenAlException;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

public class NerdAl {

	// region [DEPRECATED] Inner classes, interfaces and enums.
	/*
	 * 
	 * @FunctionalInterface
	 * interface DeviceUse {
	 * public void use(AlDevice p_device);
	 * }
	 * 
	 * @FunctionalInterface
	 * interface ContextUse {
	 * public void use(AlContext p_context);
	 * }
	 * 
	 * @FunctionalInterface
	 * interface DeviceAndContextUse {
	 * public void use(AlDevice p_device, AlContext p_context);
	 * }
	 */
	// endregion

	// region Fields.
	private final ArrayList<AlSource> contextSources = new ArrayList<>();
	private final ArrayList<AlBuffer<?>> deviceBuffers = new ArrayList<>();
	private final Sketch SKETCH;

	private boolean hasDisposed;
	private ALCapabilities alCap;
	private ALCCapabilities alCtxCap;
	private /* `volatile` */ AlDevice device;
	private /* `volatile` */ AlContext context;
	// endregion

	// region Constructors.
	public NerdAl(Sketch p_sketch) {
		this(p_sketch, AlDevice.getDefaultDeviceName());
	}

	public NerdAl(Sketch p_sketch, String p_deviceName) {
		this.SKETCH = p_sketch;
		this.createAl(p_deviceName);
	}
	// endregion

	// region Listener functions.
	// region C-style OpenAL Listener getters.
	public int getListenerInt(int p_alEnum) {
		return AL11.alGetListeneri(p_alEnum);
	}

	public float getListenerFloat(int p_alEnum) {
		return AL11.alGetListenerf(p_alEnum);
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getListenerIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alGetListeneriv(p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public float[] getListenerFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alGetListenerfv(p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
	}

	public int[] getListenerIntTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		AL11.alGetListeneriv(p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public /* `float[]` */ float[] getListenerFloatTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		AL11.alGetListenerfv(p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
		// return new PVector(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
	}
	// endregion

	// region C-style OpenAL Listener setters.
	public void setListenerInt(int p_alEnum, int p_value) {
		AL11.alListeneri(p_alEnum, p_value);
		this.checkAlErrors();
	}

	public void setListenerFloat(int p_alEnum, float p_value) {
		AL11.alListenerf(p_alEnum, p_value);
		this.checkAlErrors();
	}

	public void setListenerIntVector(int p_alEnum, int... p_value) {
		AL11.alListeneriv(p_alEnum, p_value);
		this.checkAlErrors();
	}

	public void setListenerFloatVector(int p_alEnum, float... p_values) {
		AL11.alListenerfv(p_alEnum, p_values);
		this.checkAlErrors();
	}

	public void setListenerIntTriplet(int p_alEnum, int... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alListener3i(p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.checkAlErrors();
	}

	public void setListenerIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alListener3i(p_alEnum, p_i1, p_i2, p_i3);
		this.checkAlErrors();
	}

	public void setListenerFloatTriplet(int p_alEnum, float... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alListener3f(p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.checkAlErrors();
	}

	public void setListenerFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alListener3f(p_alEnum, p_f1, p_f2, p_f3);
		this.checkAlErrors();
	}

	public void setListenerFloatTriplet(int p_alEnum, PVector p_value) {
		AL11.alListener3f(p_alEnum, p_value.x, p_value.y, p_value.z);
		this.checkAlErrors();
	}
	// endregion

	// region Listener getters.
	public float getListenerGain() {
		return this.getListenerFloat(AL11.AL_GAIN);
	}

	public float[] getListenerPosition() {
		return this.getListenerFloatTriplet(AL11.AL_POSITION);
	}

	public float[] getListenerVelocity() {
		return this.getListenerFloatTriplet(AL11.AL_VELOCITY);
	}

	public float[] getListenerOrientation() {
		return this.getListenerFloatTriplet(AL11.AL_ORIENTATION);
	}
	// endregion

	// region Listener setters.
	public void setListenerGain(float p_value) {
		this.setListenerFloat(AL11.AL_GAIN, p_value);
	}

	// region `float...` overloads for listener vectors.
	public void setListenerPosition(float... p_values) {
		this.setListenerFloatTriplet(AL11.AL_POSITION, p_values);
	}

	public void setListenerVelocity(float... p_values) {
		this.setListenerFloatTriplet(AL11.AL_VELOCITY, p_values);
	}

	public void setListenerOrientation(float... p_values) {
		this.setListenerFloatTriplet(AL11.AL_ORIENTATION, p_values);
	}
	// endregion

	// region `PVector` overloads for listener vectors.
	public void setListenerPosition(PVector p_value) {
		this.setListenerFloatTriplet(AL11.AL_POSITION, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerVelocity(PVector p_value) {
		this.setListenerFloatTriplet(AL11.AL_VELOCITY, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerOrientation(PVector p_value) {
		this.setListenerFloatTriplet(AL11.AL_ORIENTATION, p_value.x, p_value.y, p_value.z);
	}
	// endregion
	// endregion
	// endregion

	// region Getters and setters!...
	// Yes, there are no C-style setters.
	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		int toRet = AL11.alGetInteger(p_alEnum);
		this.checkAlErrors();
		return toRet;
	}

	public float getFloat(int p_alEnum) {
		float toRet = AL11.alGetFloat(p_alEnum);
		this.checkAlErrors();
		return toRet;
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alGetIntegerv(p_alEnum, buffer);
		MemoryStack.stackPop();

		this.checkAlErrors();
		return buffer.array();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alGetFloatv(p_alEnum, buffer);
		MemoryStack.stackPop();

		this.checkAlErrors();
		return buffer.array();
	}
	// endregion

	// region Getters.
	// region OpenAL API getters.
	public float getDistanceModel() {
		return this.getFloat(AL11.AL_DISTANCE_MODEL);
	}

	public float getDopplerFactor() {
		return this.getFloat(AL11.AL_DOPPLER_FACTOR);
	}

	public float getSpeedOfSound() {
		return this.getFloat(AL11.AL_SPEED_OF_SOUND);
	}
	// endregion

	public ArrayList<AlBuffer<?>> getDeviceBuffers() {
		return this.deviceBuffers;
	}

	public Sketch getSketch() {
		return this.SKETCH;
	}

	public AlDevice getDevice() {
		return this.device;
	}

	public AlContext getContext() {
		return this.context;
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
	// region OpenAL API setters.
	public void setDistanceModel(int p_value) {
		AL11.alDistanceModel(p_value);
	}

	public void setDopplerFactor(float p_value) {
		AL11.alDopplerFactor(p_value);
	}

	public void setSpeedOfSound(float p_value) {
		AL11.alSpeedOfSound(p_value);
	}
	// endregion

	public void changeDevice(AlDevice p_dv, AlContext p_ctx) {
		this.device = p_dv;
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) != p_dv.getId())
			throw new AlException(ALC11.ALC_INVALID_CONTEXT);
		this.context = p_ctx;
	}

	public void setContext(AlContext p_ctx) {
		if (ALC11.alcGetContextsDevice(p_ctx.getId()) == this.device.getId())
			this.context = p_ctx;
		else
			this.context = new AlContext(p_ctx);
	}
	// endregion
	// endregion

	// region Error, and other checks.
	public static boolean isSource(int p_id) {
		return AL11.alIsSource(p_id);
	}

	public static boolean isBuffer(int p_id) {
		return AL11.alIsBuffer(p_id);
	}

	public static boolean isEffect(int p_id) {
		return EXTEfx.alIsEffect(p_id);
	}

	public static boolean isFilter(int p_id) {
		return EXTEfx.alIsFilter(p_id);
	}

	public static boolean isEffectSlot(int p_id) {
		return EXTEfx.alIsAuxiliaryEffectSlot(p_id);
	}

	@Deprecated
	public static int errorStringToCode(String p_errorString) {
		return AL11.alGetEnumValue(p_errorString
				.split(NerdAbstractOpenAlException.ERR_CODE_MIDFIX, 0)[0]);
	}

	public static int errorStringToCode(NerdAbstractOpenAlException p_exception) {
		return AL11.alGetEnumValue(p_exception.getAlErrorString());
	}

	public int checkAlErrors() throws AlException {
		int alError = AL11.alGetError();

		// `40964` is THE MOST annoying error.
		// Its error string is literally "No Error"!
		if (!(alError == 0 || alError == 40964))
			throw new AlException(alError);

		return alError;
	}

	public int checkAlcErrors() throws AlcException {
		int alcError = ALC11.alcGetError(this.device.getId());

		if (alcError != 0)
			throw new AlcException(this.getDeviceId(), alcError);

		return alcError;
	}
	// endregion

	// region Loading sources from disk.
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
		if (this.hasDisposed)
			return;
		this.hasDisposed = true;

		for (int i = this.contextSources.size(); i > 1; i++)
			this.contextSources.get(i).dispose();

		for (int i = this.deviceBuffers.size(); i > 1; i++)
			this.deviceBuffers.get(i).dispose();

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
	// endregion

}
