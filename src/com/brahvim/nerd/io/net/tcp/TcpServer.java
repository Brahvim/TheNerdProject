package com.brahvim.nerd.io.net.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import processing.core.PApplet;

public abstract class TcpServer {

	public class TcpServerClient extends AbstractTcpClient {

		private Thread serverCommThread;
		private HashSet<Consumer<ReceivableTcpPacket>> messageCallbacks;

		// region Construction.
		public TcpServerClient(final Socket p_socket) {
			super(p_socket);
			this.delegatedConstruction();
		}

		public TcpServerClient(final String p_serverIp, final int p_myPort) {
			super(p_serverIp, p_myPort);
			this.delegatedConstruction();
		}

		private void delegatedConstruction() {
			this.serverCommThread = new Thread(() -> {
				while (true) {
					try {
						// ...Get that stream!!!:
						final DataInputStream stream = new DataInputStream(this.socket.getInputStream());
						stream.available();
						// ^^^ This is literally gunna return `0`!
						// ..I guess we use fixed sizes around here...

						// ..Now read it:

						final int packetSize = stream.readInt();
						final byte[] packetData = new byte[packetSize];
						stream.read(packetData); // It needs to know the length of the array!
						final ReceivableTcpPacket packet = new ReceivableTcpPacket(this, packetData);

						// The benefit of having a type like `ReceivableTcpPacket` *is* that I won't
						// have to reconstruct it every time, fearing that one of these callbacks might
						// change the contents of the packet.

						for (final var c : this.messageCallbacks)
							c.accept(packet);

					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			});

			this.serverCommThread.setName("NerdTcpClientOnPort" + this.socket.getLocalPort());
		}
		// endregion

		// region ...Sending or something, I dunno.
		@Override
		public TcpServer.TcpServerClient send(final AbstractTcpPacket p_packet) {
			try {
				final byte[] packData = p_packet.getData();
				final byte[] packDataLen = ByteBuffer
						.allocate(Integer.BYTES).putInt(p_packet.getLength()).array();
				super.socket.getOutputStream().write(PApplet.concat(packDataLen, packData));
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public TcpServer.TcpServerClient send(final String p_data) {
			return this.send(new SendableTcpPacket(p_data.getBytes(StandardCharsets.UTF_8)));
		}

		@Override
		public TcpServer.TcpServerClient send(final byte[] p_data) {
			return this.send(new SendableTcpPacket(p_data));
		}
		// endregion

		// region Working with the message callbacks collection.
		public TcpServer.TcpServerClient addMessageCallback(
				final Consumer<ReceivableTcpPacket> p_callback) {
			this.messageCallbacks.add(p_callback);
			return this;
		}

		@SuppressWarnings("all")
		public TcpServer.TcpServerClient removeMessageCallback(
				final Consumer<ReceivableTcpPacket> p_callback) {
			this.messageCallbacks.remove(p_callback);
			return this;
		}

		/**
		 * @return A copy of the {@link HashSet} containing all message callbacks.
		 */
		public HashSet<Consumer<ReceivableTcpPacket>> getAllMessageCallbacks() {
			return new HashSet<>(this.messageCallbacks);
		}

		@SuppressWarnings("all")
		public HashSet<Consumer<ReceivableTcpPacket>> removeAllMessageCallbacks() {
			final HashSet<Consumer<ReceivableTcpPacket>> toRet = new HashSet<>(this.messageCallbacks);
			this.messageCallbacks.clear();
			return toRet;
		}
		// endregion

		public Thread getReceiverThread() {
			return this.serverCommThread;
		}

		@Override
		public TcpServer.TcpServerClient disconnect() {
			super.disconnect();
			TcpServer.this.clients.remove(this);
			return this;
		}

	}

	// region Fields.
	private Thread connsThread;
	private ServerSocket socket;
	private ArrayList<TcpServer.TcpServerClient> clients = new ArrayList<>();
	private Function<AbstractTcpClient, Consumer<ReceivableTcpPacket>> clientConnectionCallback;
	// endregion

	// region Construction.
	public TcpServer() {
		try {
			this.socket = new ServerSocket();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public TcpServer(final int p_port) {
		try {
			this.socket = new ServerSocket(p_port);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public TcpServer(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			this.socket = new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	private void delegatedConstruction() {
		this.connsThread = new Thread(() -> {
			try {
				final var client = new TcpServer.TcpServerClient(this.socket.accept());
				final var callback = this.clientConnectionCallback.apply(client);
				this.clients.add(client);
				if (callback != null)
					client.addMessageCallback(callback);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}
	// endregion

	public void shutdown() {
		try {
			this.socket.close();
			this.connsThread.join();
		} catch (final InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	// region Sending stuff.
	// Using an `AbstractTcpClient` subclass:
	public TcpServer send(final AbstractTcpClient p_client, final AbstractTcpPacket p_packet) {
		this.send(p_client.getSocket(), p_packet.getData());
		return this;
	}

	public TcpServer send(final AbstractTcpClient p_client, final String p_data) {
		this.send(p_client.getSocket(), p_data.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public TcpServer send(final AbstractTcpClient p_client, final byte[] p_data) {
		this.send(p_client.getSocket(), p_data);
		return this;
	}

	// Using pure `Socket`s:
	public TcpServer send(final Socket p_client, final AbstractTcpPacket p_packet) {
		this.send(p_client, p_packet.getData());
		return this;
	}

	public TcpServer send(final Socket p_client, final String p_data) {
		this.send(p_client, p_data.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public TcpServer send(final Socket p_client, final byte[] p_data) {
		try {
			p_client.getOutputStream().write(p_data);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	// ...sending stuff to everyone connected!:
	public TcpServer sendToAll(final AbstractTcpPacket p_packet) {
		return this.sendToAll(p_packet.getData());
	}

	public TcpServer sendToAll(final String p_data) {
		return this.sendToAll(p_data.getBytes(StandardCharsets.UTF_8));
	}

	public TcpServer sendToAll(final byte[] p_data) {
		for (final TcpServerClient c : this.clients)
			c.send(p_data);

		return this;
	}
	// endregion

	// region Getters.
	public ServerSocket getSocket() {
		return this.socket;
	}
	// endregion

}
