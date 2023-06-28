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
import java.util.HashSet;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.net.NerdServerSocket;

import processing.core.PApplet;

public class NerdTcpServer implements NerdServerSocket {

	public class NerdClientSentTcpPacket extends NerdAbstractTcpPacket {

		private final NerdTcpServer.NerdTcpServerClient CLIENT;

		private NerdClientSentTcpPacket(final NerdTcpServer.NerdTcpServerClient p_client,
				final byte[] p_data) {
			super(p_data);
			this.CLIENT = p_client;
		}

		public NerdTcpServer.NerdTcpServerClient getSender() {
			return this.CLIENT;
		}

	}

	public class NerdTcpServerClient extends NerdAbstractTcpClient {

		// region Fields.
		private final Vector<Consumer<NerdTcpServer.NerdClientSentTcpPacket>> MESSAGE_CALLBACKS = new Vector<>(1);

		private Thread serverCommThread;
		// endregion

		// region Constructors.
		public NerdTcpServerClient(final Socket p_socket) {
			super(p_socket);
		}

		public NerdTcpServerClient(final String p_serverIp, final int p_myPort) {
			super(p_serverIp, p_myPort);
		}
		// endregion

		private void startMessageThread() {
			if (this.inMessageLoop)
				return;

			if (super.hasDisconnected)
				return;

			this.inMessageLoop = true;
			this.serverCommThread = new Thread(() -> {
				// No worries - the same stream is used till the socket shuts down.
				DataInputStream stream = null;

				// ...Get that stream!:
				try {
					stream = new DataInputStream(this.socket.getInputStream());
				} catch (final IOException e) {
					// e.printStackTrace();
				}

				while (true)
					try {
						// System.out.println("NerdTcpServer.NerdTcpServerClient.startMessageThread()");
						if (this.serverCommThread.isInterrupted())
							return;

						// System.out.printf("hasDisconnected: `%s`.%n", super.hasDisconnected);
						// System.out.println("`NerdTcpServer.NerdTcpServerClient::serverCommsThread::run()`");
						stream.available();
						// ^^^ This is literally gunna return `0`!
						// ..I guess we use fixed sizes around here...

						// ..Now read it:
						final int packetSize = stream.readInt();
						final byte[] packetData = new byte[packetSize];
						stream.read(packetData); // It needs to know the length of the array!
						final var packet = new NerdTcpServer.NerdClientSentTcpPacket(this, packetData);

						// System.out.println("""
						// `NerdTcpServer.NerdTcpServerClient\
						// ::serverCommThread::run()` read the \
						// stream.""");

						// The benefit of having a type like `ReceivableTcpPacket` *is* that I won't
						// have to reconstruct it every time, fearing that one of these callbacks might
						// change the contents of the packet.

						synchronized (this.MESSAGE_CALLBACKS) {
							// System.out.println("""
							// `NerdTcpServer.NerdTcpServerClient::serverCommThread::run()` \
							// entered the synced block.""");
							for (final var c : this.MESSAGE_CALLBACKS)
								try {
									// System.out.println("""
									// `NerdTcpServer.NerdTcpServerClient\
									// ::serverCommThread::run()` \
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
						else
							e.printStackTrace();
					}
			}, "NerdTcpClientListenerOnPort" + this.socket.getLocalPort());
			// ^^^ Yes, that's the thread's name.
			this.serverCommThread.setDaemon(true);
			this.serverCommThread.start();
		}

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
				final Consumer<NerdTcpServer.NerdClientSentTcpPacket> p_callback) {
			synchronized (this.MESSAGE_CALLBACKS) {
				this.MESSAGE_CALLBACKS.add(p_callback);
			}
			return this;
		}

		@SuppressWarnings("all")
		public NerdTcpServer.NerdTcpServerClient removeMessageCallback(
				final Consumer<NerdTcpServer.NerdClientSentTcpPacket> p_callback) {
			synchronized (this.MESSAGE_CALLBACKS) {
				this.MESSAGE_CALLBACKS.remove(p_callback);
			}
			return this;
		}

		/**
		 * @return A copy of the {@link HashSet} containing all message callbacks.
		 */
		public HashSet<Consumer<NerdTcpServer.NerdClientSentTcpPacket>> getAllMessageCallbacks() {
			synchronized (this.MESSAGE_CALLBACKS) {
				return new HashSet<>(this.MESSAGE_CALLBACKS);
			}
		}

		@SuppressWarnings("all")
		public HashSet<Consumer<NerdTcpServer.NerdClientSentTcpPacket>> removeAllMessageCallbacks() {
			HashSet<Consumer<NerdTcpServer.NerdClientSentTcpPacket>> toRet = null;
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
		protected void disconnectImpl() {
			if (this.serverCommThread != null)
				this.serverCommThread.interrupt();

			synchronized (NerdTcpServer.this.CLIENTS) {
				NerdTcpServer.this.CLIENTS.remove(this);
			}
		}

	}

	// region Fields.
	// Concurrency is huge:
	private final Vector<NerdTcpServer.NerdTcpServerClient> CLIENTS = new Vector<>(1);
	private final Vector<Consumer<NerdTcpServer.NerdClientSentTcpPacket>> NEW_CONNECTION_CALLBACKS = new Vector<>(1);

	private Thread connsThread;
	private ServerSocket socket;
	private boolean startedShutdown;
	private Function<NerdAbstractTcpClient, Boolean> invitationCallback;
	// endregion

	// region Construction.
	// region Constructors.
	public NerdTcpServer(final int p_port,
			final Function<NerdAbstractTcpClient, Boolean> p_invitationCallback) {
		this.invitationCallback = p_invitationCallback;

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
			final Function<NerdAbstractTcpClient, Boolean> p_connectionCallback) {
		this.invitationCallback = p_connectionCallback;

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
	// endregion

	private void delegatedConstruction() {
		try {
			this.socket.setSoTimeout(500);
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		this.connsThread = new Thread(() -> {
			while (!Thread.interrupted())
				try { // Loop, or try-catch block first?
						// System.out.println("`NerdTcpServer::connsThread::run()`");
					final var client = new NerdTcpServer.NerdTcpServerClient(this.socket.accept());

					synchronized (NerdTcpServer.this.CLIENTS) {
						this.CLIENTS.add(client);
					}

					if (this.invitationCallback != null) {
						final Boolean check = this.invitationCallback.apply(client);

						// if (!(check == null || !check)) {
						if (check != null && check) {

							for (final var c : NerdTcpServer.this.NEW_CONNECTION_CALLBACKS)
								client.addMessageCallback(c);

							client.startMessageThread();
						}
					}

					client.disconnect();
				} catch (final IOException e) {
					if (!(e instanceof SocketTimeoutException))
						e.printStackTrace();
				}
		});

		this.connsThread.setDaemon(true);
		this.connsThread.start();
	}
	// endregion

	public NerdTcpServer setClientInvitationCallback(
			final Function<NerdAbstractTcpClient, Boolean> p_callback) {
		this.invitationCallback = Objects.requireNonNull(p_callback);
		return this;
	}

	public Function<NerdAbstractTcpClient, Boolean> getClientInvitationCallback() {
		return this.invitationCallback;
	}

	/**
	 * When a new client connects to this {@link NerdTcpServer}, callbacks
	 * registered via this method, are called.
	 * 
	 * @param p_callback is a callback providing the new client a message callback!
	 * @return The {@link NerdTcpServer} instance this method was called on.
	 */
	public NerdTcpServer addMessageReceivedCallback(
			final Consumer<NerdTcpServer.NerdClientSentTcpPacket> p_callback) {
		this.NEW_CONNECTION_CALLBACKS.add(Objects.requireNonNull(p_callback));
		return this;
	}

	/**
	 * Removes a callback registered via the method,
	 * {@link NerdTcpServer#addMessageReceivedCallback(Consumer)}.
	 * 
	 * @param p_callback is a callback providing the new client a message callback!
	 * @return The {@link NerdTcpServer} instance this method was called on.
	 */
	public NerdTcpServer removeMessageReceivedCallback(
			final Consumer<NerdTcpServer.NerdClientSentTcpPacket> p_callback) {
		this.NEW_CONNECTION_CALLBACKS.remove(Objects.requireNonNull(p_callback));
		return this;
	}

	public void shutdown() {
		if (this.startedShutdown)
			return;

		this.startedShutdown = true;
		// System.out.println("`NerdTcpServer::shutdown()` has begun!");

		this.disconnectAll();

		// System.out.println("`NerdTcpServer::shutdown()` disconnected all clients.");

		try {
			this.connsThread.interrupt();
			this.connsThread.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		// System.out.println("`NerdTcpServer::shutdown()` stopped `connsThread`.");

		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// System.out.println("`NerdTcpServer::shutdown()` closed its `ServerSocket`
		// instance.");
	}

	public void disconnectAll() {
		synchronized (this.CLIENTS) {
			for (int i = this.CLIENTS.size() - 1; i != -1; i--)
				this.CLIENTS.get(i).disconnect();
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
		synchronized (this.CLIENTS) {
			for (final NerdTcpServerClient c : this.CLIENTS)
				c.send(p_data);
		}

		return this;
	}
	// endregion

	public ServerSocket getSocket() {
		return this.socket;
	}

}
