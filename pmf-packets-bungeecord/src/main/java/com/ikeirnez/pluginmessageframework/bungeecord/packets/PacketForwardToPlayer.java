package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Sends a plugin message to a specific player.
 */
public class PacketForwardToPlayer extends RawPacket {

    public static final String TAG = "ForwardToPlayer";

    private String player;
    private final String channel;
    private final byte[] data;

    /**
     * Creates a new instance.
     *
     * @param player the player to send the plugin message to
     * @param channel the channel to send the message through
     * @param data the message to send
     */
    public PacketForwardToPlayer(String player, String channel, byte[] data) {
        this(channel, data);

        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        }

        this.player = player;
    }

    @IncomingHandler(TAG)
    private PacketForwardToPlayer(String channel, byte[] data) {
        super(TAG);
        this.channel = channel;
        this.data = data;
    }

    /**
     * Gets the channel this message was sent through.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public byte[] getData() {
        return data;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(player);
        dataOutputStream.writeUTF(channel);
        writeByteArray(dataOutputStream, data);
    }
}
