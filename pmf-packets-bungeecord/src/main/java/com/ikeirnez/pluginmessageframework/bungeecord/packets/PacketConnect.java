package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Connects a player to said sub-server.
 */
public class PacketConnect extends RawPacket {

    public static final String TAG = "Connect", TAG_OTHER = "ConnectOther";

    private final String player, server;

    /**
     * Creates a new instance using the sender of the packet as the player to be connected to another server.
     *
     * @param server the server to connect the player to
     */
    public PacketConnect(String server) {
        this(null, server);
    }

    /**
     * Creates a new instance specifying a player to be connected to another server.
     *
     * @param player the player to be connected to another server
     * @param server the server to connect the player to
     */
    public PacketConnect(String player, String server) {
        super(player == null ? TAG : TAG_OTHER);

        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        this.player = player;
        this.server = server;
    }

    /**
     * Gets the player which will be connected to another server.
     *
     * @return the player, null when using the sender of the packet as the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the server the player will be connected to.
     *
     * @return the server
     */
    public String getServer() {
        return server;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);

        if (player != null) {
            dataOutputStream.writeUTF(player);
        }

        dataOutputStream.writeUTF(server);
    }
}
