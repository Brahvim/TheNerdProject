package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.function.Consumer;

import processing.core.PApplet;

public class NerdTcpClient extends NerdAbstractTcpClient {

	// region Fields.
	private final HashSet<Consumer<NerdReceivableTcpPacket>> MESSAGE_CALLBACKS = new HashSet<>();

	private Thread commsThread;
	private boolean inMessageLoop;
	// endregion

	// region Construction.
	public NerdTcpClient(final Socket p_socket) {
		super(p_socket);
	}

	public NerdTcpClient(final Socket p_socket,
			final Consumer<NerdReceivableTcpPacket> p_mesageCallback) {
		this(p_socket);
		this.startMessageThread(p_mesageCallback);
	}

	public NerdTcpClient(final String p_serverIp, final int p_myPort) {
		super(p_serverIp, p_myPort);
	}

	public NerdTcpClient(final String p_serverIp, final int p_myPort,
			final Consumer<NerdReceivableTcpPacket> p_mesageCallback) {
		this(p_serverIp, p_myPort);
		this.startMessageThread(p_mesageCallback);
	}

	public NerdTcpClient(final int p_port) {
		super(p_port);
	}

	public NerdTcpClient(final int p_port,
			final Consumer<NerdReceivableTcpPacket> p_mesageCallback) {
		this(p_port);
		this.startMessageThread(p_mesageCallback);
	}

	private void startMessageThread(final Consumer<NerdReceivableTcpPacket> p_mesageCallback) {
		if (this.inMessageLoop)
			return;

		this.inMessageLoop = true;
		this.MESSAGE_CALLBACKS.add(p_mesageCallback);

		this.commsThread = new Thread(() -> {
		});

		this.commsThread.setName("NerdTcpServerListenerOnPort" + this.socket.getLocalPort());
		this.commsThread.setDaemon(true);
		// this.commsThread.start();
	}
	// endregion

	// region Sending stuff.
	@Override
	public NerdTcpClient send(final NerdAbstractTcpPacket p_packet) {
		try {
			final byte[] packData = p_packet.getData();
			final byte[] packDataLen = ByteBuffer
					.allocate(Integer.BYTES).putInt(p_packet.getDataLength()).array();

			// System.out.println("`NerdTcpClient::send()` was called.");
			super.socket.getOutputStream().write(PApplet.concat(packDataLen, packData));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public NerdTcpClient send(final String p_data) {
		return this.send(p_data.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public NerdTcpClient send(final byte[] p_data) {
		return this.send(new NerdSendableTcpPacket(p_data));
	}
	// endregion

}
