package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.BasicPayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple factory class for getting a {@link PayloadHandler} capable of handling BungeeCord packets.
 */
public class BungeePayloadHandlerFactory {

    @SuppressWarnings("serial")
    private static final Map<String, Class<? extends Packet>> mapping = new HashMap<String, Class<? extends Packet>>(){{
        put(PacketIp.TAG, PacketIp.class);
        put(PacketForwardToServer.TAG, PacketForwardToServer.class);
        put(PacketForwardToPlayer.TAG, PacketForwardToPlayer.class);
    }};

    /**
     * Gets a {@link PayloadHandler} capable of handling BungeeCord packets.
     *
     * @return the payload handler
     */
    public static PayloadHandler getPayloadHandler() {
        BasicPayloadHandler basicPayloadHandler = new BasicPayloadHandler();
        basicPayloadHandler.registerAllSubChannels(mapping);
        return basicPayloadHandler;
    }

    private BungeePayloadHandlerFactory() {}

}
