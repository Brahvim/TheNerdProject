package com.brahvim.nerd.io.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TcpServer {

	// region Fields.
	private ServerSocket socket;
	private Thread connsThread, commsThread;
	private ArrayList<TcpClient> clients = new ArrayList<>();
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
				this.clients.add(new TcpClient(this.socket.accept()));
			} catch (IOException e) {
				// e.printStackTrace();
			}
		});

		this.commsThread = new Thread(() -> {
			for (final TcpClient c : this.clients) {
				// TODO: Decide how TCP clients recieve :P
			}
		});
	}
	// endregion

	public TcpServer send(final Socket p_client, final String p_bytes) {
		this.send(p_client, p_bytes.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public TcpServer send(final Socket p_client, final byte[] p_bytes) {
		try {
			p_client.getOutputStream().write(p_bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ServerSocket getSocket() {
		return this.socket;
	}

}
