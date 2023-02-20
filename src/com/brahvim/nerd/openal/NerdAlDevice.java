package com.brahvim.nerd.openal;

import java.util.List;

import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;

public class NerdAlDevice {

	public interface DisconnectionCallback {
		public default String onDisconnect() {
			return NerdAl.getDefaultDeviceName();
		}
	}

	private long id;
	private NerdAl manager;
	private NerdAlDevice.DisconnectionCallback disconnectionCallback = new NerdAlDevice.DisconnectionCallback() {

	};

	// region Constructors.
	public NerdAlDevice(NerdAl p_manager) {
		this.manager = p_manager;
		this(NerdAlDevice.getDefaultDeviceName())
	}

	public NerdAlDevice(String p_deviceName) {

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

	public void setDisconnectionCallback(NerdAlDevice.DisconnectionCallback p_callback) {
		this.disconnectionCallback = p_callback;
	}

	public long getId() {
		return this.id;
	}

	public void dispose() {
		ALC11.alcCloseDevice(this.dvId);
		this.checkAlcErrors();
		this.dvId = 0;
	}

}
