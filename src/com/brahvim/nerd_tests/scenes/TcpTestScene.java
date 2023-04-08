package com.brahvim.nerd_tests.scenes;

import java.nio.charset.StandardCharsets;

import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TcpTestScene extends NerdScene {

	@Override
	protected void setup(final SceneState p_state) {
		final NerdTcpServer server = new NerdTcpServer(8080).onNewConnection((c) -> {
			System.out.println("Ayy! A new client joined! Info: " + c.getSocket().toString());
			return (p) -> System.out.printf("Client messaged: `%s`, using `%d` bytes.\n",
					new String(p.getData(), StandardCharsets.UTF_8), p.getDataLength());
		});

		final NerdTcpClient client = new NerdTcpClient("127.0.0.1", 8080);

		client.send("Hey there - \":D!~");
		// SKETCH.delay(5_000);
		// client.disconnect();

		System.out.println("Shutting server down...");
		server.shutdown();
		SKETCH.exit();
	}

}
