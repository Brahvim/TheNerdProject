package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TcpServer {

	public class TcpServerClient extends AbstractTcpClient {

		private Thread serverCommThread;
		private Consumer<ReceivableTcpPacket> messageCallback;

		public TcpServerClient(final Socket p_socket) {
			super(p_socket);
			this.delegatedConstruction();
		}

		public TcpServerClient(final String p_serverIp, final int p_myPort) {
			super(p_serverIp, p_myPort);
			this.delegatedConstruction();
		}

		@Override
		public TcpServer.TcpServerClient send(final String p_data) {
			return this.send(p_data.getBytes(StandardCharsets.UTF_8));
		}

		@Override
		public TcpServer.TcpServerClient send(final byte[] p_data) {
			try {
				super.socket.getOutputStream().write(p_data);
			} catch (final IOException e) {
				e.printStackTrace();
			}

			return this;
		}

		public TcpServer.TcpServerClient setMessageCallback(
				final Consumer<ReceivableTcpPacket> p_callback) {
			this.messageCallback = Objects.requireNonNull(p_callback);
			return this;
		}

		public Consumer<ReceivableTcpPacket> getMessageCallback() {
			return this.messageCallback;
		}

		private void delegatedConstruction() {
			this.serverCommThread = new Thread(() -> {

				while (true) {
					try {
						// ...Get that stream!!!:
						final InputStream stream = this.socket.getInputStream();
						stream.available();
						// ^^^ This is literally gunna return `0`!
						// ..I guess we use fixed sizes around here...

						// ..Now read it:

						final ReceivableTcpPacket packet;
						final int packetSize = stream.read();
						final byte[] packetData = new byte[packetSize];

						for (int read, bytesRead = 0; bytesRead != packetSize
								// ...Useless check?
								&& (read = stream.read()) != -1; bytesRead++) {
						}

						packet = new ReceivableTcpPacket(this, packetData);

					} catch (final IOException e) {
						e.printStackTrace();
					}

				}
			});
		}

		private Thread getCommsThread() {
			return this.serverCommThread;
		}

	}

	// region Fields.
	private Thread connsThread;
	private ServerSocket socket;
	private Consumer<TcpServer> serverShutdownCallback; // TODO: Is this actually a good idea? Code like:
	// TODO: `tellAll("Bye!"); shutdown();` is better since it tells what happened.
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
				client.setMessageCallback(this.clientConnectionCallback.apply(client));
				this.clients.add(client);
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

		this.serverShutdownCallback.accept(this);
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
