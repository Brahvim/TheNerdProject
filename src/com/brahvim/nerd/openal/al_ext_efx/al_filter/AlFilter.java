package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlNativeResource;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.NerdAl;

public abstract class AlFilter extends AlNativeResource {

	// region Fields.
	protected final static ArrayList<AlFilter> ALL_INSTANCES = new ArrayList<>();

	private int id;
	private NerdAl alMan;
	// endregion

	public AlFilter(NerdAl p_alMan) {
		AlFilter.ALL_INSTANCES.add(this);

		this.alMan = p_alMan;
		this.id = EXTEfx.alGenFilters();
		this.alMan.checkAlError();

		this.setInt(EXTEfx.AL_FILTER_TYPE, this.getName());
		this.alMan.checkAlError();
	}

	// region Instance collection queries.
	public static int getNumInstances() {
		return AlFilter.ALL_INSTANCES.size();
	}

	public static ArrayList<AlFilter> getAllInstances() {
		return new ArrayList<>(AlFilter.ALL_INSTANCES);
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetFilteri(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetFilteriv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetFilterf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetFilterfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		EXTEfx.alFilteri(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		EXTEfx.alFilteriv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}

	public void setFloat(int p_alEnum, float p_value) {
		EXTEfx.alFilterf(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		EXTEfx.alFilterfv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}
	// endregion

	// region Getters.
	public int getId() {
		return this.id;
	}

	public abstract int getName();
	// endregion

	// region Mass source attachment.
	public void attachToSources(int p_filterType, AlSource... p_sources) {
		if (p_filterType == EXTEfx.AL_DIRECT_FILTER)
			for (AlSource s : p_sources) {
				if (s != null)
					s.attachDirectFilter(this);
			}
		else
			for (AlSource s : p_sources) {
				if (s != null)
					s.attachAuxiliarySendFilter(this);
			}
	}

	public void detachFromSources(int p_filterType, AlSource... p_sources) {
		if (p_filterType == EXTEfx.AL_DIRECT_FILTER)
			for (AlSource s : p_sources) {
				if (s != null)
					s.detachDirectFilter();
			}
		else
			for (AlSource s : p_sources) {
				if (s != null)
					s.detachAuxiliarySendFilter();
			}
	}
	// endregion

	@Override
	protected void disposeImpl() {
		EXTEfx.alDeleteFilters(this.id);
		AlFilter.ALL_INSTANCES.remove(this);
	}

}
