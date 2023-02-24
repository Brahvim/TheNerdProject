package com.brahvim.nerd.openal;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.openal.al_buffers.AlBufferLoader;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;

import processing.core.PVector;

public class AlSource {

	// region Fields.
	public final static ArrayList<AlSource> sources = new ArrayList<>();

	private int id;
	private NerdAl manager;
	private AlBuffer<?> buffer;
	// endregion

	// region Constructors.
	public AlSource(NerdAl p_manager) {
		this.manager = p_manager;
		this.id = AL11.alGenSources();

		this.setInt(AL11.AL_SOURCE_TYPE, 0);
		this.manager.checkAlErrors();
		this.manager.checkAlcErrors();
	}

	public AlSource(NerdAl p_manager, AlBuffer<?> p_buffer) {
		this(p_manager);
		this.setBuffer(p_buffer);
	}
	// endregion

	// region Get every source, ever!
	/* `package` */ static ArrayList<AlSource> getEverySourceEverByReference() {
		return AlSource.sources;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<AlSource> getEverySourceEver() {
		return (ArrayList<AlSource>) AlSource.sources.clone();
	}
	// endregion

	// region ...literal "buffer distribution", :joy:
	public AlBuffer<?> getBuffer() {
		return this.buffer;
	}

	public void setBuffer(AlBuffer<?> p_buffer) {
		this.buffer = p_buffer;
		AL11.alSourcei(this.id, AL11.AL_BUFFER, this.buffer.getId());
	}

	public void loadOggBuffer(File p_file) {
		if (this.buffer == null)
			this.buffer = new AlOggBuffer(this.manager, AlBufferLoader.loadOgg(p_file));
	}
	// endregion

	// region C-style AL-API getters.
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

	// region C-style AL-API setters.
	public void setInt(int p_alEnum, int p_value) {
		AL11.alSourcei(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alSourcef(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setIntVector(int p_alEnum, int... p_value) {
		AL11.alSourceiv(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		AL11.alSourcefv(this.id, p_alEnum, p_values);
		this.manager.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3i(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.manager.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alSource3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3f(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alSource3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, PVector p_value) {
		AL11.alSource3f(this.id, p_alEnum, p_value.x, p_value.y, p_value.z);
		this.manager.checkAlErrors();
	}
	// endregion

	// region Source getters.
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

	// region Actual state management!
	public void setSourceState(int p_alEnum, boolean p_value) {
		this.setInt(p_alEnum, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}

	public void play() {
		AL11.alSourcePlay(this.id);
	}

	public void loop() {
		this.setInt(AL11.AL_LOOPING, AL11.AL_TRUE);
		this.play();
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

	public void dispose() {
		this.manager.getContextSources().remove(this);
		AlSource.sources.remove(this);
		AL11.alDeleteSources(this.id);
		this.manager.checkAlErrors();
		this.manager.checkAlcErrors();
	}
	// endregion

}
