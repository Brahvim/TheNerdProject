package com.brahvim.nerd.openal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_buffers.NerdAlTypedBuffer;

import processing.core.PVector;

public class AlSource {

	// region Fields.
	private int id;
	private NerdAl manager;
	private NerdAlTypedBuffer<?> buffer;
	// endregion

	// region Constructors.
	public AlSource(NerdAl p_manager) {
		this.manager = p_manager;
		this.id = AL11.alGenSources();
	}

	public AlSource(NerdAl p_manager, NerdAlTypedBuffer<?> p_buffer) {
		this(p_manager);
		this.setBuffer(p_buffer);
	}
	// endregion

	// region ...literal "buffer distribution", :joy:
	public NerdAlTypedBuffer<?> getBuffer() {
		return this.buffer;
	}

	public void setBuffer(NerdAlTypedBuffer<?> p_buffer) {
		this.buffer = p_buffer;
		AL11.alSourcei(this.id, AL11.AL_BUFFER, this.buffer.getId());
	}
	// endregion

	// region C-style AL-API getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(1);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
		return intBuffer.get();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(1);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
		return floatBuffer.get();
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
		return intBuffer.array();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
		return floatBuffer.array();
	}

	public int[] getIntTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
		return intBuffer.array();
	}

	public /* `float[]` */ float[] getFloatTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		this.manager.checkAlErrors();
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

	public void setIntVector(int p_alEnum, int[] p_value) {
		AL11.alSourceiv(this.id, p_alEnum, p_value);
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

	public void setFloatVector(int p_alEnum, float[] p_value) {
		AL11.alSourcefv(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}
	// endregion

	// region Source getters.
	// region `int` getters.
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

	// region State getters.
	public int getSourceType() {
		return this.getInt(AL11.AL_SOURCE_TYPE);
	}

	public boolean isLooping() {
		return this.getInt(AL11.AL_LOOPING) == 1;
	}
	// endregion
	// endregion

	// region Source setters.
	// region `int` setters.
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

	public void setVelocity(float[] p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
	}

	public void setOrientation(float[] p_value) {
		this.setFloatTriplet(AL11.AL_POSITION, p_value);
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

	// region State setters.
	public void setSourceType(int p_value) {
		this.setInt(AL11.AL_SOURCE_TYPE, p_value);
	}

	public void setLooping(boolean p_value) {
		this.setInt(AL11.AL_LOOPING, p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}
	// endregion
	// endregion

}
