package com.brahvim.nerd_demos.scenes;

import java.nio.charset.StandardCharsets;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.net.NerdUdpSocket;

public class UdpDemoScene extends NerdScene {

    // region Fields!
    public static final String MESSAGE = """
            ======================================================
            Welcome to the `UdpDemoScene`!
            This demo showcases the ease of using Nerd's UDP API!
            Pressing `G` should start the demo and exit this program.
            Please read the logs in the developer console upon its completion.
            ======================================================
            """;
    // endregion

    @Override
    protected void setup(final NerdSceneState p_state) {
        GRAPHICS.textSize(25);
        WINDOW.fullscreen = true;
        GRAPHICS.currentCamera.getPos().z = 500;
    }

    @Override
    protected void draw() {
        GRAPHICS.background(0);
        GRAPHICS.text(UdpDemoScene.MESSAGE, 0, 0);
    }

    @Override
    public void keyPressed() {
        if (INPUT.key == 'g' || INPUT.key == 'G')
            this.netTest();
    }

    private void netTest() {
        final NerdUdpSocket socket1 = UdpDemoScene.createUdpSocketLoggingTheName("Socket-1"),
                socket2 = UdpDemoScene.createUdpSocketLoggingTheName("Socket-2");

        socket1.send(
                "Hello from `Socket-1`!",
                "127.0.0.1", // **Can also be an empty string, apparently!**
                socket2.getLocalPort());
        socket2.shutdown();
        socket1.shutdown();
        SKETCH.exit();
    }

    private static NerdUdpSocket createUdpSocketLoggingTheName(final String p_name) {
        return new NerdUdpSocket() {

            @Override
            protected void onStart() {
                System.out.println("`Started `" + p_name + "`.");
            }

            @Override
            protected void onClose() {
                System.out.println("`Closed `" + p_name + "`.");
            }

        }.addReceivingCallback((d, a, p) -> // "dap" and not "dip" - 'a' is for "address" here!
        System.out.printf("Received message \"%s\" from IP `%s`, on port `%d`.%n",
                new String(d, StandardCharsets.UTF_8), a, p));
    }

}
