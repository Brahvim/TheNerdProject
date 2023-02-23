package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.NerdAl;

import processing.core.PVector;

public abstract class AlBuffer<BufferT extends Buffer> {

	// region Fields.
	protected int id;
	protected NerdAl manager;

	// LWJGL does not provide `AL_DATA` anywhere, :/
	// Storing it here for access.
	protected BufferT data;

	private final static ArrayList<AlBuffer<?>> buffers = new ArrayList<>();
	// endregion

	// region Constructors.
	public AlBuffer(NerdAl p_alInst) {
		this.manager = p_alInst;

		this.id = AL11.alGenBuffers();
		this.manager.checkAlErrors();
	}

	public AlBuffer(NerdAl p_alInst, BufferT p_data) {
		this.manager = p_alInst;

		this.id = AL11.alGenBuffers();
		this.manager.checkAlErrors();
	}
	// endregion

	@SuppressWarnings("unchecked")
	public static ArrayList<AlBuffer<?>> getEveryBufferEver() {
		return (ArrayList<AlBuffer<?>>) AlBuffer.buffers.clone();
	}

	// region `abstract` methods.
	public abstract void setData(int p_dataType, BufferT p_buffer, int p_sampleRate);

	public abstract AlBuffer<?> loadFrom(File p_file);
	// endregion

	public AlBuffer<?> loadFrom(String p_path) {
		this.loadFrom(new File(p_path));
		return this;
	}

	// region C-style AL-API getters.
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

	// region C-style AL-API setters.
	public void setInt(int p_alEnum, int p_value) {
		AL11.alBufferi(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alBufferf(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setIntVector(int p_alEnum, int... p_value) {
		AL11.alBufferiv(this.id, p_alEnum, p_value);
		this.manager.checkAlErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		AL11.alBufferfv(this.id, p_alEnum, p_values);
		this.manager.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3i(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.manager.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alBuffer3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float[] p_value) {
		if (p_value.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3f(this.id, p_alEnum, p_value[0], p_value[1], p_value[2]);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alBuffer3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
		this.manager.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, PVector p_value) {
		AL11.alBuffer3f(this.id, p_alEnum, p_value.x, p_value.y, p_value.z);
		this.manager.checkAlErrors();
	}
	// endregion

	// region Getters.
	public int getId() {
		return this.id;
	}

	public int getSize() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_SIZE, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public int getBits() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_BITS, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public int getChannels() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_CHANNELS, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public BufferT getData() {
		return this.data;
	}

	public int getSampleRate() {
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);
		AL11.alBufferiv(this.id, ALC11.ALC_FREQUENCY, sampleRateBuffer);
		MemoryStack.stackPop();

		return sampleRateBuffer.get();
	}
	// endregion

	// region Setters.

	public void setBits(int p_bits) {
		AL11.alBufferi(this.id, AL11.AL_BITS, p_bits);
	}

	public void setSampleRate(int p_sampleRate) {
		AL11.alBufferi(this.id, AL11.AL_FREQUENCY, p_sampleRate);
	}

	public void setChannels(int p_channels) {
		AL11.alBufferi(this.id, AL11.AL_CHANNELS, p_channels);
	}

	// Older `setData()` overloads. No longer used thanks to generics!:
	/*
	 * public void setData(int p_dataType, float[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, FloatBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, byte[] p_data, int p_sampleRate) {
	 * this.setData(p_dataType, ByteBuffer.wrap(p_data), p_sampleRate);
	 * }
	 * 
	 * public void setData(int p_dataType, ByteBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, short[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, ShortBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, int[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, IntBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 */

	// endregion

	public void dispose() {
		this.manager.getDeviceBuffers().remove(this);
		AlBuffer.buffers.remove(this);
		AL11.alDeleteBuffers(this.id);
		this.manager.checkAlErrors();
	}

}
