package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.brahvim.nerd.io.net.NerdClientSocket;

/**
 * ...Only I (Brahvim) want to be using this class. It's written in the hopes to
 * make the API I want to in Nerd.
 */
public abstract class NerdAbstractTcpClient implements NerdClientSocket {

	protected final AtomicBoolean STOPPED = new AtomicBoolean();

	protected Socket socket;
	protected Thread commsThread;

	// region (`package`-level) Constructors.
	/* `package` */ NerdAbstractTcpClient(final String p_serverIp, final int p_myPort) {
		try {
			this.socket = new Socket(p_serverIp, p_myPort);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/* `package` */ NerdAbstractTcpClient(final int p_myPort) {
		try {
			this.socket = new Socket((String) null, p_myPort);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/* `package` */ NerdAbstractTcpClient(final Socket p_socket) {
		this.socket = p_socket;
	}
	// endregion

	public void disconnect() {
		if (this.STOPPED.get())
			return;

		// System.out.println(
		// "`NerdAbstractTcpClient::disconnect()` will now call `impl()`.");
		this.STOPPED.set(true);
		this.disconnectImpl();

		if (this.commsThread != null)
			try {
				this.commsThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

		// System.out.println(
		// "`NerdAbstractTcpClient::disconnect()` will now close the socket.");

		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// System.out.println("`NerdAbstractTcpClient::disconnect()` completed.");
	}

	// region Abstraction
	// For `TcpClient`s, these should send the data to the server.
	// For `TcpServer.TcpServerClient`s, these should send the data to the client.

	// The parameter is declared `final` because I want to be the only one extending
	// this class, and that's a style I follow.

	// Unimplemented so that the subclass may return itself rather than an
	// `AbstractTcpClient`. Yes, overloads can return subclasses and still keep
	// `@Override` happy! Java is awesome.

	protected abstract void disconnectImpl();

	public abstract NerdAbstractTcpClient send(final NerdAbstractTcpPacket p_data);

	public abstract NerdAbstractTcpClient send(final String p_data);

	public abstract NerdAbstractTcpClient send(final byte[] p_data);
	// endregion

	// region Getters.
	public int getServerPort() {
		return this.socket.getPort();
	}

	/**
	 * @return {@code -1} if the internal {@link Socket} has not yet been started.
	 */
	public int getLocalPort() {
		return this.socket.getLocalPort();
	}

	public Socket getSocket() {
		return this.socket;
	}

	public InetAddress getServerIp() {
		return this.socket.getInetAddress();
	}

	public String getIpServerString() {
		return this.socket.getInetAddress().toString();
	}
	// endregion

}
