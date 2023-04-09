package com.brahvim.nerd_tests.scenes;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.io.net.tcp.NerdAbstractTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer.NerdClientSentTcpPacket;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TcpTestScene extends NerdScene {
	// I'm not using JSON here, which is a ***totally*** valid option here, thanks
	// to Processing!

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
		REJECTED(),
		SERVED_FOOD(),
	}
	// endregion

	private class RestaurantApi implements Consumer<NerdClientSentTcpPacket>,
			Function<NerdAbstractTcpClient, Consumer<NerdClientSentTcpPacket>> {

		// Message callback:
		@Override
		public void accept(final NerdClientSentTcpPacket p_packet) {
			final Query message = (Query) ByteSerial.fromBytes(p_packet.getData());
			final NerdTcpServer.NerdTcpServerClient client = p_packet.getSender();
			Response response = null;

			// System.out.println(
			// "`TcpTestScene.RestaurantApi::accept()` (Server message loop)");

			switch (message) {
				case ORDER_FOOD -> {
					response = Response.SERVED_FOOD;
					System.out.println("The server served food!");
				}
			}

			client.send(ByteSerial.toBytes(response));
		}

		// New connection callback:
		@Override
		public Consumer<NerdClientSentTcpPacket> apply(final NerdAbstractTcpClient p_client) {
			final boolean clientAccepted = SKETCH.random(1) < 0.5f;

			if (clientAccepted) {
				p_client.send(ByteSerial.toBytes(Response.ALLOWED));
				System.out.println("Ayy! A new client joined! Info: "
						+ p_client.getSocket().toString());
			} else
				System.out.println("The server rejected a connection.");

			// API Feature!: returning `null` rejects the connection,
			// Can also do it here, but we have this now!
			return clientAccepted ? this : null;
		}

	}

	@Override
	protected void setup(final SceneState p_state) {
		SKETCH.textSize(25);
		SKETCH.fullscreen = true;
		SKETCH.getCamera().pos.z = 500;
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
		final NerdTcpServer server = new NerdTcpServer(8080, new RestaurantApi());
		final HashSet<NerdTcpClient> clients = new HashSet<>();

		System.out.println();

		for (int i = 0; i < 5; i++)
			clients.add(new NerdTcpClient("127.0.0.1", 8080, (p) -> {
				final Response message = (Response) ByteSerial.fromBytes(p.getData());
				Query response = null;

				switch (message) {
					case ALLOWED -> {
						response = Query.ORDER_FOOD;
						System.out.println("Asked the server for food!");
					}
					case REJECTED, SERVED_FOOD -> {
						p.getReceivingClient().disconnect();
						System.out.println("Received the food, disconnecting now!");
					}
				}

				if (response != null)
					p.getReceivingClient().send(ByteSerial.toBytes(response));
			}));

		SKETCH.delay(50);
		System.out.println("Shutting server down...");
		server.shutdown();
		SKETCH.exit();
	}

}
