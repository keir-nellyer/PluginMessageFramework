package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Sends a plugin message to said server.
 * This is one of the most useful packets ever.
 * <b>Remember, the sending and receiving server(s) need to have a player online.</b>
 */
public class PacketForwardToServer extends RawPacket {

    public static final String TAG = "Forward";

    @IncomingHandler(TAG)
    private static PacketForwardToServer createInstance(String channel, byte[] bytes) { // work-around multiple constructors with same args
        PacketForwardToServer packetForwardToServer = new PacketForwardToServer();
        packetForwardToServer.channel = channel;
        packetForwardToServer.bytes = bytes;
        return packetForwardToServer;
    }

    private String server;
    private String channel;
    private byte[] bytes;

    /**
     * Creates a new instance sending the plugin message to <b>ALL</b> servers.
     *
     * @param channel the channel to send the message through
     * @param data the message to send
     */
    public PacketForwardToServer(String channel, byte[] data) {
        this("ALL", channel, data);
    }

    /**
     * Creates a new instance sending the plugin message to the specified server(s).
     *
     * @param server the server(s) to send the plugin message to
     * @param channel the channel to send the message through
     * @param data the message to send
     */
    public PacketForwardToServer(String server, String channel, byte[] data) {
        this();

        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        }

        this.server = server;
        this.channel = channel;
        this.bytes = data;
    }

    private PacketForwardToServer() {
        super(TAG);
    }

    /**
     * Gets the channel this message will/has been sent through.
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
        dataOutputStream.writeUTF(server);
        dataOutputStream.writeUTF(channel);
        writeByteArray(dataOutputStream, bytes);
    }
}
