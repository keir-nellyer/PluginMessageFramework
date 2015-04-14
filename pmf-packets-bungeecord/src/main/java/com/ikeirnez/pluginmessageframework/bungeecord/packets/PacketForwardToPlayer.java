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

    private String player, channel;
    private byte[] bytes;

    /**
     * Creates a new instance.
     *
     * @param player the player to send the plugin message to
     * @param channel the channel to send the message through
     * @param bytes the message to send
     */
    public PacketForwardToPlayer(String player, String channel, byte[] bytes) {
        this(channel, bytes);
        this.player = player;
    }

    @IncomingHandler(TAG)
    private PacketForwardToPlayer(String channel, byte[] bytes) {
        super(TAG);
        this.channel = channel;
        this.bytes = bytes;
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
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(player);
        dataOutputStream.writeUTF(channel);
        writeByteArray(dataOutputStream, bytes);
    }
}
