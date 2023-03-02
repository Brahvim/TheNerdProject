package com.brahvim.nerd.openal;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_buffers.AlBufferLoader;
import com.brahvim.nerd.openal.al_buffers.AlNoTypeBuffer;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.openal.al_ext_efx.AlAuxiliaryEffectSlot;
import com.brahvim.nerd.openal.al_ext_efx.al_filter.AlFilter;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PVector;

public class AlSource extends AlNativeResource {

	// region Fields.
	public final static ArrayList<AlSource> ALL_INSTANCES = new ArrayList<>();

	private int id;
	private NerdAl alMan;
	private NerdScene scene;
	private AlBuffer<?> buffer;
	private AlAuxiliaryEffectSlot effectSlot;
	private AlFilter directFilter, auxiliarySendFilter;
	// endregion

	// region Constructors.
	public AlSource(NerdAl p_alMan) {
		AlSource.ALL_INSTANCES.add(this);

		this.alMan = p_alMan;
		this.id = AL11.alGenSources();
		this.scene = this.alMan.getSketch().getSceneManager().getCurrentScene();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	/**
	 * @deprecated Use `AlSource::AlSource(NerdAl, int)` where `int` is the source's
	 *             `id` instead. It is way faster!
	 */
	@Deprecated
	public AlSource(AlSource p_source) {
		AlSource.ALL_INSTANCES.add(this);

		this.scene = p_source.scene;
		this.alMan = p_source.alMan;
		this.id = AL11.alGenSources();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		// region Transfer properties over (hopefully, the JIT inlines!):
		this.setBuffer(p_source.buffer);
		this.setGain(p_source.getGain());
		this.setMinGain(p_source.getMinGain());
		this.setMaxGain(p_source.getMaxGain());
		this.setRolloff(p_source.getRolloff());
		this.setPosition(p_source.getPosition());
		this.setVelocity(p_source.getVelocity());
		this.setSourceType(p_source.getSourceType());
		this.attachDirectFilter(p_source.getDirectFilter());
		this.setOrientation(p_source.getOrientation());
		this.setMaxDistance(p_source.getMaxDistance());
		this.setSampleOffset(p_source.getSampleOffset());
		this.setConeOuterGain(p_source.getConeOuterGain());
		this.attachAuxiliarySendFilter(p_source.getAuxiliarySendFilter());
		this.setConeOuterAngle(p_source.getConeOuterAngle());
		this.setConeInnerAngle(p_source.getConeInnerAngle());
		this.setConeOuterGainHf(p_source.getConeOuterGainHf());
		this.setPitchMultiplier(p_source.getPitchMultiplier());
		this.setReferenceDistance(p_source.getReferenceDistance());
		this.setRoomRolloffFactor(p_source.getRoomRolloffFactor());
		this.setAirAbsorptionFactor(p_source.getAirAbsorptionFactor());
		this.setDirectFilterGainHfAuto(p_source.getDirectFilterGainHfAuto());
		this.setAuxiliarySendFilterGainAuto(p_source.getAuxiliarySendFilterGainAuto());
		this.setAuxiliarySendFilterGainHfAuto(p_source.getAuxiliarySendFilterGainHfAuto());
		// endregion

	}

	public AlSource(NerdAl p_alMan, int p_id) {
		AlSource.ALL_INSTANCES.add(this);

		this.alMan = p_alMan;
		this.id = p_id;

		this.scene = this.alMan.getSketch().getSceneManager().getCurrentScene();
	}

	public AlSource(NerdAl p_alMan, AlBuffer<?> p_buffer) {
		this(p_alMan);
		this.setBuffer(p_buffer);
	}
	// endregion

	// region ...literal "buffer distribution", :joy:
	@SuppressWarnings("unchecked")
	public <T extends Buffer> AlBuffer<T> getBuffer() {
		final int BUFFER_ID = this.getInt(AL11.AL_BUFFER);
		if (BUFFER_ID == this.buffer.getId())
			return (AlBuffer<T>) this.buffer;

		if (this.buffer instanceof AlOggBuffer)
			return (AlBuffer<T>) this.buffer;
		else if (this.buffer instanceof AlWavBuffer)
			return (AlBuffer<T>) this.buffer;
		else
			return (AlBuffer<T>) new AlNoTypeBuffer(this.alMan, BUFFER_ID);
	}

	public void setBuffer(AlBuffer<?> p_buffer) {
		this.buffer = p_buffer;
		this.setInt(AL11.AL_BUFFER, this.buffer.getId());
	}

	public void loadOggBuffer(File p_file) {
		if (this.buffer == null)
			this.buffer = new AlOggBuffer(this.alMan, AlBufferLoader.loadOgg(p_file));
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		return AL11.alGetSourcei(this.id, p_alEnum);
	}

	public float getFloat(int p_alEnum) {
		return AL11.alGetSourcef(this.id, p_alEnum);
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alGetSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alGetSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
	}

	public int[] getIntTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		AL11.alGetSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public /* `float[]` */ float[] getFloatTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		AL11.alGetSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
		// return new PVector(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		AL11.alSourcei(this.id, p_alEnum, p_value);
		this.alMan.checkAlErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alSourcef(this.id, p_alEnum, p_value);
		this.alMan.checkAlErrors();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		AL11.alSourceiv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		AL11.alSourcefv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3i(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.alMan.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alSource3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float... p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3f(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alSource3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, PVector p_value) {
		AL11.alSource3f(this.id, p_alEnum, p_value.x, p_value.y, p_value.z);
		this.alMan.checkAlErrors();
	}
	// endregion

	// region Source getters.
	public NerdScene getScene() {
		return this.scene;
	}

	public int getId() {
		return this.id;
	}

	// region `int` getters.
	public int getSourceType() {
		return this.getInt(AL11.AL_SOURCE_TYPE);
	}

	public int getSourceState() {
		return this.getInt(AL11.AL_SOURCE_STATE);
	}

	public int getBuffersQueued() {
		return this.getInt(AL11.AL_BUFFERS_QUEUED);
	}

	public int getBuffersProcessed() {
		return this.getInt(AL11.AL_BUFFERS_PROCESSED);
	}

	public int getSecOffset() {
		return this.getInt(AL11.AL_SEC_OFFSET);
	}

	public int getSampleOffset() {
		return this.getInt(AL11.AL_SAMPLE_OFFSET);
	}

	public int getByteOffset() {
		return this.getInt(AL11.AL_BYTE_OFFSET);
	}
	// endregion

	// region `float` getters.
	public float getGain() {
		return this.getFloat(AL11.AL_GAIN);
	}

	public float getPitchMultiplier() {
		return this.getFloat(AL11.AL_PITCH);
	}

	public float getMaxDistance() {
		return this.getFloat(AL11.AL_MAX_DISTANCE);
	}

	public float getRolloff() {
		return this.getFloat(AL11.AL_ROLLOFF_FACTOR);
	}

	public float getReferenceDistance() {
		return this.getFloat(AL11.AL_REFERENCE_DISTANCE);
	}

	public float getMinGain() {
		return this.getFloat(AL11.AL_MIN_GAIN);
	}

	public float getMaxGain() {
		return this.getFloat(AL11.AL_MAX_GAIN);
	}

	public float getConeOuterGain() {
		return this.getFloat(AL11.AL_CONE_OUTER_GAIN);
	}

	public float getConeInnerAngle() {
		return this.getFloat(AL11.AL_CONE_INNER_ANGLE);
	}

	public float getConeOuterAngle() {
		return this.getFloat(AL11.AL_CONE_OUTER_ANGLE);
	}
	// endregion

	// region Triplet getters (`float[]`s only).
	public float[] getPosition() {
		return this.getFloatTriplet(AL11.AL_POSITION);
	}

	public float[] getVelocity() {
		return this.getFloatTriplet(AL11.AL_VELOCITY);
	}

	public float[] getOrientation() {
		return this.getFloatTriplet(AL11.AL_ORIENTATION);
	}

	// endregion

	// region [DEPRECATED: Faulty!] State (`boolean`) getters.
	// ..could be made faster with some `boolean`s in this class, y'know?
	// ...just sayin'...
	public boolean isLooping() {
		return this.getInt(AL11.AL_SOURCE_STATE) == AL11.AL_LOOPING;
	}

	public boolean isPaused() {
		return this.getInt(AL11.AL_SOURCE_STATE) == AL11.AL_PAUSED;
	}

	public boolean isStopped() {
		return this.getInt(AL11.AL_SOURCE_STATE) == AL11.AL_STOPPED;
	}

	public boolean isPlaying() {
		return this.getInt(AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
	}
	// endregion
	// endregion

	// region Source setters.
	// region `int` setters.
	public void setSourceType(int p_value) {
		this.setInt(AL11.AL_SOURCE_TYPE, p_value);
	}

	public void setSecOffset(int p_value) {
		this.setInt(AL11.AL_SEC_OFFSET, p_value);
	}

	public void setSampleOffset(int p_value) {
		this.setInt(AL11.AL_SAMPLE_OFFSET, p_value);
	}

	public void setByteOffset(int p_value) {
		this.setInt(AL11.AL_BYTE_OFFSET, p_value);
	}
	// endregion

	// region `float` setters.
	public void setGain(float p_value) {
		this.setFloat(AL11.AL_GAIN, p_value);
	}

	public void setPitchMultiplier(float value) {
		AL11.alSourcef(this.id, AL11.AL_PITCH, value);
	}

	public void setMaxDistance(float p_value) {
		this.setFloat(AL11.AL_MAX_DISTANCE, p_value);
	}

	public void setRolloff(float p_value) {
		this.setFloat(AL11.AL_ROLLOFF_FACTOR, p_value);
	}

	public void setReferenceDistance(float p_value) {
		this.setFloat(AL11.AL_REFERENCE_DISTANCE, p_value);
	}

	public void setMinGain(float p_value) {
		this.setFloat(AL11.AL_MIN_GAIN, p_value);
	}

	public void setMaxGain(float p_value) {
		this.setFloat(AL11.AL_MAX_GAIN, p_value);
	}

	public void setConeOuterGain(float p_value) {
		this.setFloat(AL11.AL_CONE_OUTER_GAIN, p_value);
	}

	public void setConeInnerAngle(float p_value) {
		this.setFloat(AL11.AL_CONE_INNER_ANGLE, p_value);
	}

	public void setConeOuterAngle(float p_value) {
		this.setFloat(AL11.AL_CONE_OUTER_ANGLE, p_value);
	}
	// endregion

	// region Triplet setters.
	public void setPosition(float[] p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setPosition(float p_x, float p_y, float p_z) {
		this.setFloatTriplet(AL11.AL_POSITION, new float[] { p_x, p_y, p_z });
	}

	public void setVelocity(float[] p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setVelocity(float p_x, float p_y, float p_z) {
		this.setFloatTriplet(AL11.AL_POSITION, new float[] { p_x, p_y, p_z });
	}

	public void setOrientation(float[] p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setOrientation(float p_x, float p_y, float p_z) {
		this.setFloatTriplet(AL11.AL_POSITION, new float[] { p_x, p_y, p_z });
	}

	// region `PVector` overloads.
	public void setPosition(PVector p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setVelocity(PVector p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setOrientation(PVector p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}
	// endregion
	// endregion
	// endregion

	// region Anything `EXTEfx`.
	public AlAuxiliaryEffectSlot getEffectSlot() {
		return this.effectSlot;
	}

	public AlAuxiliaryEffectSlot setEffectSlot(AlAuxiliaryEffectSlot p_effectSlot) {
		AlAuxiliaryEffectSlot toRet = this.effectSlot;
		this.effectSlot = p_effectSlot;
		this.setIntTriplet(
				EXTEfx.AL_AUXILIARY_SEND_FILTER,
				this.effectSlot.getId(), 0,
				this.auxiliarySendFilter == null
						? EXTEfx.AL_FILTER_NULL
						: this.auxiliarySendFilter.getId());
		return toRet;
	}

	// region Methods for `AlFilter`s.
	public AlFilter getDirectFilter() {
		return this.directFilter;
	}

	public AlFilter detachDirectFilter() {
		AlFilter toRet = this.directFilter;
		this.directFilter = null;
		this.setInt(EXTEfx.AL_DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);

		return toRet;
	}

	public AlFilter attachDirectFilter(AlFilter p_filter) {
		AlFilter toRet = this.directFilter;
		this.directFilter = p_filter;
		this.setInt(EXTEfx.AL_DIRECT_FILTER, this.directFilter == null
				? EXTEfx.AL_FILTER_NULL
				: this.directFilter.getId());

		return toRet;
	}

	public AlFilter getAuxiliarySendFilter() {
		return this.auxiliarySendFilter;
	}

	public AlFilter detachAuxiliarySendFilter() {
		AlFilter toRet = this.auxiliarySendFilter;
		this.auxiliarySendFilter = null;
		this.setInt(EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_FILTER_NULL);

		return toRet;
	}

	public AlFilter attachAuxiliarySendFilter(AlFilter p_filter) {
		AlFilter toRet = this.auxiliarySendFilter;
		this.auxiliarySendFilter = p_filter;
		this.setIntTriplet(
				EXTEfx.AL_AUXILIARY_SEND_FILTER,
				this.effectSlot.getId(), 0,
				this.auxiliarySendFilter == null
						? EXTEfx.AL_FILTER_NULL
						: this.auxiliarySendFilter.getId());

		return toRet;
	}
	// endregion

	// region `EXTEfx` property getters.
	public float getAirAbsorptionFactor() {
		return this.getFloat(EXTEfx.AL_AIR_ABSORPTION_FACTOR);
	}

	public float getRoomRolloffFactor() {
		return this.getFloat(EXTEfx.AL_ROOM_ROLLOFF_FACTOR);
	}

	public float getConeOuterGainHf() {
		return this.getFloat(EXTEfx.AL_CONE_OUTER_GAINHF);
	}

	public float getDirectFilterGainHfAuto() {
		return this.getFloat(EXTEfx.AL_DIRECT_FILTER_GAINHF_AUTO);
	}

	public float getAuxiliarySendFilterGainAuto() {
		return this.getFloat(EXTEfx.AL_AUXILIARY_SEND_FILTER_GAIN_AUTO);
	}

	public float getAuxiliarySendFilterGainHfAuto() {
		return this.getFloat(EXTEfx.AL_AUXILIARY_SEND_FILTER_GAINHF_AUTO);
	}
	// endregion

	// region `EXTEfx` property setters.
	public void setAirAbsorptionFactor(float p_value) {
		this.setFloat(EXTEfx.AL_AIR_ABSORPTION_FACTOR, p_value);
	}

	public void setRoomRolloffFactor(float p_value) {
		this.setFloat(EXTEfx.AL_ROOM_ROLLOFF_FACTOR, p_value);
	}

	public void setConeOuterGainHf(float p_value) {
		this.setFloat(EXTEfx.AL_CONE_OUTER_GAINHF, p_value);
	}

	public void setDirectFilterGainHfAuto(float p_value) {
		this.setFloat(EXTEfx.AL_DIRECT_FILTER_GAINHF_AUTO, p_value);
	}

	public void setAuxiliarySendFilterGainAuto(float p_value) {
		this.setFloat(EXTEfx.AL_AUXILIARY_SEND_FILTER_GAIN_AUTO, p_value);
	}

	public void setAuxiliarySendFilterGainHfAuto(float p_value) {
		this.setFloat(EXTEfx.AL_AUXILIARY_SEND_FILTER_GAINHF_AUTO, p_value);
	}
	// endregion
	// endregion

	// region Actual state management!
	public void play() {
		AL11.alSourcePlay(this.id);
	}

	public void loop() {
		this.loop(true);
	}

	public void loop(boolean p_value) {
		this.setInt(AL11.AL_LOOPING, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}

	public void stop() {
		AL11.alSourceStop(this.id);
	}

	public void pause() {
		AL11.alSourcePause(this.id);
	}

	public void rewind() {
		AL11.alSourceRewind(this.id);
	}

	public void queueBuffers(AlBuffer<?> p_buffer) {
		AL11.alSourceQueueBuffers(this.id, p_buffer.getId());
	}

	public void queueBuffers(AlBuffer<?>... p_buffers) {
		int[] buffers = new int[p_buffers.length];
		AL11.alSourceQueueBuffers(this.id, buffers);
	}

	public void unqueueBuffers(AlBuffer<?>... p_buffers) {
		int[] buffers = new int[p_buffers.length];
		AL11.alSourceUnqueueBuffers(this.id, buffers);
	}

	public void unqueueProcessedBuffers(AlBuffer<?> p_buffer) {
		AL11.alSourceUnqueueBuffers(this.id);
	}

	// TODO: MAKE `AssetLoaderOptions` (finally) and make buffers persistent :D

	@Override
	protected void disposeImpl() {
		AL11.alDeleteSources(this.id);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
		AlSource.ALL_INSTANCES.remove(this);
	}
	// endregion

}
