package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.BasicPayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A packet containing a byte array which will be forwarded to another server through any means possible.
 */
public class PacketForwardToServer extends RawPacket {

    private static final long serialVersionUID = -225097732629157786L;
    public static final String TAG = "Forward";

    private String server, channel;
    private byte[] bytes;

    public PacketForwardToServer(String server, String channel, Packet packet) throws IOException {
        this(server, channel, BasicPayloadHandler.writeBytes(packet));
    }

    public PacketForwardToServer(String server, String channel, byte[] bytes) {
        this(channel, bytes);
        this.server = server;
    }

    @IncomingHandler
    private PacketForwardToServer(String channel, byte[] bytes) {
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
        dataOutputStream.writeUTF(server);
        dataOutputStream.writeUTF(channel);
        writeByteArray(dataOutputStream, bytes);
    }
}
