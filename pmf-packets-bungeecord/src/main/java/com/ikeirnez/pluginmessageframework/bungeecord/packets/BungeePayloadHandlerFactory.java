package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.basic.BasicPayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory class for getting a {@link PayloadHandler} capable of handling BungeeCord packets.
 */
public class BungeePayloadHandlerFactory {

    @SuppressWarnings("serial")
    private static final List<Class<? extends RawPacket>> packets = new ArrayList<Class<? extends RawPacket>>() {
        {
            add(PacketForwardToPlayer.class);
            add(PacketForwardToServer.class);
            add(PacketGetServer.class);
            add(PacketGetServers.class);
            add(PacketIP.class);
            add(PacketPlayerCount.class);
            add(PacketPlayerList.class);
            add(PacketUUID.class);
            add(PacketServerIP.class);
        }
    };

    /**
     * Gets a {@link PayloadHandler} capable of handling BungeeCord packets.
     *
     * @return the payload handler
     */
    public static PayloadHandler getPayloadHandler() {
        BasicPayloadHandler basicPayloadHandler = new BasicPayloadHandler();
        basicPayloadHandler.registerAllIncomingPackets(packets);
        return basicPayloadHandler;
    }

    private BungeePayloadHandlerFactory() {}

}
