package com.brahvim.nerd.openal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Objects;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;

import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.openal.al_exceptions.AlcException;

/**
 * @deprecated I really can't see how one records audio with OpenAL.
 */
@Deprecated
public class AlCapture extends AlNativeResource {
	// Y'know what?
	// Using different OpenAL contexts probably doesn't matter here.

	// region Fields.
	protected static final ArrayList<AlCapture> ALL_INSTANCES = new ArrayList<>();

	private static int numInstances;

	// This is here literally just for naming threads!:
	private static volatile int numActiveInstances;

	private long id;
	private final NerdAl alMan;
	private String deviceName;
	private Thread captureThread;
	private ByteBuffer capturedData = ByteBuffer.allocate(0);

	// Last capture info:
	private int lastCapSampleRate = -1, lastCapFormat = -1;
	// endregion

	// region Constructors.
	public AlCapture(final NerdAl p_alMan) {
		this(p_alMan, AlCapture.getDefaultDeviceName());
		AlCapture.ALL_INSTANCES.add(this);
	}

	public AlCapture(final NerdAl p_alMan, final String p_deviceName) {
		this.alMan = p_alMan;
		AlCapture.ALL_INSTANCES.add(this);
	}
	// endregion

	// region Instance management.
	public static ArrayList<AlCapture> getAllInstances() {
		return new ArrayList<>(AlCapture.ALL_INSTANCES);
	}

	public static int getNumInstances() {
		return AlCapture.numInstances;
	}

	public static int getNumInstancesCurrentlyCapturing() {
		return AlCapture.numActiveInstances;
	}
	// endregion

	// region Capture queries.
	public static String getDefaultDeviceName() {
		return ALC11.alcGetString(0, ALC11.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
	}

	// region `startCapturing()` overloads.
	public void startCapturing() {
		this.startCapturing(44100, AL11.AL_FORMAT_MONO8, 1024);
	}

	public void startCapturing(final int p_format) {
		this.startCapturing(44100, p_format, 1024);
	}

	public void startCapturing(final int p_sampleRate, final int p_format) {
		this.startCapturing(p_sampleRate, p_format, p_format);
	}

	public void startCapturing(final int p_sampleRate, final int p_format, final int p_samplesPerBuffer) {
		if (this.isCapturing()) {
			System.err.println("OpenAL cannot start capturing whilst already doing it!");
			return;
		}

		AlCapture.numActiveInstances++;

		// region Preparing to capture.
		// Store the last ones.
		this.lastCapFormat = p_format;
		this.lastCapSampleRate = p_sampleRate;

		// Open the capture device,
		this.id = ALC11.alcCaptureOpenDevice(this.deviceName, p_sampleRate, p_format, p_samplesPerBuffer);
		this.alMan.checkAlcError();

		// Begin capturing!:
		ALC11.alcCaptureStart(this.id);
		this.alMan.checkAlcError();
		// endregion

		this.captureThread = new Thread(() -> {
			boolean deviceGotRemoved = false;
			ByteBuffer dataCaptured = ByteBuffer.allocate(0);

			// Capture till `stopCapturing()` is called:
			while (!Thread.interrupted()) {
				this.alMan.checkAlcError();

				final ByteBuffer SAMPLES_BUFFER = ByteBuffer.allocate(p_samplesPerBuffer);

				for (int i = 0; i < p_samplesPerBuffer; i = ALC11.alcGetInteger(
						this.id, ALC11.ALC_CAPTURE_SAMPLES)) {
					System.out.printf("Captured `%d` samples.\n", i);
				}

				ALC11.alcCaptureSamples(this.id, SAMPLES_BUFFER, p_samplesPerBuffer);

				// region Check if the device gets disconnected (cause of `ALC_INVALID_DEVICE`):
				try {
					this.alMan.checkAlcError();
				} catch (final AlcException e) {
					deviceGotRemoved = true;
					System.err.printf("""
							Audio capture device on thread \"%s\" has been disconnected amidst a session.
							Recording has stopped.""", this.captureThread.getName());
					this.captureThread.interrupt();
				}
				// endregion

				// Store the old data away:
				final byte[] oldData = dataCaptured.array();
				dataCaptured = ByteBuffer.allocate(oldData.length + p_samplesPerBuffer);
				dataCaptured.put(oldData);
				dataCaptured.put(SAMPLES_BUFFER);
			}

			// When interrupted, stop capturing:
			synchronized (this) {
				if (deviceGotRemoved)
					ALC11.alcCaptureStop(this.id);
				this.capturedData = dataCaptured;
			}
		});

		this.captureThread.setName("OpenAL capture thread #" + AlCapture.numActiveInstances);
		this.captureThread.start();
	}
	// endregion

	public Thread getCaptureThread() {
		return this.captureThread;
	}

	public boolean isCapturing() {
		return this.captureThread == null ? false : this.captureThread.isAlive();
	}

	public AlWavBuffer stopCapturing(final AlWavBuffer p_buffer) {
		this.stopCapturing();
		this.storeIntoBuffer(p_buffer);
		return p_buffer;
	}

	public ByteBuffer stopCapturing() {
		if (this.captureThread == null)
			return this.capturedData;
		else if (!this.captureThread.isAlive())
			return this.capturedData;

		this.captureThread.interrupt();

		ALC11.alcCaptureCloseDevice(this.id);
		this.alMan.checkAlcError();

		AlCapture.numActiveInstances--;
		return this.capturedData;
	}
	// endregion

	// region Using the captured data.
	public ByteBuffer getCapturedData() {
		return this.capturedData;
	}

	public ByteBuffer storeIntoBuffer(final AlWavBuffer p_buffer) {
		Objects.requireNonNull(p_buffer,
				"`AlCapture::storeIntoBuffer(AlWavBuffer)` cannot use a `null` buffer.");

		if (this.lastCapFormat == -1 || this.lastCapSampleRate == -1)
			return this.capturedData;

		p_buffer.setData(this.lastCapFormat,
				this.capturedData.order(ByteOrder.nativeOrder()).asIntBuffer(),
				this.lastCapSampleRate);
		return this.capturedData;
	}

	public AlWavBuffer storeIntoBuffer() {
		final AlWavBuffer toRet = new AlWavBuffer(this.alMan);
		toRet.setData(this.lastCapFormat,
				this.capturedData.order(ByteOrder.nativeOrder()).asIntBuffer(),
				this.lastCapSampleRate);
		return toRet;
	}
	// endregion

	@Override
	protected void disposeImpl() {
		ALC11.alcCaptureCloseDevice(this.id);
		AlCapture.ALL_INSTANCES.remove(this);
	}

}
