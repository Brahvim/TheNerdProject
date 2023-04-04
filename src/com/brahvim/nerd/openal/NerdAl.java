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
import com.brahvim.nerd.openal.al_ext_efx.AlAuxiliaryEffectSlot;
import com.brahvim.nerd.openal.al_ext_efx.AlEffect;
import com.brahvim.nerd.openal.al_ext_efx.al_filter.AlFilter;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.rendering.cameras.NerdAbstractCamera;

import processing.core.PVector;

public class NerdAl {

	// region Fields.
	public static final float UNIT_SIZE_3D_PARK_SCENE = 100.0f;
	public static final float UNIT_SIZE_MOUSE_RELATIVE = 1.0f;
	public static final float UNIT_SIZE_2D_SCENE = 25.0f;

	public final long DEFAULT_CONTEXT_ID;
	public final AlContext DEFAULT_CONTEXT;

	public float unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;

	private final Sketch SKETCH;

	private ALCapabilities alCap;
	private ALCCapabilities alCtxCap;
	private /* `volatile` */ AlDevice device;
	private /* `volatile` */ AlContext context;
	// endregion

	// region Construction.
	public NerdAl(final Sketch p_sketch) {
		this(p_sketch, AlDevice.getDefaultDeviceName());
	}

	public NerdAl(final Sketch p_sketch, final String p_deviceName) {
		this.SKETCH = p_sketch;
		this.DEFAULT_CONTEXT = this.createAl(AlDevice.getDefaultDeviceName());
		this.DEFAULT_CONTEXT_ID = this.DEFAULT_CONTEXT.getId();
		this.delegatedConstruction();
	}

	public NerdAl(final Sketch p_sketch, final AlContext.AlContextSettings p_settings) {
		this.SKETCH = p_sketch;
		this.DEFAULT_CONTEXT = this.createAl(AlDevice.getDefaultDeviceName(), p_settings);
		this.DEFAULT_CONTEXT_ID = this.DEFAULT_CONTEXT.getId();
		this.delegatedConstruction();
	}

	public NerdAl(final Sketch p_sketch, final AlContext.AlContextSettings p_settings, final String p_deviceName) {
		this.SKETCH = p_sketch;
		this.DEFAULT_CONTEXT = this.createAl(AlDevice.getDefaultDeviceName(), p_settings);
		this.DEFAULT_CONTEXT_ID = this.DEFAULT_CONTEXT.getId();
		this.delegatedConstruction();
	}

	private void delegatedConstruction() {
		this.initListeners();
		// this.setMetersPerUnit(0.0001f); // (float) Math.pow(10.0d, -150.0d));
	}

	private void initListeners() {
		// When the scene is changed, delete unnecessary OpenAL data:
		this.SKETCH.getSceneManager().addSceneChangedListener((s, p, c) -> {
			if (p != null)
				this.scenelyDisposal();
			this.unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;
		});

		// I wanted to declare this lambda as an anon class instead, but I wanted to
		// watch this trick where I have a variable from outside the lambda work there.
		// ...It does!:
		final PVector lastCameraPos = new PVector();

		this.SKETCH.addDrawListener((s) -> {
			// Process everything, every frame!:
			this.framelyCallback();

			final NerdAbstractCamera camera = this.SKETCH.getCamera();

			this.setListenerOrientation(camera.up);
			this.setListenerPosition(PVector.div(camera.pos, this.unitSize));
			this.setListenerVelocity(PVector.sub(camera.pos, lastCameraPos));
			// PVector.div((PVector.sub(camera.pos, lastCameraPos)), this.unitSize));

			lastCameraPos.set(camera.pos);
		});

		// When the sketch is exiting, delete all OpenAL native data:
		this.SKETCH.addSketchDisposalListener((s) -> {
			this.completeDisposal();
		});
	}
	// endregion

