package com.brahvim.nerd.io.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * {@link NerdUdpSocket} helps two applications running on different machines
 * connect via networks following the "User Datagram Protocol" and let them
 * listen to each other on a different thread for easier asynchronous
 * multitasking.
 * 
 * <p>
 * Of course, it is based on classes from the
 * {@link java.net} package :)
 *
 * @author Brahvim Bhaktvatsal
 */

public abstract class NerdUdpSocket implements NerdServerSocket {

	// Concurrent stuff *haha:*
	/**
	 * The {@link NerdUdpSocket.ReceiverThread} class helps {@link NerdUdpSocket}s
	 * receive
	 * data on
	 * a separate thread, aiding with application performance and modern
	 * hardware programming practices. It may NOT, be useful on systems like
	 * Android, where ALL networking tasks must be done asynchronously.
	 *
	 * @author Brahvim Bhaktvatsal
	 * @see NerdUdpSocket
	 */
	public class ReceiverThread {

		// region Fields.
		/**
		 * Number of bytes allocated to hold the data in a packet. The size of the
		 * array is truncated before it is passed to `UdpSocket::onReceive()`.
		 */
		public static final int PACKET_MAX_SIZE = 70_000;

		/**
		 * The {@link Thread} that handles the network's receive calls.
		 *
		 * @implSpec {@link NerdUdpSocket.ReceiverThread#start()}
		 *           should set this to be a daemon thread.
		 * @see NerdUdpSocket.ReceiverThread#start()
		 * @see NerdUdpSocket.ReceiverThread#stop()
		 */
		private final Thread thread; // Ti's but a daemon thread.

		/**
		 * Internal field holding data used by the receiver thread.
		 */
		// B I G __ A L L O C A T I O N:
		private byte[] byteData;
		// endregion

		/**
		 * Starts the thread and listens for events.
		 */
		private ReceiverThread() {
			this.byteData = new byte[ReceiverThread.PACKET_MAX_SIZE];

			this.thread = new Thread(this::receiverTasks,
					"UdpSocketReceiverOnPort" + NerdUdpSocket.this.getLocalPort());
			this.thread.setDaemon(true); // The JVM can shut down without waiting for this thread to.
			this.thread.start();
		}

		private void receiverTasks() {
			// We got some work?
			while (!Thread.interrupted()) {
				try {
					NerdUdpSocket.this.in = new DatagramPacket(this.byteData, this.byteData.length);
					if (NerdUdpSocket.this.socket != null)
						NerdUdpSocket.this.socket.receive(NerdUdpSocket.this.in); // Fetch it well!
				} catch (final IOException e) {
					if (e instanceof SocketTimeoutException) {
						// ¯\_(ツ)_/¯
						// System.out.println("Timeout ended! Continuing...");
					} else if (e instanceof SocketException) {
						this.thread.interrupt();
						return;
					} else
						e.printStackTrace(); // ¯\_(ツ)_/¯
				}

				// Callback!:
				if (NerdUdpSocket.this.in != null) {
					final InetAddress addr = NerdUdpSocket.this.in.getAddress();

					if (addr == null)
						continue;

					// System.out.println("Calling `onReceive()`!");

					// The user's code can throw exceptions that could pause our
					// thread.
					// :)

					// ..Gotta handle those!:

					try {
						final byte[] copy = new byte[NerdUdpSocket.this.in.getLength()];

						// Don't worry, this won't crash. *I hope.*
						System.arraycopy(this.byteData, 0, copy, 0, copy.length);
						// Super slow `memset()`...
						// for (int i = 0; i < byteData.length; i++)
						// byteData[i] = 0;

						// ~~...but I just didn't want to use `System.arraycopy()`
						// with a freshly allocated array. What a WASTE!~~
						this.byteData = new byte[ReceiverThread.PACKET_MAX_SIZE];
						// PS It IS TWO WHOLE ORDERS OF MAGNITUDE faster to allocate
						// than to do a loop and set values. Java allocations ARE fast.
						// I'm only worried about de-allocation. GCs can be slow, right?

						NerdUdpSocket.this.onReceive(copy,
								addr.toString().substring(1),
								NerdUdpSocket.this.in.getPort());
					} catch (final Exception e) {
						e.printStackTrace();
					}
				} // End of `if (PARENT.in != null)`.

			} // End of `while` loop,
		} // End of `run()`,

