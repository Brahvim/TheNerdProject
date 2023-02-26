package com.brahvim.nerd.openal.al_capture;

import java.nio.ByteBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.NerdAl;

public class AlCapture {
	// region Fields.
	private volatile static int numActiveCaptures;

	private int id;
	private NerdAl alMan;
	private String deviceName;
	private Thread captureThread;
	private ByteBuffer captureData;
	// endregion

	// region Constructors.
	public AlCapture(NerdAl p_alMan) {
		this(p_alMan, AlCapture.getDefaultDeviceName());
	}

	public AlCapture(NerdAl p_alMan, String p_deviceName) {
		this.alMan = p_alMan;
	}
	// endregion

	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
	}

	public Thread getCaptureThread() {
		return this.captureThread;
	}

	// region `startCapturing()` overloads.
	public void startCapturing() {
		this.startCapturing(44100, AL11.AL_FORMAT_MONO8, 1024);
	}

	public void startCapturing(int p_format) {
		this.startCapturing(44100, p_format, 1024);
	}

	public void startCapturing(int p_sampleRate, int p_format) {
		this.startCapturing(p_sampleRate, p_format, p_format);
	}

	public void startCapturing(int p_sampleRate, int p_format, int p_samplesPerBuffer) {
		AlCapture.numActiveCaptures++;

		this.captureData = ByteBuffer.allocate(p_samplesPerBuffer);
		ALC11.alcCaptureOpenDevice(this.deviceName, p_sampleRate, p_format, p_samplesPerBuffer);

		this.captureThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				synchronized (this.captureData) {
					ALC11.alcCaptureSamples(this.id, this.captureData, p_samplesPerBuffer);
				}
			}
		});

		this.captureThread.setName("OpenAL recording thread #" + AlCapture.numActiveCaptures);
		this.captureThread.start();
	}
	// endregion

	public ByteBuffer stopCapturing() {
		this.captureThread.interrupt();

		ALC11.alcCaptureCloseDevice(this.id);

		AlCapture.numActiveCaptures--;
		return this.captureData;
	}

}
