package com.ikeirnez.pluginmessageframework.bungeecord;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.ikeirnez.pluginmessageframework.DummyGateway;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketIP;
import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Test the functionality of BungeeCord packets.
 */
public class BungeeCordPacketTest {

    private final GatewaySupport<String> gatewaySupport = new DummyGateway();
    private final String sender = "SomeSenderLel";

    // testing variables
    private final String host = "127.0.0.1";
    private final int port = 25565;

    private boolean packetReceived = false;

    @Before
    public void setUp() {
        gatewaySupport.setPayloadHandler(BungeeCordHelper.getPayloadHandler());
        gatewaySupport.registerListener(this);
    }

    @Test
    public void testPackets() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        // simulate BungeeCord reply
        try {
            dataOutputStream.writeUTF(PacketIP.TAG);
            dataOutputStream.writeUTF(host);
            dataOutputStream.writeInt(port);
        } catch (IOException e) {
            fail("IOException whilst writing output stream.");
            e.printStackTrace();
        }


        try {
            gatewaySupport.incomingPayload(sender, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            fail("IOException whilst handling incoming payload.");
            e.printStackTrace();
        }

        assertThat("Packet wasn't received.", packetReceived, is(true));
    }

    @PacketHandler
    public void onPacketIP(String sender, PacketIP packetIP) {
        assertThat("Sender does not match.", sender, is(this.sender));
        InetSocketAddress inetSocketAddress = packetIP.getSocketAddress();
        assertThat("Hostname does not match.", inetSocketAddress.getHostString(), is(host));
        assertThat("Port does not match.", inetSocketAddress.getPort(), is(port));
        packetReceived = true;
    }

}
