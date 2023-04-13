package com.brahvim.nerd.io.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * {@link UdpSocket} helps two applications running on different machines
 * connect via networks following the "User Datagram Protocol" and let them
 * listen to each other on a different thread for easier asynchronous
 * multitasking.<br>
 * <br>
 * Of course, it is based on classes from the
 * {@code java.net} package :)
 *
 * @author Brahvim Bhaktvatsal
 */

public class UdpSocket {

    // Concurrent stuff *haha:*
    /**
     * The {@link UdpSocket.ReceiverThread} class helps {@link UdpSocket}s receive
     * data on
     * a separate thread, aiding with application performance and modern
     * hardware programming practices. It may NOT, be useful on systems like
     * Android, where ALL networking tasks must be done asynchronously.
     *
     * @author Brahvim Bhaktvatsal
     * @see UdpSocket
     */
    public class ReceiverThread {

        // region Fields.
        /**
         * Please note that the specified standard buffer sizes are 4 bytes less than
         * what is actually allowed - the first 4 bytes hold the size of the data.
         */
        // Standard receive buffer sizes:
        public static final int UDP_PACKET_SAFE_SIZE = 564;
        public static final int UDP_PACKET_MAX_SIZE = 65_503;
        public static final int UDP_PACKET_MOST_SAFE_SIZE = 539;

        // You also need to fit the HEADER! The max is NOT `65535`!

        /**
         * Sets the size of the buffer (in bytes) data is received into. The maximum
         * possible size is {@link ReceiverThread#UDP_PACKET_MAX_SIZE} ({@code 65_507})
         * bytes.
         *
         * @apiNote Is {@link ReceiverThread#UDP_PACKET_MAX_SIZE} ({@code 65_507}) by
         *          default.
         *          Though <b>a higher value will use more
         *          bandwidth.</b> This number should not be something you need to
         *          worry about. Even if a dial-up router fragments the packet, the
         *          packet won't get corrupted. If even a single fragment fails to
         *          reach, the entire packet is discarded.
         * 
         * @implNote This <i>should</i> instead, be {@code 576}, <b><i>or even
         *           {@code 543}</b></i> by default.
         *           <br>
         *           </br>
         *           To know why, please see {@link<a href=
         *           "https://stackoverflow.com/a/9235558/">this</a>}.
         */
        @Deprecated
        public final int packetSize;

        /**
         * The {@code Thread} that handles the network's receive calls.
         *
         * @implSpec {@link UdpSocket.ReceiverThread#start()}
         *           should set this to be a daemon thread.
         * @see UdpSocket.ReceiverThread#start()
         * @see UdpSocket.ReceiverThread#stop()
         */
        private Thread thread; // Ti's but a daemon thread.

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
            this.packetSize = ReceiverThread.UDP_PACKET_MAX_SIZE;
            this.byteData = new byte[this.packetSize];

            this.thread = new Thread(this::receiverTasks);
            this.thread.setName("UdpSocketReceiverOnPort" + UdpSocket.this.getPort());
            this.thread.setDaemon(true); // The JVM can shut down without waiting for this thread to.
            this.thread.start();
        }

