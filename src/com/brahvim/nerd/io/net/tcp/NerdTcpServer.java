package com.brahvim.nerd.io.net.tcp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import processing.core.PApplet;

public class NerdTcpServer {

	public class NerdTcpServerClient extends NerdAbstractTcpClient {

		// region Fields.
		private final HashSet<Consumer<NerdReceivableTcpPacket>> MESSAGE_CALLBACKS = new HashSet<>();

		private Thread serverCommThread;
		// endregion

		// region Construction.
		public NerdTcpServerClient(final Socket p_socket) {
			super(p_socket);
			this.delegatedConstruction();
		}

		public NerdTcpServerClient(final String p_serverIp, final int p_myPort) {
			super(p_serverIp, p_myPort);
			this.delegatedConstruction();
		}

		private void delegatedConstruction() {
			this.serverCommThread = new Thread(() -> {
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
						stream.available();
						// ^^^ This is literally gunna return `0`!
						// ..I guess we use fixed sizes around here...

						// ..Now read it:
						final int packetSize = stream.readInt();
						final byte[] packetData = new byte[packetSize];
						stream.read(packetData); // It needs to know the length of the array!
						final NerdReceivableTcpPacket packet = new NerdReceivableTcpPacket(this, packetData);

						// System.out.println(
						// "`NerdTcpServer.NerdTcpServerClient::serverCommThread::run()` read the
						// stream.");

						// The benefit of having a type like `ReceivableTcpPacket` *is* that I won't
						// have to reconstruct it every time, fearing that one of these callbacks might
						// change the contents of the packet.

						synchronized (this.MESSAGE_CALLBACKS) {
							// System.out.println("""
							// `NerdTcpServer.NerdTcpServerClient::serverCommThread::run()` \
							// entered the synced block.""");
							for (final var c : this.MESSAGE_CALLBACKS)
								try {
									// System.out.println(
									// "`NerdTcpServer.NerdTcpServerClient::serverCommThread::run()` called a
									// message callback.");
									c.accept(packet);
								} catch (final Exception e) {
									e.printStackTrace();
								}
						}
					} catch (final IOException e) {
						// When the client disconnects, this exception is thrown by
						// `*InpuStream::read*()`:
						if (e instanceof EOFException)
							this.disconnect();
						else
							e.printStackTrace();
					}
			});

			this.serverCommThread.setName("NerdTcpClientListenerOnPort" + this.socket.getLocalPort());
			this.serverCommThread.setDaemon(true);
			this.serverCommThread.start();
		}
		// endregion

