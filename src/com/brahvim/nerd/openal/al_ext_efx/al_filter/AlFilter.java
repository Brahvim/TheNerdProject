package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.NerdAl;

/* `package` */ class AlFilter {

	private int id;
	private NerdAl alMan;

	public AlFilter(NerdAl p_alMan) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenFilters();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	// public AlFilter(AlFilter p_filter) { }

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetFilteri(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetFilteriv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetFilterf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetFilterfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		EXTEfx.alFilteri(this.id, p_alEnum, p_value);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		EXTEfx.alFilteriv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		EXTEfx.alFilterf(this.id, p_alEnum, p_value);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		EXTEfx.alFilterfv(this.id, p_alEnum, p_values);
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}
	// endregion

	public int getId() {
		return this.id;
	}

	// region OpenAL Setters.

	// endregion

	public void dispose() {
		EXTEfx.alDeleteFilters(this.id);
	}

}
