package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketForwardToPlayer;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketForwardToServer;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketGetServer;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketGetServers;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketIP;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketPlayerCount;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketPlayerList;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketServerIP;
import com.ikeirnez.pluginmessageframework.bungeecord.packets.PacketUUID;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.basic.BasicPayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory class for getting a {@link PayloadHandler} capable of handling BungeeCord packets.
 */
public class BungeeCordHelper {

    public static final String BUNGEECORD_CHANNEL = "BungeeCord";

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

    private BungeeCordHelper() {}

}