		// region ...Sending or something, I dunno.
		@Override
		public NerdTcpServer.NerdTcpServerClient send(final NerdAbstractTcpPacket p_packet) {
			try {
				final byte[] packData = p_packet.getData();
				final byte[] packDataLen = ByteBuffer
						.allocate(Integer.BYTES).putInt(p_packet.getDataLength()).array();
				super.socket.getOutputStream().write(PApplet.concat(packDataLen, packData));
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public NerdTcpServer.NerdTcpServerClient send(final String p_data) {
			return this.send(new NerdSendableTcpPacket(p_data.getBytes(StandardCharsets.UTF_8)));
		}

		@Override
		public NerdTcpServer.NerdTcpServerClient send(final byte[] p_data) {
			return this.send(new NerdSendableTcpPacket(p_data));
		}
		// endregion

		// region Working with the message callbacks collection.
		public NerdTcpServer.NerdTcpServerClient addMessageCallback(
				final Consumer<NerdReceivableTcpPacket> p_callback) {
			synchronized (this.MESSAGE_CALLBACKS) {
				this.MESSAGE_CALLBACKS.add(p_callback);
			}
			return this;
		}

		@SuppressWarnings("all")
		public NerdTcpServer.NerdTcpServerClient removeMessageCallback(
				final Consumer<NerdReceivableTcpPacket> p_callback) {
			synchronized (this.MESSAGE_CALLBACKS) {
				this.MESSAGE_CALLBACKS.remove(p_callback);
			}
			return this;
		}

		/**
		 * @return A copy of the {@link HashSet} containing all message callbacks.
		 */
		public HashSet<Consumer<NerdReceivableTcpPacket>> getAllMessageCallbacks() {
			synchronized (this.MESSAGE_CALLBACKS) {
				return new HashSet<>(this.MESSAGE_CALLBACKS);
			}
		}

		@SuppressWarnings("all")
		public HashSet<Consumer<NerdReceivableTcpPacket>> removeAllMessageCallbacks() {
			HashSet<Consumer<NerdReceivableTcpPacket>> toRet = null;
			synchronized (this.MESSAGE_CALLBACKS) {
				toRet = new HashSet<>(this.MESSAGE_CALLBACKS);
				this.MESSAGE_CALLBACKS.clear();
			}

			return toRet;
		}
		// endregion

		public Thread getReceiverThread() {
			return this.serverCommThread;
		}

		@Override
		public NerdTcpServer.NerdTcpServerClient disconnect() {
			try {
				this.serverCommThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			synchronized (NerdTcpServer.this.clients) {
				NerdTcpServer.this.clients.remove(this);
			}

			super.disconnect();
			return this;
		}

	}

	// region Fields.
	private final ArrayList<NerdTcpServer.NerdTcpServerClient> clients = new ArrayList<>();

	private Thread connsThread;
	private ServerSocket socket;
	private volatile boolean startedShutdown;
	private Function<NerdAbstractTcpClient, Consumer<NerdReceivableTcpPacket>> connectionCallback;
	// endregion

	// region Construction.
	public NerdTcpServer(final int p_port,
			final Function<NerdAbstractTcpClient, Consumer<NerdReceivableTcpPacket>> p_connectionCallback) {
		this.connectionCallback = p_connectionCallback;

		try {
			this.socket = new ServerSocket(p_port);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public NerdTcpServer(final int p_port) {
		try {
			this.socket = new ServerSocket(p_port);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public NerdTcpServer(final int p_port, final int p_maxConnReqsAtOnce,
			final Function<NerdAbstractTcpClient, Consumer<NerdReceivableTcpPacket>> p_connectionCallback) {
		this.connectionCallback = p_connectionCallback;

		try {
			this.socket = new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public NerdTcpServer(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			this.socket = new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	private void delegatedConstruction() {
		try {
			this.socket.setSoTimeout(500);
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		this.connsThread = new Thread(() -> {
			while (!this.startedShutdown)
				try { // Loop, or try-catch block first?
					final var client = new NerdTcpServer.NerdTcpServerClient(this.socket.accept());

					synchronized (NerdTcpServer.this.clients) {
						this.clients.add(client);
					}

					if (this.connectionCallback == null)
						continue;

					final var callback = this.connectionCallback.apply(client);

					if (callback != null)
						client.addMessageCallback(callback);
				} catch (final IOException e) {
					if (!(e instanceof SocketTimeoutException))
						e.printStackTrace();
				}
		});

		this.connsThread.setDaemon(true);
		this.connsThread.start();
	}
	// endregion

	public NerdTcpServer onNewConnection(
			final Function<NerdAbstractTcpClient, Consumer<NerdReceivableTcpPacket>> p_callback) {
		this.connectionCallback = Objects.requireNonNull(p_callback);
		return this;
	}

	public void shutdown() {
		this.startedShutdown = true;

		try {
			this.connsThread.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		synchronized (this.clients) {
			for (int i = this.clients.size() - 1; i != 0; i--)
				this.clients.get(i).disconnect();
		}

		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// region Sending stuff.
	// Using an `AbstractTcpClient` subclass:
	public NerdTcpServer send(final NerdAbstractTcpClient p_client, final NerdAbstractTcpPacket p_packet) {
		p_client.send(p_packet);
		return this;
	}

	public NerdTcpServer send(final NerdAbstractTcpClient p_client, final String p_data) {
		this.send(p_client.getSocket(), p_data.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public NerdTcpServer send(final NerdAbstractTcpClient p_client, final byte[] p_data) {
		this.send(p_client.getSocket(), p_data);
		return this;
	}

	// Using pure `Socket`s:
	public NerdTcpServer send(final Socket p_client, final NerdAbstractTcpPacket p_packet) {
		this.send(p_client, p_packet.getData());
		return this;
	}

	public NerdTcpServer send(final Socket p_client, final String p_data) {
		this.send(p_client, p_data.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public NerdTcpServer send(final Socket p_client, final byte[] p_data) {
		try {
			p_client.getOutputStream().write(p_data);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	// ...sending stuff to everyone connected!:
	public NerdTcpServer sendToAll(final NerdAbstractTcpPacket p_packet) {
		return this.sendToAll(p_packet.getData());
	}

	public NerdTcpServer sendToAll(final String p_data) {
		return this.sendToAll(p_data.getBytes(StandardCharsets.UTF_8));
	}

	public NerdTcpServer sendToAll(final byte[] p_data) {
		synchronized (this.clients) {
			for (final NerdTcpServerClient c : this.clients)
				c.send(p_data);
		}

		return this;
	}
	// endregion

	public ServerSocket getSocket() {
		return this.socket;
	}

}
