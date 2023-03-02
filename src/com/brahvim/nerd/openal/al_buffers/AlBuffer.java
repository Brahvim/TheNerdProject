package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlResourceHolder;
import com.brahvim.nerd.openal.NerdAl;

import processing.core.PVector;

public abstract class AlBuffer<BufferT extends Buffer> extends AlResourceHolder {

	// region Fields.
	// No OpenAL implementation provides `AL_DATA`.
	// Storing it here!
	protected BufferT data;
	protected NerdAl alMan;
	protected int id, dataType;
	// endregion

	// region Constructors.
	public AlBuffer(NerdAl p_alMan) {
		this.alMan = p_alMan;

		this.id = AL11.alGenBuffers();
		this.alMan.checkAlErrors();
	}

	@SuppressWarnings("unchecked")
	protected AlBuffer(AlBuffer<?> p_buffer) {
		this.alMan = p_buffer.alMan;
		this.id = AL11.alGenBuffers();
		this.dataType = p_buffer.dataType;

		this.setBits(p_buffer.getBits());
		this.setChannels(p_buffer.getChannels());
		this.setData(p_buffer.dataType, (BufferT) p_buffer.getData(), p_buffer.getSampleRate());

		this.alMan.checkAlErrors();
	}

	public AlBuffer(NerdAl p_alMan, int p_id) {
		this.id = p_id;
		this.alMan = p_alMan;
	}

	public AlBuffer(NerdAl p_alInst, BufferT p_data) {
		this.alMan = p_alInst;

		this.id = AL11.alGenBuffers();
		this.alMan.checkAlErrors();
	}
	// endregion

	// region `abstract` methods (and overloads).
	public AlBuffer<?> loadFrom(String p_path) {
		this.loadFrom(new File(p_path));
		return this;
	}

	public abstract AlBuffer<?> loadFrom(File p_file);

	public abstract void setData(int p_format, BufferT p_buffer, int p_sampleRate);
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
		this.alMan.checkAlErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		AL11.alBufferf(this.id, p_alEnum, p_value);
		this.alMan.checkAlErrors();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		AL11.alBufferiv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		AL11.alBufferfv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int... p_values) {
		if (p_values.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setIntTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3i(this.id, p_alEnum, p_values[0], p_values[1], p_values[2]);
		this.alMan.checkAlErrors();
	}

	public void setIntTriplet(int p_alEnum, int p_i1, int p_i2, int p_i3) {
		AL11.alBuffer3i(this.id, p_alEnum, p_i1, p_i2, p_i3);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float... p_values) {
		if (p_values.length != 3)
			throw new IllegalArgumentException(
					"`alBuffer::setFloatTriplet()` cannot take an array of size other than `3`!");

		AL11.alBuffer3f(this.id, p_alEnum, p_values[0], p_values[1], p_values[2]);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, float p_f1, float p_f2, float p_f3) {
		AL11.alBuffer3f(this.id, p_alEnum, p_f1, p_f2, p_f3);
		this.alMan.checkAlErrors();
	}

	public void setFloatTriplet(int p_alEnum, PVector p_values) {
		AL11.alBuffer3f(this.id, p_alEnum, p_values.x, p_values.y, p_values.z);
		this.alMan.checkAlErrors();
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

	// Older `setData()` overloads. No longer used due to problems with generics!:
	/*
	 * public void setData(int p_format, float[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, FloatBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, byte[] p_data, int p_sampleRate) {
	 * this.setData(p_dataType, ByteBuffer.wrap(p_data), p_sampleRate);
	 * }
	 * 
	 * public void setData(int p_format, ByteBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, short[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, ShortBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, int[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_format, IntBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 */
	// endregion

	@Override
	public void dispose() {
		AL11.alDeleteBuffers(this.id);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

}