	// region Listener functions.
	// region C-style OpenAL listener getters.
	public int getListenerInt(long p_ctxId, int p_alEnum) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		return AL11.alGetListeneri(p_alEnum);
	}

	public float getListenerFloat(long p_ctxId, int p_alEnum) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		return AL11.alGetListenerf(p_alEnum);
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getListenerIntVector(long p_ctxId, int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alGetListeneriv(p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public float[] getListenerFloatVector(long p_ctxId, int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alGetListenerfv(p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
	}

	public int[] getListenerIntTriplet(long p_ctxId, int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alGetListeneriv(p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public /* `float[]` */ float[] getListenerFloatTriplet(long p_ctxId, int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alGetListenerfv(p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
		// return new PVector(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
	}
	// endregion

	// region C-style OpenAL listener setters.
	public void setListenerInt(long p_ctxId, int p_alEnum, int p_value) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListeneri(p_alEnum, p_value);
		this.checkAlError();
	}

	public void setListenerFloat(long p_ctxId, int p_alEnum, float p_value) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListenerf(p_alEnum, p_value);
		this.checkAlError();
	}

	public void setListenerIntVector(long p_ctxId, int p_alEnum, int... p_value) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListeneriv(p_alEnum, p_value);
		this.checkAlError();
	}

	public void setListenerFloatVector(long p_ctxId, int p_alEnum, float... p_values) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListenerfv(p_alEnum, p_values);
		this.checkAlError();
	}

	public void setListenerIntTriplet(long p_ctxId, int p_alEnum, int... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setIntTriplet(AlContext p_ctx, )` cannot take an array of size other than `3`!");

		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListener3i(p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.checkAlError();
	}

	public void setListenerIntTriplet(long p_ctxId, int p_alEnum, int p_i1, int p_i2, int p_i3) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListener3i(p_alEnum, p_i1, p_i2, p_i3);
		this.checkAlError();
	}

	public void setListenerFloatTriplet(long p_ctxId, int p_alEnum, float... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setFloatTriplet(AlContext p_ctx, )` cannot take an array of size other than `3`!");

		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListener3f(p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.checkAlError();
	}

	public void setListenerFloatTriplet(long p_ctxId, int p_alEnum, float p_f1, float p_f2, float p_f3) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListener3f(p_alEnum, p_f1, p_f2, p_f3);
		this.checkAlError();
	}

	public void setListenerFloatTriplet(long p_ctxId, int p_alEnum, PVector p_value) {
		ALC11.alcMakeContextCurrent(p_ctxId);
		this.checkAlcError();
		AL11.alListener3f(p_alEnum, p_value.x, p_value.y, p_value.z);
		this.checkAlError();
	}
	// endregion

	// region Listener getters.
	public float getListenerMetersPerUnit(AlContext p_ctx) {
		return this.getListenerFloat(p_ctx.getId(), EXTEfx.AL_METERS_PER_UNIT);
	}

	public float getListenerGain(AlContext p_ctx) {
		return this.getListenerFloat(p_ctx.getId(), AL11.AL_GAIN);
	}

	public float[] getListenerPosition(AlContext p_ctx) {
		return this.getListenerFloatTriplet(p_ctx.getId(), AL11.AL_POSITION);
	}

	public float[] getListenerVelocity(AlContext p_ctx) {
		return this.getListenerFloatTriplet(p_ctx.getId(), AL11.AL_VELOCITY);
	}

	public float[] getListenerOrientation(AlContext p_ctx) {
		return this.getListenerFloatTriplet(p_ctx.getId(), AL11.AL_ORIENTATION);
	}
	// endregion

	// region Listener setters.
	public void setListenerGain(AlContext p_ctx, float p_value) {
		this.setListenerFloat(p_ctx.getId(), AL11.AL_GAIN, p_value);
	}

	public void setMetersPerUnit(AlContext p_ctx, float p_value) {
		this.setListenerFloat(p_ctx.getId(), EXTEfx.AL_METERS_PER_UNIT, p_value);
	}

	// region `float...` overloads for listener vectors.
	public void setListenerPosition(AlContext p_ctx, float... p_values) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_POSITION, p_values);
	}

	public void setListenerVelocity(AlContext p_ctx, float... p_values) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_VELOCITY, p_values);
	}

	public void setListenerOrientation(AlContext p_ctx, float... p_values) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_ORIENTATION, p_values);
	}
	// endregion

	// region `PVector` overloads for listener vectors.
	public void setListenerPosition(AlContext p_ctx, PVector p_value) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_POSITION, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerVelocity(AlContext p_ctx, PVector p_value) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_VELOCITY, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerOrientation(AlContext p_ctx, PVector p_value) {
		this.setListenerFloatTriplet(p_ctx.getId(), AL11.AL_ORIENTATION, p_value.x, p_value.y, p_value.z);
	}
	// endregion
	// endregion

	// region Default listener getters.
	public float getListenerMetersPerUnit() {
		return this.getListenerFloat(this.DEFAULT_CONTEXT_ID, EXTEfx.AL_METERS_PER_UNIT);
	}

	public float getListenerGain() {
		return this.getListenerFloat(this.DEFAULT_CONTEXT_ID, AL11.AL_GAIN);
	}

	public float[] getListenerPosition() {
		return this.getListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_POSITION);
	}

	public float[] getListenerVelocity() {
		return this.getListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_VELOCITY);
	}

	public float[] getListenerOrientation() {
		return this.getListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_ORIENTATION);
	}
	// endregion

	// region Default listener setters.
	public void setListenerGain(float p_value) {
		this.setListenerFloat(this.DEFAULT_CONTEXT_ID, AL11.AL_GAIN, p_value);
	}

	public void setMetersPerUnit(float p_value) {
		this.setListenerFloat(this.DEFAULT_CONTEXT_ID, EXTEfx.AL_METERS_PER_UNIT, p_value);
	}

	// region `float...` overloads for listener vectors.
	public void setListenerPosition(float... p_values) {
		this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_POSITION, p_values);
	}

	public void setListenerVelocity(float... p_values) {
		this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_VELOCITY, p_values);
	}

	public void setListenerOrientation(float... p_values) {
		this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_ORIENTATION, p_values);
	}
	// endregion

	// region `PVector` overloads for listener vectors.
	public void setListenerPosition(PVector p_value) {
		this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_POSITION, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerVelocity(PVector p_value) {
		this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_VELOCITY, p_value.x, p_value.y, p_value.z);
	}

	public void setListenerOrientation(PVector p_value) {
		// this.setListenerFloatTriplet(this.DEFAULT_CONTEXT_ID, AL11.AL_ORIENTATION,
		// p_value.x, p_value.y, p_value.z); // Broken ¯\_(ツ)_/¯

		final float[] values = p_value.array();

		// The usual case is for `y` to be `1`, and the rest to be `0`.
		// This should keep that logic significantly fast:
		values[0] = values[0] == 0.0f ? 0.0f : values[0] > 0.0f ? 1.0f : -1.0f;
		values[1] = values[1] == 0.0f ? 0.0f : values[1] > 0.0f ? 1.0f : -1.0f;
		values[2] = values[2] == 0.0f ? 0.0f : values[2] > 0.0f ? 1.0f : -1.0f;

		try { // Need to put this in a try-catch block...
			this.setListenerFloatVector(this.DEFAULT_CONTEXT_ID, AL11.AL_ORIENTATION, values);
		} catch (AlException e) { // TOO MANY FALSE POSITIVES!
			// System.out.printf("""
			// Setting the listener's orientation to `%s` failed!
			// Values: `[%.7f, %.7f, %.7f]`.""", p_value, values[0], values[1], values[2]);
		}
	}
	// endregion
	// endregion
	// endregion

	// region Getters and setters!...
	// region C-style OpenAL getters.
	public int getAlInt(int p_alEnum) {
		int toRet = AL11.alGetInteger(p_alEnum);
		this.checkAlError();
		return toRet;
	}

	public float getAlFloat(int p_alEnum) {
		float toRet = AL11.alGetFloat(p_alEnum);
		this.checkAlError();
		return toRet;
	}

	public int[] getAlIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alGetIntegerv(p_alEnum, buffer);
		MemoryStack.stackPop();

		this.checkAlError();
		return buffer.array();
	}

	public float[] getAlFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alGetFloatv(p_alEnum, buffer);
		MemoryStack.stackPop();

		this.checkAlError();
		return buffer.array();
	}
	// endregion

	// region Getters.
	// region OpenAL API getters.
	public float getDistanceModel() {
		return this.getAlFloat(AL11.AL_DISTANCE_MODEL);
	}

	public float getDopplerFactor() {
		return this.getAlFloat(AL11.AL_DOPPLER_FACTOR);
	}

	public float getSpeedOfSound() {
		return this.getAlFloat(AL11.AL_SPEED_OF_SOUND);
	}
	// endregion

	public Sketch getSketch() {
		return this.SKETCH;
	}

	public AlDevice getDevice() {
		return this.device;
	}

	public AlContext getContext() {
		return this.context;
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

	// Only these three setters.
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

	public static int errorStringToCode(String p_errorString) {
		return AL11.alGetEnumValue(p_errorString.split("\"")[1]);
	}

	public static int errorStringToCode(NerdAbstractOpenAlException p_exception) {
		return AL11.alGetEnumValue(p_exception.getAlcErrorString());
	}

	public int checkAlError() throws AlException {
		final int alError = AL11.alGetError();

		// `40964` is THE MOST annoying error.
		// Its error string is literally "No Error"!
		if (!(alError == 0 || alError == 40964))
			throw new AlException(alError);

		return alError;
	}

	public int checkAlcError() throws AlcException {
		final int alcError = ALC11.alcGetError(this.device.getId());

		if (alcError != 0)
			throw new AlcException(this.getDeviceId(), alcError);

		return alcError;
	}
	// endregion

	// region Loading sources from disk.
	public AlSource sourceFromOgg(File p_file) {
		return new AlSource(this, new AlOggBuffer(this).loadFrom(p_file));
	}

	@Deprecated
	public AlSource sourceFromWav(File p_file) {
		return new AlSource(this, new AlWavBuffer(this).loadFrom(p_file));
	}

	public AlSource sourceFromOgg(String p_filePath) {
		return new AlSource(this, new AlOggBuffer(this).loadFrom(p_filePath));
	}

	@Deprecated
	public AlSource sourceFromWav(String p_filePath) {
		return new AlSource(this, new AlWavBuffer(this).loadFrom(p_filePath));
	}
	// endregion

	// region State management.
	public void framelyCallback() {
		this.device.disconnectionCheck();

		for (AlSource s : AlSource.ALL_INSTANCES)
			s.framelyCallback();
	}

	@SuppressWarnings("deprecation")
	public void scenelyDisposal() {
		ArrayList<? extends AlNativeResource> list = null;

		for (int listId = 0; listId != 6; listId++) {
			switch (listId) {
				// Yes, I know some of these can reference the `protected` `ArrayList` directly.
				case 0 -> list = AlCapture.getAllInstances();
				case 1 -> list = AlFilter.getAllInstances();
				case 2 -> list = AlEffect.getAllInstances();
				case 3 -> list = AlAuxiliaryEffectSlot.getAllInstances(); // Always after the other two!
				case 4 -> list = AlSource.getAllInstances(); // Before the buffers!
				case 5 -> list = AlBuffer.getAllInstances(); // ...After the sources.
			}

			if (list == null)
				continue;

			for (int i = list.size() - 1; i > -1; i--)
				list.get(i).dispose();
		}
	}

	@SuppressWarnings("deprecation")
	public void completeDisposal() {
		ArrayList<? extends AlNativeResource> list = null;

		for (int listId = 0; listId < 8; listId++) {
			switch (listId) {
				// Yes, I know some of these can reference the `protected` `ArrayList` directly.
				// Is this an extremely heavy resource?:
				case 0 -> list = AlCapture.getAllInstances();
				case 1 -> list = AlFilter.getAllInstances();
				case 2 -> list = AlEffect.getAllInstances();
				case 3 -> list = AlAuxiliaryEffectSlot.getAllInstances(); // Always after the other two!
				case 4 -> list = AlSource.getAllInstances(); // Before the buffers!
				case 5 -> list = AlBuffer.getAllInstances(); // ...After the sources.
				case 6 -> list = AlContext.getAllInstances();
				case 7 -> list = AlDevice.getAllInstances();
			}

			if (list == null)
				continue;

			for (int i = list.size() - 1; i > -1; i--)
				list.get(i).disposeForcibly();
		}
	}

	protected AlContext createAl(String p_deviceName) {
		return this.createAl(p_deviceName, null);
	}

	protected AlContext createAl(String p_deviceName, AlContext.AlContextSettings p_contextSettings) {
		this.device = new AlDevice(this);
		this.context = new AlContext(this, p_contextSettings);

		// LWJGL objects:
		this.alCtxCap = ALC.createCapabilities(this.getDeviceId());
		this.alCap = AL.createCapabilities(this.alCtxCap);

		this.checkAlError();
		this.checkAlcError();

		return this.context;
	}
	// endregion

}
