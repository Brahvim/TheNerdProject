package com.brahvim.nerd.io.net.tcp.implementations.ssl;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import javax.net.ssl.SSLSocket;

import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpClient;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpPacket;
import com.brahvim.nerd.io.net.tcp.implementations.NerdSendableTcpPacket;

import processing.core.PApplet;

public class NerdTcpWithSslClient extends NerdAbstractTcpClient {

	public class NerdServerSentTcpPacket extends NerdAbstractTcpPacket {

		public NerdServerSentTcpPacket(final byte[] p_data) {
			super(p_data);
		}

		public NerdTcpWithSslClient getReceivingClient() {
			return NerdTcpWithSslClient.this;
		}

	}

	private final List<Consumer<NerdTcpWithSslClient.NerdServerSentTcpPacket>> MESSAGE_CALLBACKS = new Vector<>(1);

	// region Construction.
	// region Constructors.
	public NerdTcpWithSslClient(final SSLSocket p_sslSocket) {
		super(p_sslSocket);
	}

	public NerdTcpWithSslClient(final SSLSocket p_sslSocket,
			final Consumer<NerdTcpWithSslClient.NerdServerSentTcpPacket> p_messageCallback) {
		this(p_sslSocket);
		this.startMessageThread(p_messageCallback);
	}

	public NerdTcpWithSslClient(final String p_serverIp, final int p_myPort) {
		super(p_serverIp, p_myPort);
	}

	public NerdTcpWithSslClient(final String p_serverIp, final int p_myPort,
			final Consumer<NerdTcpWithSslClient.NerdServerSentTcpPacket> p_messageCallback) {
		this(p_serverIp, p_myPort);
		this.startMessageThread(p_messageCallback);
	}

	public NerdTcpWithSslClient(final int p_port) {
		super(p_port);
	}

	public NerdTcpWithSslClient(final int p_port,
			final Consumer<NerdTcpWithSslClient.NerdServerSentTcpPacket> p_messageCallback) {
		this(p_port);
		this.startMessageThread(p_messageCallback);
	}
	// endregion

	private void startMessageThread(final Consumer<NerdTcpWithSslClient.NerdServerSentTcpPacket> p_messageCallback) {
		// Shouldn't occur till somebody uses reflection!:
		if (super.STOPPED.get())
			return;

		this.MESSAGE_CALLBACKS.add(p_messageCallback);

		// It's faster to give the thread a name in this manner:
		super.commsThread = new Thread(this::receiverTasks,
				this.getClass().getSimpleName() + "OnPort:" + this.socket.getLocalPort());
		super.commsThread.setDaemon(true);
		super.commsThread.start();
	}

	private void receiverTasks() {
		// No worries - the same stream is used till the socket shuts down.
		DataInputStream stream = null;

		// ...Get that stream!:
		try {
			synchronized (this.socket) {
				// If the socket has been closed, EXIT!
				// `stream` will be `null` otherwise...:
				if (this.socket.isClosed())
					return;

				stream = new DataInputStream(this.socket.getInputStream());
			}
		} catch (final IOException e) {
			e.printStackTrace();
			synchronized (System.err) {
				e.printStackTrace();
			}
			this.disconnect();
			return;
		}

		while (!super.STOPPED.get())
			try {
				if (stream.available() < 1)
					return;

				// ^^^ This is literally gunna return `0`!
				// ..I guess we use fixed sizes around here...

				// ..Now read it:
				// FIXME VULNERABILITY! What if the packet ISN'T from Nerd?!
				final int packetSize = stream.readInt();
				final byte[] packetData = new byte[packetSize];

				// int bytesRead = 0; // We *are* relying on that `EOFException` down there...
				// while (!(bytesRead == packetSize || bytesRead == -1))
				/* bytesRead = */ stream.read(packetData); // It needs to know the length of the array!

				final NerdServerSentTcpPacket packet = new NerdTcpWithSslClient.NerdServerSentTcpPacket(packetData);

				// The benefit of having a type like `ReceivableTcpPacket` *is* that I won't
				// have to reconstruct it every time, fearing that one of these callbacks might
				// change the contents of the packet.

				synchronized (this.MESSAGE_CALLBACKS) {
					for (final Consumer<NerdServerSentTcpPacket> c : this.MESSAGE_CALLBACKS)
						try {// NOSONAR
								// I want this to skip an iteration when something goes wrong, not
								// completely break the loop!
							c.accept(packet);
						} catch (final Exception e) {
							e.printStackTrace();
						}
				}
			} catch (final IOException e) {
				// When the client disconnects, this exception is thrown by
				// `*InputStream::read*()`:
				if (e instanceof EOFException || e instanceof SocketException)
					this.disconnect();
				else
					e.printStackTrace(); // I have NO idea what to do, y'hear!
			}
	}
	// endregion

	public boolean isInMessageLoop() {
		return super.STOPPED.get();
	}

	@Override
	protected void disconnectImpl() {
		// super.STOPPED.set(false);
	}

	// region Sending stuff.
	@Override
	public NerdTcpWithSslClient send(final NerdAbstractTcpPacket p_packet) {
		try {
			final byte[] packData = p_packet.getData();
			final byte[] packDataLen = ByteBuffer.allocate(Integer.BYTES).putInt(p_packet.getDataLength()).array();

			// System.out.println("`NerdTcpClient::send()` was called.");
			super.socket.getOutputStream().write(PApplet.concat(packDataLen, packData));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public NerdTcpWithSslClient send(final String p_data) {
		return this.send(p_data.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public NerdTcpWithSslClient send(final byte[] p_data) {
		return this.send(new NerdSendableTcpPacket(p_data));
	}
	// endregion

}
