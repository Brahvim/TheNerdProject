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
	protected static final ArrayList<AlFilter> ALL_INSTANCES = new ArrayList<>();

	private final int id;
	private final NerdAl alMan;
	// endregion

	public AlFilter(final NerdAl p_alMan) {
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
	public int getInt(final int p_alEnum) {
		MemoryStack.stackPush();
		final IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetFilteri(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public int[] getIntVector(final int p_alEnum, final int p_vecSize) {
		MemoryStack.stackPush();
		final IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetFilteriv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}

	public float getFloat(final int p_alEnum) {
		MemoryStack.stackPush();
		final FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetFilterf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public float[] getFloatVector(final int p_alEnum, final int p_vecSize) {
		MemoryStack.stackPush();
		final FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetFilterfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(final int p_alEnum, final int p_value) {
		EXTEfx.alFilteri(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setIntVector(final int p_alEnum, final int... p_values) {
		EXTEfx.alFilteriv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}

	public void setFloat(final int p_alEnum, final float p_value) {
		EXTEfx.alFilterf(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setFloatVector(final int p_alEnum, final float... p_values) {
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
	public void attachToSources(final int p_filterType, final AlSource... p_sources) {
		if (p_filterType == EXTEfx.AL_DIRECT_FILTER)
			for (final AlSource s : p_sources) {
				if (s != null)
					s.attachDirectFilter(this);
			}
		else
			for (final AlSource s : p_sources) {
				if (s != null)
					s.attachAuxiliarySendFilter(this);
			}
	}

	public void detachFromSources(final int p_filterType, final AlSource... p_sources) {
		if (p_filterType == EXTEfx.AL_DIRECT_FILTER)
			for (final AlSource s : p_sources) {
				if (s != null)
					s.detachDirectFilter();
			}
		else
			for (final AlSource s : p_sources) {
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
