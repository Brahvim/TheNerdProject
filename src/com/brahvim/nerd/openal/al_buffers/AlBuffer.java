package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlNativeResource;
import com.brahvim.nerd.openal.NerdAl;

import processing.core.PVector;

public abstract class AlBuffer<BufferT extends Buffer> extends AlNativeResource {

	// region Fields.
	public static final ArrayList<AlBuffer<?>> ALL_INSTANCES = new ArrayList<>();

	// No OpenAL implementation provides `AL_DATA`.
	// Storing it here!
	protected BufferT data;
	protected NerdAl alMan;
	protected int id, alFormat;
	// endregion

	// region Constructors.
	public AlBuffer(NerdAl p_alMan) {
		AlBuffer.ALL_INSTANCES.add(this);
		this.alMan = p_alMan;

		this.id = AL11.alGenBuffers();
		this.alMan.checkAlError();
	}

	@SuppressWarnings("unchecked")
	public AlBuffer(AlBuffer<?> p_buffer) {
		AlBuffer.ALL_INSTANCES.add(this);

		this.alMan = p_buffer.alMan;
		this.id = AL11.alGenBuffers();
		this.alFormat = p_buffer.alFormat;

		this.setBits(p_buffer.getBits());
		this.setChannels(p_buffer.getChannels());
		this.setDataImpl(p_buffer.alFormat, (BufferT) p_buffer.getData(), p_buffer.getSampleRate());

		this.alMan.checkAlError();
	}

	public AlBuffer(NerdAl p_alMan, int p_id) {
		AlBuffer.ALL_INSTANCES.add(this);

		this.id = p_id;
		this.alMan = p_alMan;
	}

	public AlBuffer(NerdAl p_alInst, BufferT p_data) {
		AlBuffer.ALL_INSTANCES.add(this);
		this.alMan = p_alInst;

		this.id = AL11.alGenBuffers();
		this.alMan.checkAlError();
	}
	// endregion

	// region Instance collection queries.
	public static int getNumInstances() {
		return AlBuffer.ALL_INSTANCES.size();
	}

	public static ArrayList<AlBuffer<?>> getAllInstances() {
		return new ArrayList<>(AlBuffer.ALL_INSTANCES);
	}
	// endregion

	// region `abstract` methods (and overloads, with their implementations).
	public AlBuffer<?> loadFrom(String p_path) {
		this.loadFrom(new File(p_path)); // Also invoke `AlNativeResource::shouldDispose()`.
		return this;
	}

	public AlBuffer<?> loadFrom(File p_file) {
		super.shouldDispose(false);
		this.loadFromImpl(p_file);
		return this;
	}

	protected abstract AlBuffer<?> loadFromImpl(File p_file);

	public void setData(int p_format, BufferT p_buffer, int p_sampleRate) {
		this.data = p_buffer;
		this.alFormat = p_format;
		this.setDataImpl(p_format, p_buffer, p_sampleRate);
		this.alMan.checkAlError();
	}

	protected abstract void setDataImpl(int p_format, BufferT p_buffer, int p_sampleRate);
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		return AL11.alGetBufferi(this.id, p_alEnum);
	}

	public float getFloat(int p_alEnum) {
		return AL11.alGetBufferf(this.id, p_alEnum);
	}

	// Vectors in OpenAL are not large and can be allocated on the stack just fine.
	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(p_vecSize);
		AL11.alGetBufferiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(p_vecSize);
		AL11.alGetBufferfv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
	}

	public int[] getIntTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer intBuffer = MemoryStack.stackMallocInt(3);
		AL11.alGetBufferiv(this.id, p_alEnum, intBuffer);
		MemoryStack.stackPop();

		return intBuffer.array();
	}

	public /* `float[]` */ float[] getFloatTriplet(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer floatBuffer = MemoryStack.stackMallocFloat(3);
		AL11.alGetBufferfv(this.id, p_alEnum, floatBuffer);
		MemoryStack.stackPop();

		return floatBuffer.array();
		// return new PVector(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		AL11.alBufferi(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alBufferf(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		AL11.alBufferiv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		AL11.alBufferfv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}

	public void setIntTriplet(int p_alEnum, int... p_values) {
		if (p_values.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3i(this.id, p_alEnum, p_values[0], p_values[1], p_values[2]);
		this.alMan.checkAlError();
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alBuffer3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
		this.alMan.checkAlError();
	}

	public void setFloatTriplet(int p_alEnum, float... p_values) {
		if (p_values.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3f(this.id, p_alEnum, p_values[0], p_values[1], p_values[2]);
		this.alMan.checkAlError();
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alBuffer3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
		this.alMan.checkAlError();
	}

	public void setFloatTriplet(int p_alEnum, PVector p_values) {
		AL11.alBuffer3f(this.id, p_alEnum, p_values.x, p_values.y, p_values.z);
		this.alMan.checkAlError();
	}
	// endregion

	// region Getters.
	public int getId() {
		return this.id;
	}

	public int getSize() {
		return this.getInt(AL11.AL_SIZE);
	}

	public int getBits() {
		return this.getInt(AL11.AL_BITS);
	}

	public int getChannels() {
		return this.getInt(AL11.AL_CHANNELS);
	}

	public BufferT getData() {
		return this.data;
	}

	public int getSampleRate() {
		return this.getInt(ALC11.ALC_FREQUENCY);
	}
	// endregion

	// region Setters.
	public AlBuffer<BufferT> setBits(int p_bits) {
		AL11.alBufferi(this.id, AL11.AL_BITS, p_bits);
		return this;
	}

	public AlBuffer<BufferT> setSampleRate(int p_sampleRate) {
		AL11.alBufferi(this.id, AL11.AL_FREQUENCY, p_sampleRate);
		return this;
	}

	public AlBuffer<BufferT> setChannels(int p_channels) {
		AL11.alBufferi(this.id, AL11.AL_CHANNELS, p_channels);
		return this;
	}
	// endregion

	@Override
	protected void disposeImpl() {
		AL11.alDeleteBuffers(this.id);
		this.alMan.checkAlError();
		AlBuffer.ALL_INSTANCES.remove(this);
	}

}
