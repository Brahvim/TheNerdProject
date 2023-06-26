package com.brahvim.nerd.io.net.tcp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.function.Consumer;

import com.brahvim.nerd.io.net.NerdSocket;

import processing.core.PApplet;

public class NerdTcpClient extends NerdAbstractTcpClient implements NerdSocket {

	public class NerdServerSentTcpPacket extends NerdAbstractTcpPacket {

		public NerdServerSentTcpPacket(final byte[] p_data) {
			super(p_data);
		}

		public NerdTcpClient getReceivingClient() {
			return NerdTcpClient.this;
		}

	}

	// region Fields.
	private final Vector<Consumer<NerdTcpClient.NerdServerSentTcpPacket>> MESSAGE_CALLBACKS = new Vector<>(1);

	private Thread commsThread;
	private boolean inMessageLoop;
	// endregion

	// region Construction.
	// region Constructors.
	public NerdTcpClient(final Socket p_socket) {
		super(p_socket);
	}

	public NerdTcpClient(
			final Socket p_socket,
			final Consumer<NerdTcpClient.NerdServerSentTcpPacket> p_mesageCallback) {
		this(p_socket);
		this.startMessageThread(p_mesageCallback);
	}

	public NerdTcpClient(final String p_serverIp, final int p_myPort) {
		super(p_serverIp, p_myPort);
	}

	public NerdTcpClient(final String p_serverIp, final int p_myPort,
			final Consumer<NerdTcpClient.NerdServerSentTcpPacket> p_mesageCallback) {
		this(p_serverIp, p_myPort);
		this.startMessageThread(p_mesageCallback);
	}

	public NerdTcpClient(final int p_port) {
		super(p_port);
	}

	public NerdTcpClient(final int p_port,
			final Consumer<NerdTcpClient.NerdServerSentTcpPacket> p_mesageCallback) {
		this(p_port);
		this.startMessageThread(p_mesageCallback);
	}
	// endregion

	private void startMessageThread(final Consumer<NerdTcpClient.NerdServerSentTcpPacket> p_mesageCallback) {
		if (this.inMessageLoop)
			return;

		if (super.hasDisconnected)
			return;

		this.inMessageLoop = true;
		this.MESSAGE_CALLBACKS.add(p_mesageCallback);
		this.commsThread = new Thread(() -> {
			// No worries - the same stream is used till the socket shuts down.
			DataInputStream stream = null;

			// ...Get that stream!:
			try {
				stream = new DataInputStream(this.socket.getInputStream());
			} catch (final IOException e) {
				e.printStackTrace();
			}

			while (true)
				try {
					if (this.commsThread.isInterrupted())
						return;

					stream.available();
					// ^^^ This is literally gunna return `0`!
					// ..I guess we use fixed sizes around here...

					// ..Now read it:
					final int packetSize = stream.readInt();
					final byte[] packetData = new byte[packetSize];
					stream.read(packetData); // It needs to know the length of the array!
					final var packet = new NerdTcpClient.NerdServerSentTcpPacket(packetData);

					// The benefit of having a type like `ReceivableTcpPacket` *is* that I won't
					// have to reconstruct it every time, fearing that one of these callbacks might
					// change the contents of the packet.

					synchronized (this.MESSAGE_CALLBACKS) {
						for (final var c : this.MESSAGE_CALLBACKS)
							try {
								// System.out.println("""
								// `NerdTcpClient::serverCommThread::run()` \
								// called a message callback.""");
								c.accept(packet);
							} catch (final Exception e) {
								e.printStackTrace();
							}
					}
				} catch (final IOException e) {
					// When the client disconnects, this exception is thrown by
					// `*InputStream::read*()`:
					if (e instanceof EOFException)
						this.disconnect();
					else if (e instanceof SocketException)
						this.disconnect();
					// e.printStackTrace(); // I have NO idea what to do!
					else
						e.printStackTrace();
				}
		}, "NerdTcpServerListenerOnPort" + this.socket.getLocalPort());
		// ^^^ It's faster to give the thread a name in this manner.

		this.commsThread.setDaemon(true);
		this.commsThread.start();

	}
	// endregion

	@Override
	protected void disconnectImpl() {
		if (this.commsThread != null)
			this.commsThread.interrupt();
	}

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
