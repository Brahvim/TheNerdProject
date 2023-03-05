package com.brahvim.nerd.openal;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTDisconnect;
import org.lwjgl.openal.SOFTReopenDevice;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_exceptions.AlcException;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlDevice extends AlNativeResource {

	// region Fields.
	protected final static ArrayList<AlDevice> ALL_INSTANCES = new ArrayList<>();

	private long id;
	private String name;
	private NerdAl alMan;
	private Supplier<String> disconnectionCallback = () -> AlDevice.getDefaultDeviceName();
	// endregion

	// region Constructors.
	public AlDevice(NerdAl p_manager) {
		this(p_manager, AlDevice.getDefaultDeviceName());
	}

	public AlDevice(NerdAl p_manager, String p_deviceName) {
		AlDevice.ALL_INSTANCES.add(this);

		this.alMan = p_manager;
		this.name = p_deviceName;
		this.id = ALC11.alcOpenDevice(this.name);

		// Check for errors:
		int alcError = ALC11.alcGetError(this.id);
		if (alcError != 0)
			throw new AlcException(this.id, alcError);
	}
	// endregion

	// region `static` methods.
	// region Instance collection queries.
	public static int getNumInstances() {
		return AlDevice.ALL_INSTANCES.size();
	}

	public static ArrayList<AlDevice> getAllInstances() {
		return new ArrayList<>(AlDevice.ALL_INSTANCES);
	}
	// endregion

	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	}

	public static List<String> getDevices() {
		return ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
	}
	// endregion

	// region Connection status.
	public void setDisconnectionCallback(Supplier<String> p_callback) {
		this.disconnectionCallback = p_callback;
	}

	// It's fine, let this be here:
	public void disconnectionCheck() {
		if (!this.isConnected())
			this.changeEndpoint(this.disconnectionCallback.get());
	}

	public void changeEndpoint(String p_dvName) {
		if (!SOFTReopenDevice.alcReopenDeviceSOFT(
				this.id, p_dvName, new int[] { 0 }))
			throw new NerdAlException("`SOFTReopenDevice` failed.");
		this.name = p_dvName;
	}

	// This uses device handles and not device names. Thus, no `static` version.
	public boolean isConnected() {
		// No idea why this bad stack read works.
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1); // Stack allocation, "should" be "faster".
		ALC11.alcGetIntegerv(this.id, EXTDisconnect.ALC_CONNECTED, buffer);
		MemoryStack.stackPop();

		return buffer.get() == 1;
	}
	// endregion

	// region Getters (and the `isDefault()` query).
	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public NerdAl getAlMan() {
		return this.alMan;
	}

	// I don't want to hold a `boolean` for this...
	public boolean isDefault() {
		return this.name.equals(AlDevice.getDefaultDeviceName());
	}
	// endregion

	@Override
	protected void disposeImpl() {
		if (!ALC11.alcCloseDevice(this.id))
			throw new NerdAlException("Could not close OpenAL device!");

		this.id = 0;
		// this.alMan.checkAlcErrors();
		AlDevice.ALL_INSTANCES.remove(this);
	}

}
