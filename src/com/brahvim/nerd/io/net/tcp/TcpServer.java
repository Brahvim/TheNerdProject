package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class TcpServer {

	// region Inner classes.
	public class TcpServerClient extends AbstractTcpClient {

		// region Standard receive buffer sizes.
		// (From: https://www.baeldung.com/cs/tcp-max-packet-size)
		public static final int TCP_PACKET_LEAST_SIZE = 543;
		public static final int TCP_PACKET_MAX_SIZE = 65_507;
		// You also need to fit the HEADER! The max is NOT `65535`!

		public static final int TCP_PACKET_DIAL_UP_SIZE = 576;
		public static final int TCP_PACKET_RECOMMENDED_SIZE = 576;

		public static final int TCP_PACKET_ROUTER_MAX_SIZE = 1500;
		public static final int TCP_PACKET_ROUTER_SAFEST_SIZE = 1000;
		// endregion

		/**
		 * Sets the size of the buffer (in bytes) data is received into. The maximum
		 * possible size is {@link TcpServerClient#PACKET_MAX_SIZE} ({@code 65_507})
		 * bytes.
		 *
		 * @apiNote Is {@link TcpServerClient#PACKET_MAX_SIZE} ({@code 65_507}) by
		 *          default.
		 * @implNote This <i>should</i> instead, be {@code 576} by default.
		 *           To know why, please see {@link<a href=
		 *           "https://stackoverflow.com/a/9235558/">this</a>}.
		 *           <br>
		 *           </br>
		 *           This field should not be something you need to worry about. Modern
		 *           computers are fast enough already. A {@code 65}KB allocation
		 *           cannot be a problem when your application already takes
		 *           {@code 200}MB in RAM, GC'ing away much of it every second,
		 *           and still sailing well.
		 */
		public Integer receivedPacketMaxSize = TcpServer.TcpServerClient.TCP_PACKET_MAX_SIZE;

		private Thread serverCommThread;
		private Consumer<TcpServer.TcpPacket> messageCallback;

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

		public void setMessageCallback(final Consumer<TcpServer.TcpPacket> p_callback) {
			this.messageCallback = Objects.requireNonNull(p_callback);
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
						for (int read, packetSize, bytesRead = 0; (read = stream.read()) != -1; bytesRead++) {
							if (bytesRead == 0) {
								packetSize = read;
							}

							

						}

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

	public class TcpPacket {

		private final TcpServer.TcpServerClient client;
		private final byte[] data;

		private TcpPacket(final TcpServer.TcpServerClient p_client, final byte[] p_data) {
			this.data = p_data;
			this.client = p_client;
		}

		public TcpServer.TcpServerClient getClient() {
			return this.client;
		}

		public byte[] getData() {
			return this.data;
		}

	}
	// endregion

	// region Fields.
	private ServerSocket socket;
	private Thread connsThread;
	private ArrayList<TcpServer.TcpServerClient> clients = new ArrayList<>();
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
				this.clients.add(new TcpServer.TcpServerClient(this.socket.accept()));
			} catch (final IOException e) {
				// e.printStackTrace();
			}
		});
	}
	// endregion

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

	public TcpServer sendToAll(final String p_data) {
		return this.sendToAll(p_data.getBytes(StandardCharsets.UTF_8));
	}

	public TcpServer sendToAll(final byte[] p_data) {
		for (final TcpServerClient c : this.clients)
			c.send(p_data);

		return this;
	}

	public void shutdown() {
		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			this.connsThread.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ServerSocket getSocket() {
		return this.socket;
	}

	// region Callbacks.
	protected abstract void onClientConnect(AbstractTcpClient p_client);

	protected abstract void onServerShutdown();
	// endregion

}