		public Thread getThread() {
			return this.thread;
		}

		public void stop() {
			this.thread.interrupt();
			try {
				this.thread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// region Fields!
	/**
	 * The default timeout value, in milliseconds, for each {@link NerdUdpSocket}.
	 * Used
	 * if a timeout value is not specified in the constructor.
	 *
	 * @implSpec Should be {@code 32}.
	 */
	public static final int DEFAULT_TIMEOUT = 32;

	/**
	 * The internal, {@code private} {@link NerdUdpSocket.ReceiverThread} instance.
	 * In abstract words, it handles threading for receiving messages.
	 */
	protected ReceiverThread receiver;

	/**
	 * The internal {@link DatagramSocket} that takes care of
	 * networking.
	 * 
	 * <p>
	 * If you need to change it, consider using the
	 * {@link NerdUdpSocket#setSocket(DatagramSocket)}
	 * method (it pauses the receiving thread, swaps the socket, and resumes
	 * listening).
	 * 
	 * <p>
	 * {@link NerdUdpSocket#getSocket()} <b>should</b> be used for equality checks,
	 * etcetera.
	 */
	private DatagramSocket socket;

	/**
	 * Holds the previous {@link DatagramPacket} that was sent.
	 */
	private DatagramPacket out;

	/**
	 * Holds the previous {@link DatagramPacket} that was received.
	 */
	private DatagramPacket in;
	// endregion

	// region Construction!~
	/**
	 * Constructs a {@link NerdUdpSocket} with an empty port requested from the OS,
	 * the
	 * receiver thread of which will time-out every
	 * {@link NerdUdpSocket#DEFAULT_TIMEOUT} milliseconds.
	 *
	 * @implSpec {@link NerdUdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
	 */
	protected NerdUdpSocket() {
		this(0, NerdUdpSocket.DEFAULT_TIMEOUT);
	}

	/**
	 * Constructs a {@link NerdUdpSocket} with the specified port, the receiver
	 * thread
	 * of which will time-out every {@link NerdUdpSocket#DEFAULT_TIMEOUT}
	 * milliseconds.
	 *
	 * @implSpec {@link NerdUdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
	 */
	protected NerdUdpSocket(final int p_port) {
		this(p_port, NerdUdpSocket.DEFAULT_TIMEOUT);
	}

	/**
	 * Constructs a {@link NerdUdpSocket} with the specified socket.
	 */
	protected NerdUdpSocket(final DatagramSocket p_sock) {
		if (p_sock == null)
			throw new NullPointerException("Yo! That socket's `null`...");

		try {
			if (p_sock.getSoTimeout() == 0)
				p_sock.setSoTimeout(NerdUdpSocket.DEFAULT_TIMEOUT);
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		this.socket = p_sock;
		this.receiver = new ReceiverThread();
	}

	/**
	 * Constructs a {@link NerdUdpSocket} with the specified port and receiver
	 * thread
	 * timeout (in milliseconds).
	 *
	 * @apiNote This constructor used to try to force the OS into giving the port of
	 *          the user's choice. This functionality has now been split. Please see
	 *          {@link NerdUdpSocket#createSocketForcingPort(int, int)} and
	 *          {@link NerdUdpSocket#UdpSocket(DatagramSocket)}.
	 */
	protected NerdUdpSocket(final int p_port, final int p_timeout) {

		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket(p_port);
			sock.setSoTimeout(p_timeout);
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		this.socket = sock;

		// System.out.printf("Socket port: `%d`.%n", this.sock.getLocalPort());
		// System.out.println(this.sock.getLocalAddress());

		if (this.receiver == null)
			this.receiver = new ReceiverThread();

		this.onStart();
	}
	// endregion

	// region `public` and `static` method[s]!
	/**
	 * Tries to 'force' the OS into constructing a socket with the port specified
	 * using {@link DatagramSocket#setReuseAddress()}.
	 *
	 * @param p_port    The port to use,
	 * @param p_timeout The timeout for the port's receiving thread.
	 * @return A {@link java.net.DatagramSocket}.
	 */
	public static DatagramSocket createSocketForcingPort(final int p_port, final int p_timeout) {
		/* final */ DatagramSocket toRet = null;

		try {
			toRet = new DatagramSocket(p_port);
			toRet.setReuseAddress(true);
			toRet.setSoTimeout(p_timeout);
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		return toRet;
	}
	// endregion

	// region Callback methods to overload. These are what you get! LOOK HERE!
	/**
	 * Simply called by the constructor of {@link NerdUdpSocket}, really.
	 */
	protected abstract void onStart();

	/**
	 * @implNote
	 *           If you wish to convert any part of the received packet to a
	 *           {@link String}, use the constructor,
	 *           {@code String(byte[] bytes, int offset, int length)}.
	 * 
	 *           Following is example code to convert a packet to a
	 *           {@link String}:
	 * 
	 *           <pre>
	 *           new String(p_data, 0, p_data.length);
	 *           </pre>
	 * 
	 *           The last two arguments passed to this constructor specify a
	 *           boundary, which you are free to adjust!
	 *           <p>
	 *           The byte at the position of the first argument <i>is</i> also
	 *           decoded. So is also the case with the second argument, length.
	 *           All bytes counted, are decoded.
	 * 
	 * @param p_data is a {@code byte[]} <i>always</i> of length {@code 65535}. No
	 *               more, no less!
	 * @param p_ip   is the IP address of the sender, as a {@link String}.
	 * @param p_port is the port number this data packet was received on.
	 */
	protected abstract void onReceive(final byte[] p_data, final String p_ip, final int p_port);

	/**
	 * Called before {@link NerdUdpSocket#close()} closes the thread and socket.
	 */
	protected abstract /* `synchronized` */ void onClose(); // AYO, NOBODY CALL THIS UNSYNCED!
	// endregion

	// region Getters and setters. They're all `public`.
	public DatagramSocket getSocket() {
		return this.socket;
	}

	public void setSocket(final DatagramSocket p_sock) {
		this.receiver.stop();
		this.socket = p_sock;
		this.receiver = new ReceiverThread();
	}

	/**
	 * @return {@code -1} if there is a UDP error (A {@link SocketException} to be
	 *         specific).
	 */
	public int getTimeout() {
		try {
			return this.socket.getSoTimeout();
		} catch (final SocketException e) {
			// Hope this never happens!:
			e.printStackTrace();
			return -1;
		}
	}

	public void setTimeout(final int p_timeout) {
		try {
			this.socket.setSoTimeout(p_timeout);
		} catch (final SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tries to force the socket to use the specified port. If the port is not
	 * available, an address reuse request is performed.
	 * 
	 * @param p_port The port number!
	 */
	public void setPort(final int p_port) {
		try {
			final InetAddress previous = this.socket.getLocalAddress();
			final boolean receiverWasNull = this.receiver == null;
			// ^^^ Used when the function is called from constructors.

			if (!receiverWasNull)
				this.receiver.stop();
			this.socket.close();
			this.socket = new DatagramSocket(p_port, previous);
			this.socket.setReuseAddress(true);

			if (receiverWasNull)
				this.receiver = new ReceiverThread();

			System.out.printf("Successfully forced the port to: `%d`.%n", this.socket.getLocalPort());
		} catch (final SocketException e) {
			System.out.printf("Setting the port to `%d` failed!%n", p_port);
			System.out.printf("Had to revert to port `%d`...%n", this.socket.getLocalPort());
			e.printStackTrace();
		}
	}

	public InetAddress getIp() {
		return this.socket.getLocalAddress();
	}

	// These allow concurrent modification - bad!:
	public DatagramPacket getLastPacketSent() {
		return this.out;
	}

	// public DatagramPacket getLastPacketReceived() {
	// return this.in;
	// }
	// endregion

	// region Other `public` methods!:
	/**
	 * Calls {@link NerdUdpSocket#onReceive()} to simulate fake message receival.
	 */
	public void fakeReceive(final byte[] p_data, final String p_ip, final int p_port) {
		this.onReceive(p_data, p_ip, p_port);
	}

	// region UDP group operations overloads.
	/**
	 * Calls {@link NerdUdpSocket#joinGroup(SocketAddress, NetworkInterface)} with
	 * an
	 * instance of {@link InetSocketAddress} - effectively the same as
	 * calling {@link DatagramSocket#joinGroup(SocketAddress, NetworkInterface)} on
	 * the underlying {@link DatagramSocket}.
	 * 
	 * @see NerdUdpSocket#leaveGroup(String, int, NetworkInterface)
	 * @see NerdUdpSocket#joinGroup(SocketAddress, NetworkInterface)
	 * @see NerdUdpSocket#leaveGroup(SocketAddress, NetworkInterface)
	 */
	public void joinGroup(final String p_ip, final int p_port, final NetworkInterface p_netIf) throws IOException {
		this.joinGroup(new InetSocketAddress(p_ip, p_port), p_netIf);
	}

	/**
	 * Calls {@link NerdUdpSocket#leaveGroup(SocketAddress, NetworkInterface)} with
	 * an
	 * instance of {@link InetSocketAddress} - effectively the same as
	 * calling {@link DatagramSocket#leaveGroup(SocketAddress, NetworkInterface)} on
	 * the underlying {@link DatagramSocket}.
	 * 
	 * @see NerdUdpSocket#joinGroup(SocketAddress, NetworkInterface)
	 * @see NerdUdpSocket#leaveGroup(SocketAddress, NetworkInterface)
	 */
	public void leaveGroup(final String p_ip, final int p_port, final NetworkInterface p_netIf) throws IOException {
		this.socket.leaveGroup(new InetSocketAddress(p_ip, p_port), p_netIf);
	}
	// endregion

	// region `send()` overloads.
	/**
	 * Sends over a {@link DatagramPacket} using the internal
	 * {@link DatagramSocket}.
	 */
	public synchronized void send(final DatagramPacket p_packet) {
		// System.out.println("The socket sent some data!");
		try {
			// No worries here. Nobody can change the packet anymore!~
			this.out = p_packet;
			this.socket.send(p_packet);
		} catch (final IOException e) {
			// if (e instanceof UnknownHostException) {
			e.printStackTrace();
			// } else {
			// e.printStackTrace();
			// }
		}
	}

	/**
	 * Sends over a {@code byte[]} to the specified IP address and port.
	 */
	public synchronized void send(final byte[] p_data, final String p_ip, final int p_port) {
		// System.out.println("The socket sent some data!");
		try {
			// Okay, okay, look. These methods can be called over threads, so we better set
			// `this.out` LATER!
			final DatagramPacket toSend = new DatagramPacket(
					p_data, p_data.length, InetAddress.getByName(p_ip), p_port);
			this.socket.send(toSend);
			this.out = toSend;
		} catch (final IOException e) {
			// if (e instanceof UnknownHostException) {
			e.printStackTrace();
			// } else {
			// e.printStackTrace();
			// }
		}
	}

	/**
	 * Sends over a {@link String} converted to a {@code byte[]} using the
	 * {@code UTF-8} character set to the specified IP address and port.
	 */
	public synchronized void send(final String p_message, final String p_ip, final int p_port) {
		this.send(p_message.getBytes(StandardCharsets.UTF_8), p_ip, p_port);

		// VSCode, please allow comments without an
		// extra space, I beg you. I need it for my style! I DON'T insert spaces for
		// code-only comments!
	}
	// endregion

	/**
	 * Frees memory used by the operating system handle and any other resources the
	 * underlying {@link DatagramSocket} instance is using, prints the exception
	 * closing it results in (if it occurs), and stops the receiver thread.
	 */
	public synchronized void close() {
		this.onClose();
		this.setTimeout(0);
		this.receiver.stop();

		try {
			this.socket.setReuseAddress(false);
			this.socket.close();
		} catch (final SocketException e) {
			// That's basically re-printing the exception! NO!
			// e.printStackTrace();
		}

		// System.out.println("Socket closed...");
	}
	// endregion

	// region Stuff from `DatagramSocket`.
	public boolean getBroadcast() throws SocketException {
		return this.socket.getBroadcast();
	}

	public DatagramChannel getChannel() {
		return this.socket.getChannel();
	}

	public InetAddress getInetAddress() {
		return this.socket.getInetAddress();
	}

	public InetAddress getLocalAddress() {
		return this.socket.getLocalAddress();
	}

	public int getLocalPort() {
		return this.socket.getLocalPort();
	}

	public SocketAddress getLocalSocketAddress() {
		return this.socket.getLocalSocketAddress();
	}

	public <T> T getOption(final SocketOption<T> p_socketOption) throws IOException {
		return this.socket.getOption(p_socketOption);
	}

	public int getPort() {
		return this.socket.getPort();
	}

	public int getReceiveBufferSize() throws SocketException {
		return this.socket.getReceiveBufferSize();
	}

	public SocketAddress getRemoteSocketAddress() {
		return this.socket.getRemoteSocketAddress();
	}

	public boolean getReuseAddress() throws SocketException {
		return this.socket.getReuseAddress();
	}

	public int getSendBufferSize() throws SocketException {
		return this.socket.getSendBufferSize();
	}

	public int getSoTimeout() throws SocketException {
		return this.socket.getSoTimeout();
	}

	public int getTrafficClass() throws SocketException {
		return this.socket.getTrafficClass();
	}

	public boolean isBound() {
		return this.socket.isBound();
	}

	public boolean isClosed() {
		return this.socket.isClosed();
	}

	public boolean isConnected() {
		return this.socket.isConnected();
	}

	public void joinGroup(final SocketAddress p_address, final NetworkInterface p_networkInterface) throws IOException {
		this.socket.joinGroup(p_address, p_networkInterface);
	}

	public void leaveGroup(final SocketAddress p_address, final NetworkInterface p_networkInterface)
			throws IOException {
		this.socket.leaveGroup(p_address, p_networkInterface);
	}

	public void setBroadcast(final boolean p_state) throws SocketException {
		this.socket.setBroadcast(p_state);
	}

	public <T> DatagramSocket setOption(
			final SocketOption<T> p_socketOption, final T p_value) throws IOException {
		return this.socket.setOption(p_socketOption, p_value);
	}

	public void setReceiveBufferSize(final int p_size) throws SocketException {
		this.socket.setReceiveBufferSize(p_size);
	}

	public void setReuseAddress(final boolean p_state) throws SocketException {
		this.socket.setReuseAddress(p_state);
	}

	public void setSendBufferSize(final int p_size) throws SocketException {
		this.socket.setSendBufferSize(p_size);
	}

	public void setSoTimeout(final int p_timeout) throws SocketException {
		this.socket.setSoTimeout(p_timeout);
	}

	public void setTrafficClass(final int p_trafficClass) throws SocketException {
		this.socket.setTrafficClass(p_trafficClass);
	}

	public Set<SocketOption<?>> supportedOptions() {
		return this.socket.supportedOptions();
	}
	// endregion

}
