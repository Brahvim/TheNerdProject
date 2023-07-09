package com.brahvim.nerd_demos.scenes.net_demo_scenes;

import java.util.function.Consumer;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.NerdByteSerialUtils;
import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer.NerdClientSentTcpPacket;

public class TcpDemoScene extends NerdScene {

	// I'm not using JSON here, which is *the* standard for communication nowadays.
	// Thanks to Processing, however, you can (using the `processing.data` package)!

	// region Fields!
	public static final String MESSAGE = String.format("""
			====================================================================
			Welcome to the `RestaurantApi` demo!
			This demo showcases the ease of using Nerd's TCP API!
			Pressing `G` should start the demo and exit this program.
			Please read the logs in the developer console upon its completion.
			Try modifying `%s::netTest()` to experiment with it and learn more!
			====================================================================
			""", TcpDemoScene.class.getSimpleName());
	// endregion

	// region Message `enum`s.
	private enum ClientQuery {
		ORDER_FOOD(),
	}

	private enum ServerResponse {
		ALLOWED(),
		// REFUSED(),
		SERVED_FOOD(),
	}
	// endregion

	private class RestaurantApi implements Consumer<NerdClientSentTcpPacket> {

		@Override
		public void accept(final NerdClientSentTcpPacket p_packet) {
			// Get the client's message:
			final ClientQuery message = NerdByteSerialUtils.fromBytes(p_packet.getData());
			final NerdTcpServer.NerdTcpServerClient client = p_packet.getSender();
			ServerResponse response = null; // In here, we store our response!

			switch (message) {
				// If food is ordered, we serve.
				case ORDER_FOOD -> response = ServerResponse.SERVED_FOOD;
			}

			// Send the response over!
			client.send(NerdByteSerialUtils.toBytes(response));
		}

	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		GRAPHICS.textSize(25);
		WINDOW.fullscreen = true;
		GRAPHICS.currentCamera.getPos().z = 500;
	}

	@Override
	protected void draw() {
		GRAPHICS.background(0);
		GRAPHICS.text(TcpDemoScene.MESSAGE, 0, 0);
	}

	@Override
	public void keyPressed() {
		if (INPUT.key == 'g' || INPUT.key == 'G')
			this.netTest();
	}

	private void netTest() {
		// Start a TCP server on the given port and check for clients to join!:
		final NerdTcpServer server = new NerdTcpServer(8080);

		server.setClientInvitationCallback(
				// Yes, you can directly pass this lambda into the constructor!
				// `c` holds the client object that wishes to join.
				c -> {
					// ...Let clients be refused by chance:
					final boolean clientAccepted = SKETCH.random(1) < 0.5f;

					if (clientAccepted) {
						// This client got accepted - hooray! Tell it!:
						c.send(NerdByteSerialUtils.toBytes(ServerResponse.ALLOWED));
						System.out.println("Ayy! A new client joined! Info: "
								+ c.getSocket().toString());
					} else // Tell us that it got refused otherwise.
						System.out.println("The server refused a connection.");

					// Returning `null` or calling `c.disconnect()` should disconnect.
					// If we accept this client, we return a listener to listen to its messages!:
					return clientAccepted;
				});

		server.addMessageReceivedCallback(new RestaurantApi());

		// Now, we start 5 clients to connect to the server!

		for (int i = 0; i < 5; i++)
			new NerdTcpClient(
					// Info on the server to connect to:
					"127.0.0.1", 8080,
					// Following, is a callback to receive messages from the server.
					// `p` holds the packet of data received!:
					p -> {
						// Get the server's message,
						final ServerResponse message = (ServerResponse) NerdByteSerialUtils.fromBytes(p.getData());
						ClientQuery response = null; // We'll store our response here.

						switch (message) {
							case ALLOWED -> { // If the server happily allows us, we order!:
								response = ClientQuery.ORDER_FOOD;
								System.out.println("Asked the server for food!");
							}
							case SERVED_FOOD -> { // If we're served food, we happily take it and go!:
								p.getReceivingClient().disconnect();
								System.out.println("Received the food, disconnecting now!");
							}
						}

						if (response != null)
							p.getReceivingClient().send(NerdByteSerialUtils.toBytes(response));
					});

		SKETCH.delay(50);
		System.out.println("Great day! The restaurant served well.");
		server.shutdown();
		SKETCH.exit();
	}

}