        private void receiverTasks() {
            // We got some work?
            while (!Thread.interrupted()) {
                try {
                    UdpSocket.this.in = new DatagramPacket(this.byteData, this.byteData.length);
                    if (UdpSocket.this.sock != null)
                        UdpSocket.this.sock.receive(UdpSocket.this.in); // Fetch it well!
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
                if (UdpSocket.this.in != null) {
                    final InetAddress addr = UdpSocket.this.in.getAddress();

                    if (addr == null)
                        continue;

                    // System.out.println("Calling `onReceive()`!");

                    // The user's code can throw exceptions that could pause our
                    // thread.
                    // :)

                    // ..Gotta handle those!:

                    try {
                        final byte[] copy = new byte[UdpSocket.this.in.getLength()];

                        System.arraycopy(this.byteData, 0, copy, 0, this.byteData.length);

                        // Super slow `memset()`...
                        // for (int i = 0; i < byteData.length; i++)
                        // byteData[i] = 0;

                        // ~~...but I just didn't want to use `System.arraycopy()`
                        // with a freshly allocated array. What a WASTE!~~

                        this.byteData = new byte[this.packetSize];
                        // PS It IS TWO WHOLE ORDERS OF MAGNITUDE faster to allocate
                        // than to do a loop and set values.
                        // I'm only worried about de-allocation.

                        UdpSocket.this.onReceive(copy,
                                addr.toString().substring(1),
                                UdpSocket.this.in.getPort());
                    } catch (final Exception e) {
                        e.printStackTrace();
                    } catch (final Error e) { // Also, ERRORS! ...if possible, that is.
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
     * The default timeout value, in milliseconds, for each {@link UdpSocket}. Used
     * if a timeout value is not specified in the constructor.
     *
     * @implSpec Should be {@code 32}.
     */
    public static final int DEFAULT_TIMEOUT = 32;

    /**
     * The internal, {@code private} {@link UdpSocket.ReceiverThread} instance.
     * In abstract words, it handles threading for receiving messages.
     */
    protected ReceiverThread receiver;

    /**
     * The internal {@link DatagramSocket} that takes care of
     * networking.<br>
     * <br>
     * If you need to change it, consider using the
     * {@link UdpSocket#setSocket(DatagramSocket)}
     * method (it pauses the receiving thread, swaps the socket, and resumes
     * listening).<br>
     * <br>
     * {@link UdpSocket#getSocket()} <b>should</b> be used for equality checks,
     * etcetera.
     */
    private DatagramSocket sock;

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
     * Constructs a {@link UdpSocket} with an empty port requested from the OS, the
     * receiver thread of which will time-out every
     * {@link UdpSocket#DEFAULT_TIMEOUT} milliseconds.
     *
     * @implSpec {@link UdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
     */
    public UdpSocket() {
        this(0, UdpSocket.DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a {@link UdpSocket} with the specified port, the receiver thread
     * of which will time-out every {@link UdpSocket#DEFAULT_TIMEOUT}
     * milliseconds.
     *
     * @implSpec {@link UdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
     */
    public UdpSocket(final int p_port) {
        this(p_port, UdpSocket.DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a {@link UdpSocket} with the specified socket.
     */
    public UdpSocket(final DatagramSocket p_sock) {
        this.sock = p_sock;
        this.receiver = new ReceiverThread();
    }

    /**
     * Constructs a {@link UdpSocket} with the specified port and receiver thread
     * timeout (in milliseconds).
     *
     * @apiNote This constructor used to try to force the OS into giving the port of
     *          the user's choice. This functionality has now been split. Please see
     *          {@link UdpSocket#createSocketForcingPort(int, int)} and
     *          {@link UdpSocket#UdpSocket(DatagramSocket)}.
     */
    public UdpSocket(final int p_port, final int p_timeout) {
        try {
            this.sock = new DatagramSocket(p_port);
            this.sock.setSoTimeout(p_timeout);
        } catch (final SocketException e) {
            e.printStackTrace();
        }

        // System.out.printf("Socket port: `%d`.\n", this.sock.getLocalPort());
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
     * Simply called by the constructor of {@link UdpSocket}, really.
     */
    protected void onStart() {
    }

    /**
     * @param p_data Always of length {@code 65535}. No more, no less!
     *               If you wish to make a string out of it, use the constructor
     *               {@code new String(p_data, 0, p_data.length)}. The {@code 0} is
     *               the first character of the string.
     * @apiNote {@code public} so you can generate fake events ;)
     */
    public void onReceive(final byte[] p_data, final String p_ip, final int p_port) {
    }

    /**
     * Called before {@code .close()} closes the thread and socket.
     */
    protected void onClose() {
    }
    // endregion

    // region Getters and setters. They're all `public`.
    public DatagramSocket getSocket() {
        return this.sock;
    }

    public void setSocket(final DatagramSocket p_sock) {
        this.receiver.stop();
        this.sock = p_sock;
        this.receiver = new ReceiverThread();
    }

    /**
     * @return {@code -1} if there is a UDP error (A {@link SocketException} to be
     *         specific).
     */
    public int getTimeout() {
        try {
            return this.sock.getSoTimeout();
        } catch (final SocketException e) {
            // Hope this never happens!:
            e.printStackTrace();
            return -1;
        }
    }

    public void setTimeout(final int p_timeout) {
        try {
            this.sock.setSoTimeout(p_timeout);
        } catch (final SocketException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return this.sock.getLocalPort();
    }

    /**
     * Tries to force the socket to use the specified port. If the port is not
     * available, an address reuse request is performed.
     * 
     * @param p_port The port number!
     */
    public void setPort(final int p_port) {
        try {
            final InetAddress previous = this.sock.getLocalAddress();
            final boolean receiverWasNull = this.receiver == null;
            // ^^^ Used when the function is called from constructors.

            if (!receiverWasNull)
                this.receiver.stop();
            this.sock.close();

            this.sock = new DatagramSocket(p_port, previous);
            this.sock.setReuseAddress(true);

            if (receiverWasNull)
                this.receiver = new ReceiverThread();

            System.out.printf("Successfully forced the port to: `%d`.\n", this.sock.getLocalPort());
        } catch (final SocketException e) {
            System.out.printf("Setting the port to `%d` failed!\n", p_port);
            System.out.printf("Had to revert to port `%d`...\n", this.sock.getLocalPort());
            e.printStackTrace();
        }
    }

    public InetAddress getIp() {
        return this.sock.getLocalAddress();
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
     * Sends over a {@code byte[]} to the specified IP address and port.
     */
    public void send(final byte[] p_data, final String p_ip, final int p_port) {
        // System.out.println("The socket sent some data!");
        try {
            this.sock.send(this.out = new DatagramPacket(
                    p_data, p_data.length, InetAddress.getByName(p_ip), p_port));
        } catch (final IOException e) {
            // if (e instanceof UnknownHostException) {
            e.printStackTrace();
            // } else {
            // e.printStackTrace();
            // }
        }
    }

    /**
     * Sends over a {@code String} converted to a {@code byte[]} using the
     * {@code UTF-8} character set to the specified IP address and port.
     */
    public void send(final String p_message, final String p_ip, final int p_port) {
        this.send(p_message.getBytes(StandardCharsets.UTF_8), p_ip, p_port);

        // VSCode, please allow comments without an
        // extra space, I beg you. I need it for my style! I DON'T insert spaces for
        // code-only comments!
    }

    /**
     * Frees memory used by the Operating System handle and any other resources the
     * underlying {@code DatagramSocket} instance is using, prints the exception
     * closing it results in (if it occurs), and stops the receiver thread.
     */
    public void close() {
        this.onClose();
        this.setTimeout(0);
        this.receiver.stop();

        try {
            this.sock.setReuseAddress(false);
            this.sock.close();
        } catch (final SocketException e) {
            // That's basically re-printing the exception! NO!
            // e.printStackTrace();
        }

        // System.out.println("Socket closed...");
    }
    // endregion

}
