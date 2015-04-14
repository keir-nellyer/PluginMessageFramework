package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

/**
 * Get this server's name, as defined in BungeeCord's config.yml.
 */
public class PacketGetServer extends RawPacket {

    public static final String TAG = "GetServer";

    private String serverName;

    /**
     * Creates a new instance.
     */
    public PacketGetServer() {
        super(TAG);
    }

    @IncomingHandler(TAG)
    private PacketGetServer(String serverName) {
        this();
        this.serverName = serverName;
    }

    /**
     * Gets the servers name as defined in BungeeCord's config.yml.
     *
     * @return the server name
     */
    public String getServerName() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return serverName;
    }

}
