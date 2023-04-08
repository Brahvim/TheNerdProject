package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ...Only I (Brahvim) want to be using this class. It's written in the hopes to
 * make the API I want to in Nerd.
 */
/* `package` */ abstract class AbstractTcpClient {

	protected Socket socket;

	public AbstractTcpClient(final String p_serverIp, final int p_myPort) {
		try {
			this.socket = new Socket(p_serverIp, p_myPort);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public AbstractTcpClient(final Socket p_socket) {
		this.socket = p_socket;
	}

	public AbstractTcpClient disconnect() {
		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	// region Abstraction
	// For `TcpClient`s, these should send the data to the server.
	// For `TcpServer.TcpServerClient`s, these should send the data to the client.

	// The parameter is declared `final` because I want to be the only one extending
	// this class, and that's a style I follow.

	// Unimplemented so that the subclass may return itself rather than an
	// `AbstractTcpClient`. Yes, overloads can return subclasses and still keep
	// `@Override` happy! Java is awesome.

	public abstract AbstractTcpClient send(final String p_data);

	public abstract AbstractTcpClient send(final byte[] p_data);
	// endregion

	// region Getters.
	public int getPort() {
		return this.socket.getPort();
	}

	public int getLocalPort() {
		return this.socket.getLocalPort();
	}

	public Socket getSocket() {
		return this.socket;
	}

	public InetAddress getIp() {
		return this.socket.getInetAddress();
	}

	public String getIpString() {
		return this.socket.getInetAddress().toString();
	}
	// endregion

}
