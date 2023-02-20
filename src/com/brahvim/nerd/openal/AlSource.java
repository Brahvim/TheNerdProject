package com.brahvim.nerd.openal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_buffers.NerdAlTypedBuffer;

import processing.core.PVector;

public class AlSource {
	private int id;
	private NerdAlContext manager;
	private NerdAlTypedBuffer<?> buffer;

	// region Constructors.
	public AlSource(NerdAlContext p_ctx) {
		this.manager = p_ctx;
		this.id = AL11.alGenSources();
	}

	public AlSource(NerdAlContext p_ctx, NerdAlTypedBuffer<?> p_buffer) {
		this(p_ctx);
		this.setBuffer(p_buffer);
	}
	// endregion

	public NerdAlTypedBuffer<?> getBuffer() {
		return this.buffer;
	}

	public void setBuffer(NerdAlTypedBuffer<?> p_buffer) {
		this.buffer = p_buffer;
		AL11.alSourcei(this.id, AL11.AL_BUFFER, this.buffer.getId());
	}

	// region C-style AL-API getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(1);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.get();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(1);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.get();
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
	}

	public int[] getIntTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		AL11.alSourceiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public /* `float[]` */ PVector getFloatTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		AL11.alSourcefv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		// return floatBuffer.array();
		return new PVector(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
	}
	// endregion

	// region C-style AL-API setters.
	public void setInt(int p_alEnum, int p_value) {
		AL11.alSourcei(this.id, p_alEnum, p_value);
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alSourcef(this.id, p_alEnum, p_value);
	}

	public void setIntVector(int p_alEnum, int[] p_value) {
		AL11.alSourceiv(this.id, p_alEnum, p_value);
	}

	public void setIntTriplet(int p_alEnum, int[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3i(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alSource3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
	}

	public void setFloatTriplet(int p_alEnum, float[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`AlSource::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alSource3f(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alSource3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
	}

	public void setFloatTriplet(int p_alEnum, PVector p_value) {
		AL11.alSource3f(this.id, p_alEnum, p_value.x, p_value.y, p_value.z);
	}

	public void setFloatVector(int p_alEnum, float[] p_value) {
		AL11.alSourcefv(this.id, p_alEnum, p_value);
	}
	// endregion

	// region Source getters.
	public float getPitch() {
		return this.getFloat(AL11.AL_PITCH);
	}

	public float getGain() {
		return this.getFloat(AL11.AL_GAIN);
	}
	// endregion

	// region Source setters.
	public void setGain(float p_value) {
		this.setFloat(AL11.AL_GAIN, p_value);
	}

	public void setPitch(float value) {
		AL11.alSourcef(this.id, AL11.AL_PITCH, value);
	}
	// endregion

}
