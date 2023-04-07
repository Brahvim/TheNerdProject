package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class TcpServer {

	// region Inner classes.
	public class TcpServerClient extends AbstractTcpClient {

		private Thread serverCommThread;
		private Consumer<?> messageCallback;

		public TcpServerClient(final String p_serverIp, final int p_myPort) {
			super(p_serverIp, p_myPort);
			this.delegatedConstruction();
		}

		public TcpServerClient(final Socket p_socket) {
			super(p_socket);
			this.delegatedConstruction();
		}

		private void delegatedConstruction() {
			this.serverCommThread = new Thread(() -> {
				while (true) {
				}
			});
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public TcpServer(final int p_port) {
		try {
			this.socket = new ServerSocket(p_port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	public TcpServer(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			this.socket = new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.delegatedConstruction();
	}

	private void delegatedConstruction() {
		this.connsThread = new Thread(() -> {
			try {
				this.clients.add(new TcpServer.TcpServerClient(this.socket.accept()));
			} catch (IOException e) {
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
		} catch (IOException e) {
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

	public ServerSocket getSocket() {
		return this.socket;
	}

	// region Callbacks.
	protected abstract void onClientConnect(AbstractTcpClient p_client);

	protected abstract void onServerShutdown();
	// endregion

}
