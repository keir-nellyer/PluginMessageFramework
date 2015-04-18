package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Get the amount of players on a certain server, or on <b>all</b> servers.
 */
public class PacketPlayerCount extends RawPacket {

    public static final String TAG = "PlayerCount";

    private String server;
    private int playerCount;

    /**
     * Creates a new instance requesting the player count of <b>all</b> the servers.
     */
    public PacketPlayerCount() {
        this("ALL");
    }

    /**
     * Creates a new instance requesting the player count of the defined server(s).
     *
     * @param server the server to get the player count of (or <b>ALL</b> for global count)
     */
    public PacketPlayerCount(String server) {
        super(TAG);

        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        this.server = server;
    }

    @IncomingHandler(TAG)
    private PacketPlayerCount(String server, int playerCount) {
        this(server);
        this.playerCount = playerCount;
    }

    /**
     * Gets the name of the server to get the player count of (or <b>ALL</b> for global count).
     *
     * @return the name of the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Gets the player count of the server(s).
     *
     * @return the player count
     */
    public int getPlayerCount() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return playerCount;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(server);
    }
}
