package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Get a list of players connected on a certain server, or on <b>ALL</b> the servers.
 */
public class PacketPlayerList extends RawPacket {

    public static final String TAG = "PlayerList";

    private final String server;
    private String[] playerList;

    /**
     * Creates a new instance getting the player list from <b>ALL</b> servers.
     */
    public PacketPlayerList() {
        this("ALL");
    }

    /**
     * Creates a new instance getting the player list from the defined server(s).
     *
     * @param server the server to get the player list of (or <b>ALL</b> for global player list).
     */
    public PacketPlayerList(String server) {
        super(TAG);

        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        this.server = server;
    }

    @IncomingHandler(TAG)
    private PacketPlayerList(String server, String playerList) {
        this(server);
        this.playerList = playerList.split(", ");
    }

    /**
     * Gets the name of the server in which the player list was retrieved from.
     *
     * @return the name of the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Gets the player list retrieved from the server(s).
     *
     * @return the player list
     */
    public String[] getPlayerList() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return playerList;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(server);
    }
}
