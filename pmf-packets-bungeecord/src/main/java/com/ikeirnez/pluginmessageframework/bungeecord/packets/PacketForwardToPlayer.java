package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.BasicPayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A packet containing a byte array which will be forwarded to another player.
 */
public class PacketForwardToPlayer extends RawPacket {

    private static final long serialVersionUID = -225097732629157786L;
    public static final String TAG = "ForwardToPlayer";

    private String player, channel;
    private byte[] bytes;

    public PacketForwardToPlayer(String player, String channel, Packet packet) throws IOException {
        this(player, channel, BasicPayloadHandler.writeBytes(packet));
    }

    public PacketForwardToPlayer(String player, String channel, byte[] bytes) {
        this(channel, bytes);
        this.player = player;
    }

    @IncomingHandler
    private PacketForwardToPlayer(String channel, byte[] bytes) {
        super(TAG);
        this.channel = channel;
        this.bytes = bytes;
    }

    public String getChannel() {
        return channel;
    }

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
