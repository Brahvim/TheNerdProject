package com.brahvim.nerd_tests.scenes;

import java.util.HashSet;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TcpTestScene extends NerdScene {
	// I'm not using JSON here, which is a ***totally*** valid option here, thanks
	// to Processing!

	// region Message `enum`s.
	private enum Query {
		LET_ME_IN(),
		ORDER_FOOD(),
	}

	private enum Response {
		ALLOWED(),
		REJECTED(),
		SERVED_FOOD(),
	}
	// endregion

	@Override
	protected void setup(final SceneState p_state) {
		final NerdTcpServer server = new NerdTcpServer(8080, (c) -> {
			System.out.println("Ayy! A new client joined! Info: " + c.getSocket().toString());
			return (p) -> {
				final Query message = (Query) ByteSerial.fromBytes(p.getData());
				final NerdTcpServer.NerdTcpServerClient client = p.getSender();
				final Response response = switch (message) {
					default -> null;
					case ORDER_FOOD -> Response.SERVED_FOOD;
					case LET_ME_IN -> SKETCH.random(1) < 0.5f ? Response.ALLOWED : Response.REJECTED;
				};

				client.send(ByteSerial.toBytes(response));

				// final var client = p.getSender();
				// client.removeAllMessageCallbacks();
				// client.addMessageCallback(p2 -> System.out.println("New client callback!"));
				// System.out.printf("Client messaged: `%s`, using `%d` bytes.\n",
				// new String(p.getData(), StandardCharsets.UTF_8), p.getDataLength());
			};
		});

		final HashSet<NerdTcpClient> clients = new HashSet<>();

		for (int i = 0; i < 5; i++) {
			final NerdTcpClient c = new NerdTcpClient("127.0.0.1", 8080, (p) -> {
				final Response message = (Response) ByteSerial.fromBytes(p.getData());
				Query response = null;

				switch (message) {
					case ALLOWED -> response = Query.ORDER_FOOD;
					case REJECTED, SERVED_FOOD -> p.getReceivingClient().disconnectImpl();
				}

				if (response != null)
					p.getReceivingClient().send(ByteSerial.toBytes(response));
			});
			clients.add(c); // ...Ask the server to let the client in!
			c.send(ByteSerial.toBytes(Query.LET_ME_IN));
		}

		SKETCH.delay(10_000);
		System.out.println("Shutting server down...");
		server.shutdown();
		SKETCH.exit();
	}

}
