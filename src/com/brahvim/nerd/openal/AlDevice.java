package com.brahvim.nerd.openal;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTDisconnect;
import org.lwjgl.system.MemoryStack;

public class AlDevice {

	public interface DisconnectionCallback {
		public default String onDisconnect() {
			return AlDevice.getDefaultDeviceName();
		}
	}

	// region Fields.
	private long id;
	private String name;
	private NerdAl manager;
	private boolean isDefaultDevice = true;
	private AlDevice.DisconnectionCallback disconnectionCallback
	// // // // // // // // // // // // // // // // // // // // // //
			= new AlDevice.DisconnectionCallback() {
			};
	// endregion

	// region Constructors.
	public AlDevice(NerdAl p_manager) {
		this(p_manager, AlDevice.getDefaultDeviceName());
	}

	public AlDevice(NerdAl p_manager, String p_deviceName) {
		this.name = p_deviceName;
		this.manager = p_manager;
		this.isDefaultDevice = p_deviceName.equals(AlDevice.getDefaultDeviceName());

		this.id = ALC11.alcOpenDevice(this.name);
	}
	// endregion

	// region `static` methods.
	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
	}

	public static List<String> getDevices() {
		return ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
	}
	// endregion

	public void setDisconnectionCallback(AlDevice.DisconnectionCallback p_callback) {
		this.disconnectionCallback = p_callback;
	}

	public boolean disconnectionCheck() {
		boolean connected = this.isConnected();

		if (!connected)
			this.manager.createAl(this.disconnectionCallback.onDisconnect());

		return connected;
	}

	// region Getters.
	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public NerdAl getManager() {
		return this.manager;
	}
	// endregion

	public void dispose() {
		ALC11.alcCloseDevice(this.id);
		this.manager.checkAlcErrors();
		this.id = 0;
	}

	public boolean isDefault() {
		return this.isDefaultDevice;
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

}
