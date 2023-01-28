package com.brahvim.nerd.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
    // region Fields:
    /**
     * The default timeout value, in milliseconds, for each {@code UdpSocket}. Used
     * if a timeout value is not specified in the constructor.
     *
     * @implSpec Should be {@code 32}.
     */
    public final static int DEFAULT_TIMEOUT = 32;

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
     * The internal, {@code private} {@link UdpSocket.Receiver} instance.
     * In abstract words, it handles threading for receiving messages.
     */
    protected Receiver receiver;

    /**
     * Holds the previous {@code DatagramPacket} that was received.
     */
    private DatagramPacket in;

    /**
     * Holds the previous {@code DatagramPacket} that was sent.
     */
    private DatagramPacket out;
    // endregion

    // Concurrent stuff *haha:*
    /**
     * The {@link UdpSocket.Receiver} class helps {@code UdpSocket}s receive data on
     * a separate thread, aiding with application performance and modern
     * hardware programming practices. It may NOT, be useful on systems like
     * Android, where ALL networking tasks must be done asynchronously.
     *
     * @author Brahvim Bhaktvatsal
     * @see UdpSocket
     */
    public class Receiver {
        // region Fields.
        private static int NUMBER_OF_THREADS = 0;

        // Standard array sizes:
        public final static int PACKET_LEAST_SIZE = 543;
        public final static int PACKET_MAX_SIZE = 65535;
        public final static int PACKET_RECOMMENDED_SIZE = 576;

        /**
         * The {@code Thread} that handles the network's receive calls.
         *
         * @implSpec {@link UdpSocket.Receiver#start()}
         *           should set this to be a daemon thread.
         * @see UdpSocket.Receiver#start()
         * @see UdpSocket.Receiver#stop()
         */
        private Thread thread; // Ti's but a daemon thread.

        /**
         * The {@link UdpSocket} instance using this
         * {@link UdpSocket.Receiver} instance.
         */
        private final UdpSocket parent;

        /**
         * Sets the size of the buffer (in bytes) data is received into. The maximum
         * possible size is {@code 65535} ({@link Receiver#PACKET_MAX_SIZE}) bytes.
         *
         * @apiNote Is {@code 65535} ({@link Receiver#PACKET_MAX_SIZE}) by default.
         * @implNote This <i>should</i> instead, be {@code 576} by default.
         *           To know why, please see {@link<a href=
         *           "https://stackoverflow.com/a/9235558/13951505">this</a>}.
         *
         *           <br>
         *           </br>
         *           This field should not be something you need to worry about. Modern
         *           computers are fast enough already. A {@code 65}KB allocation
         *           cannot be a problem when your application already takes
         *           {@code 200}MB in RAM, GC'ing away much of it every second,
         *           and still sailing well.
         */
        @Deprecated
        // PS The reason mentioned for deprecation is *probably* one of
        // ***the worst*** decisions of my life! :joy:
        public Integer packetMaxSize = Receiver.PACKET_MAX_SIZE;
        // ^^^ PS a more precise number is `543` bytes.
        // Update (as of `21 December, 2022`): It. Doesn't. MATTER.
        // Just come up with a fixed the packet size! The rest is fine!

        /**
         * An internal {@code private} variable that lets the thread know whether or not
         * to continue execution, so that it can be joined back into the current
         * (usually main) thread.
         *
         * @see UdpSocket.Receiver#start()
         * @see UdpSocket.Receiver#stop()
         */
        private boolean doRun;

        /**
         * This {@code Runnable} defines the actual code executed in the
         * {@link UdpSocket.Receiver#thread} thread.
         * <p>
         * You may choose to modify it since it has been declared {@code public}.
         *
         * @see UdpSocket.Receiver#Receiver(UdpSocket)
         */
        public Runnable task;
        // endregion

        /**
         * Given the 'parent' {@link UdpSocket} that objects of this class are to
         * be attached to, this class will handle receiving UDP packets.
         *
         * @param p_parent The {@code UdpSocket} instance.
         * @implNote {@code p_parent} may not be used since {@code Receiver} is a class
         *           nested inside {@link UdpSocket}
         */
        private Receiver(UdpSocket p_parent) {
            Receiver.NUMBER_OF_THREADS++;
            this.parent = p_parent;

            // Scope is what matters.
            // That's also why I use Hungarian notation.
            final UdpSocket.Receiver REC = this;
            final UdpSocket PARENT = this.parent;

            // Sure, we have lambda expressions, but I won't be using them, haha.
            this.task = new Runnable() {
                // No mutual exclusion locking here, by the way. This
                // `short`(`byteArrayMaxSize`) *should* be fine here?
                // Ehh, it's fine. Is it THAT important to use `AtomicShort` right-away anyway?
                // (...or `volatile`?)

                private byte[] byteData = new byte[REC.packetMaxSize /* 65535 */];
                // ^^^ B I G ___ A L L O C A T I O N !

                @Override
                public void run() {
                    // We got some work?
                    while (REC.doRun) {
                        try {
                            PARENT.in = new DatagramPacket(byteData, byteData.length);
                            if (PARENT.sock != null)
                                PARENT.sock.receive(PARENT.in); // Fetch it well!
                        } catch (IOException e) {
                            if (e instanceof SocketTimeoutException) {
                                // ¯\_(ツ)_/¯
                                // System.out.println("Timeout ended! Continuing...");
                            } else if (e instanceof SocketException) {
                                REC.doRun = false;
                                return;
                            } else
                                e.printStackTrace(); // ¯\_(ツ)_/¯
                        }

                        // Callback!:
                        if (PARENT.in != null) {
                            InetAddress addr = PARENT.in.getAddress();

                            if (addr == null)
                                continue;

                            // System.out.println("Calling `onReceive()`!");

                            // The user's code can throw exceptions that pause our thread :)
                            try {
                                byte[] copy = new byte[byteData.length];

                                System.arraycopy(byteData, 0, copy, 0, byteData.length);

                                // Super slow `memset()`...
                                // for (int i = 0; i < byteData.length; i++)
                                // byteData[i] = 0;

                                // ~~...but I just didn't want to use `System.arraycopy()`
                                // with a freshly allocated array. What a WASTE!~~

                                this.byteData = new byte[REC.packetMaxSize];
                                // PS It IS TWO WHOLE ORDERS OF MAGNITUDE faster to allocate
                                // than to do a loop and set values.
                                // I'm only worried about de-allocation.

                                PARENT.onReceive(copy,
                                        addr.toString().substring(1),
                                        PARENT.in.getPort());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } // End of `if (PARENT.in != null)`.

                    } // End of `while` loop,
                } // End of `run()`,
            }; // End of the `Runnable` constructor!... :D!~

            // GO! ":D
            this.start();
        }

        public static int getNumberOfReceivers() {
            // LOL (Autocomplete moment):
            return com.brahvim.nerd.io.UdpSocket.Receiver.NUMBER_OF_THREADS;
        }

        public Thread getThread() {
            return this.thread;
        }

        public void start() {
            this.doRun = true;
            // Yes. If `task` is `null`, throw an error.
            this.thread = new Thread(this.task);

            this.thread.setName("UDP Socket Receiver Thread " + Receiver.NUMBER_OF_THREADS);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        public void stop() {
            Receiver.NUMBER_OF_THREADS--;
            this.doRun = false;
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // region Construction!~
    /**
     * Constructs a {@code UdpSocket} with an empty port requested from the OS, the
     * receiver thread of which will time-out every
     * {@link UdpSocket#DEFAULT_TIMEOUT} milliseconds.
     *
     * @implSpec {@link UdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
     */
    public UdpSocket() {
        this(0, UdpSocket.DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a {@code UdpSocket} with the specified port, the receiver thread
     * of which will time-out every {@link UdpSocket#DEFAULT_TIMEOUT}
     * milliseconds.
     *
     * @implSpec {@link UdpSocket#DEFAULT_TIMEOUT} should be {@code 32}.
     */
    public UdpSocket(int p_port) {
        this(p_port, UdpSocket.DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a {@code UdpSocket} with the specified socket.
     */
    public UdpSocket(DatagramSocket p_sock) {
        this.sock = p_sock;
        this.receiver = new Receiver(this);
    }

    /**
     * Constructs a {@code UdpSocket} with the specified port and receiver thread
     * timeout (in milliseconds).
     *
     * @apiNote This constructor used to try to force the OS into giving the port of
     *          the user's choice. This functionality has now been split. Please see
     *          {@link UdpSocket#createSocketForcingPort(int, int)} and
     *          {@link UdpSocket#UdpSocket(DatagramSocket)}.
     */
    public UdpSocket(int p_port, int p_timeout) {
        try {
            this.sock = new DatagramSocket(p_port);
            this.sock.setSoTimeout(p_timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // region Old "force port" method:
        // try {
        // this.sock = new DatagramSocket(p_port);
        // if (this.getPort() != p_port) {
        // System.out.printf("Could not bind to port `%d`. Forcing the OS...\n",
        // p_port);
        // this.setPort(p_port);
        // }
        // this.sock.setSoTimeout(p_timeout);
        // } catch (SocketException e) {
        // e.printStackTrace();
        // try {
        // if (this.sock != null)
        // this.sock.close();
        // this.sock = new DatagramSocket(null);
        // this.sock.setReuseAddress(true);
        // this.sock.bind(new InetSocketAddress(p_port));
        // this.sock.setSoTimeout(p_timeout);
        // } catch (SocketException f) {
        // f.printStackTrace();
        // }
        // }
        // endregion

        // System.out.printf("Socket port: `%d`.\n", this.sock.getLocalPort());
        // System.out.println(this.sock.getLocalAddress());

        if (this.receiver == null)
            this.receiver = new Receiver(this);
        this.onStart();
    }
    // endregion

    // region `public` and `static`!:
    /**
     * Tries to 'force' the OS into constructing a socket with the port specified
     * using {@code DatagramSocket.setReuseAddress(boolean)}.
     *
     * @param p_port    The port to use,
     * @param p_timeout The timeout for the port's receiving thread.
     * @return A {@code java.net.DatagramSocket}.
     */
    public static DatagramSocket createSocketForcingPort(int p_port, int p_timeout) {
        DatagramSocket toRet = null;

        try {
            toRet = new DatagramSocket(null);
            toRet.setReuseAddress(true);
            toRet.bind(new InetSocketAddress(p_port));
            toRet.setSoTimeout(p_timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return toRet;
    }
    // endregion

    // region Callbacks. These are what you get. LOOK HERE!

    /**
     * Simply called by the constructor of {@code UdpSocket}, really.
     */
    protected void onStart() {
    }

    /**
     * @param p_data Always of length {@code 65535}. No more, no less!
     *               If you wish to make a string out of it, use the constructor
     *               {@code new String(p_data, 0, p_data.length)}. The {@code 0} is
     *               the first character of the string.
     * @apiNote {@code public} so you can generate fake events ;) *
     */
    public void onReceive(byte[] p_data, String p_ip, int p_port) {
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

    public void setSocket(DatagramSocket p_sock) {
        this.receiver.stop();
        this.sock = p_sock;
        this.receiver.start();
    }

    public int getTimeout(int p_timeout) {
        try {
            return this.sock.getSoTimeout();
        } catch (SocketException e) {
            // Hope this never happens!:
            e.printStackTrace();
            return -1;
        }
    }

    public void setTimeout(int p_timeout) {
        try {
            this.sock.setSoTimeout(p_timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return this.sock.getLocalPort();
    }

    public void setPort(int p_port) {
        try {
            InetAddress previous = this.sock.getLocalAddress();
            boolean receiverWasNull = receiver == null;
            // ^^^ Used when the function is called from constructors.

            if (!receiverWasNull)
                this.receiver.stop();
            this.sock.close();

            this.sock = new DatagramSocket(null);
            this.sock.setReuseAddress(true);
            this.sock.bind(new InetSocketAddress(previous, p_port));

            if (receiverWasNull)
                this.receiver = new Receiver(this);

            this.receiver.start();

            System.out.printf("Successfully forced the port to: `%d`.\n", this.sock.getLocalPort());
        } catch (SocketException e) {
            System.out.printf("Setting the port to `%d` failed!\n", p_port);
            System.out.printf("Had to revert to port `%d`...\n", this.sock.getLocalPort());
            e.printStackTrace();
        }
    }

    public InetAddress getIp() {
        return this.sock.getLocalAddress();
    }

    public DatagramPacket getLastPacketSent() {
        return this.out;
    }

    public DatagramPacket getLastPacketReceived() {
        return this.in;
    }
    // endregion

    // region Other `public` methods!:
    /**
     * Sends over a {@code byte[]} to the specified IP address and port.
     */
    public void send(byte[] p_data, String p_ip, int p_port) {
        // System.out.println("The socket sent some data!");
        try {
            this.sock.send(out = new DatagramPacket(
                    p_data, p_data.length, InetAddress.getByName(p_ip), p_port));
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends over a {@code String} converted to a {@code byte[]} using the
     * {@code UTF-8} character set to the specified IP address and port.
     */
    public void send(String p_message, String p_ip, int p_port) {
        this.send(p_message.getBytes(StandardCharsets.UTF_8),
                p_ip, p_port);

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

        // LIAR:
        // No need to stop the receiving thread!

        this.receiver.stop();

        try {
            this.sock.setReuseAddress(false);
            this.sock.close();
        } catch (SocketException e) {
            // That's basically re-printing the exception! NO!
            // e.printStackTrace();
        }
        // System.out.println("Socket closed...");
    }
    // endregion

}
