package com.brahvim.nerd.openal;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTDisconnect;
import org.lwjgl.openal.SOFTReopenDevice;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_exceptions.NerdAlException;

public class AlDevice  {

	public interface DisconnectionCallback {
		public default String onDisconnect() {
			return AlDevice.getDefaultDeviceName();
		}
	}

	// region Fields.
	private long id;
	private String name;
	private NerdAl alMan;
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
		this.alMan = p_manager;
		this.name = p_deviceName;
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

	// region Connection status.
	public void setDisconnectionCallback(AlDevice.DisconnectionCallback p_callback) {
		this.disconnectionCallback = p_callback;
	}

	public boolean disconnectionCheck() {
		boolean connected = this.isConnected();

		if (!connected) {
			SOFTReopenDevice.alcReopenDeviceSOFT(
					this.id,
					this.disconnectionCallback.onDisconnect(),
					new int[] { 0 });
		}

		return connected;
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

	// region Getters.
	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public NerdAl getAlMan() {
		return this.alMan;
	}
	// endregion

	public void dispose() {
		if (!ALC11.alcCloseDevice(this.id))
			throw new NerdAlException("Could not close OpenAL device!");

		// this.alMan.checkAlErrors();
		// this.alMan.checkAlcErrors();
		this.id = 0;
	}

	public boolean isDefault() {
		return this.isDefaultDevice;
	}

}
