package com.brahvim.nerd_demos.scenes;

import java.util.function.Consumer;

import com.brahvim.nerd.api.scene_api.NerdScene;
import com.brahvim.nerd.api.scene_api.NerdSceneState;
import com.brahvim.nerd.io.NerdByteSerial;
import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer.NerdClientSentTcpPacket;

public class TcpDemoScene extends NerdScene {
	// I'm not using JSON here, which is *the* standard for communication nowadays.
	// Thanks to Processing, however, you can (the `processing.data` package)!

	// region Fields!
	public final String MESSAGE = """
			\n
			======================================================
			Welcome to the `RestaurantApi` demo!
			This demo showcases the ease of using Nerd's TCP API!
			Pressing 'G' should start the demo and exit this program.
			Please read the logs in the developer console upon its completion.
			======================================================
			\n""";
	// endregion

	// region Message `enum`s.
	private enum Query {
		ORDER_FOOD(),
	}

	private enum Response {
		ALLOWED(),
		// REJECTED(),
		SERVED_FOOD(),
	}
	// endregion

	private class RestaurantApi implements Consumer<NerdClientSentTcpPacket> {

		@Override
		public void accept(final NerdClientSentTcpPacket p_packet) {
			// Get the client's message:
			final Query message = (Query) NerdByteSerial.fromBytes(p_packet.getData());
			final NerdTcpServer.NerdTcpServerClient client = p_packet.getSender();
			Response response = null; // In here, we store our response!

			switch (message) {
				// If food is ordered, we serve.
				case ORDER_FOOD -> response = Response.SERVED_FOOD;
			}

			// Send the response over!
			client.send(NerdByteSerial.toBytes(response));
		}

	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		SKETCH.textSize(25);
		WINDOW.fullscreen = true;
		SKETCH.getCamera().getPos().z = 500;
	}

	@Override
	protected void draw() {
		SKETCH.background(0);
		SKETCH.text(this.MESSAGE, 0, 0);
	}

	@Override
	public void keyPressed() {
		if (SKETCH.key == 'g' || SKETCH.key == 'G')
			this.netTest();
	}

	private void netTest() {
		// Start a TCP server on the given port and check for clients to join!:
		final NerdTcpServer server = new NerdTcpServer(8080,
				// `c` holds the client object that wishes to join.
				c -> {
					// ...Let clients be rejected by chance:
					final boolean clientAccepted = SKETCH.random(1) < 0.5f;

					if (clientAccepted) {
						// This client got accepted - hooray! Tell it!:
						c.send(NerdByteSerial.toBytes(Response.ALLOWED));
						System.out.println("Ayy! A new client joined! Info: "
								+ c.getSocket().toString());
					} else // Tell us that it got rejected otherwise.
						System.out.println("The server rejected a connection.");

					// Returning `null` or calling `c.disconnect()` should disconnect.
					// If we accept this client, we return a listener to listen to its messages!:
					return clientAccepted ? new RestaurantApi() : null;
				});

		// Now, we start 5 clients to connect to the server!

		for (int i = 0; i < 5; i++)
			new NerdTcpClient("127.0.0.1", 8080,
					// `p` holds the packet of data received!:
					p -> {
						// Get the server's message,
						final Response message = (Response) NerdByteSerial.fromBytes(p.getData());
						Query response = null; // We'll store our response here.

						switch (message) {
							case ALLOWED -> { // If the server happily allows us, we order!:
								response = Query.ORDER_FOOD;
								System.out.println("Asked the server for food!");
							}
							case SERVED_FOOD -> { // If we're served food, we happily take it and go!:
								p.getReceivingClient().disconnect();
								System.out.println("Received the food, disconnecting now!");
							}
						}

						if (response != null)
							p.getReceivingClient().send(NerdByteSerial.toBytes(response));
					});

		SKETCH.delay(50);
		System.out.println("Great day! The restaurant served well.");
		server.shutdown();
		SKETCH.exit();
	}

}
