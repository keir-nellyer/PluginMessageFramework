package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Request the IP of any server on this proxy.
 */
public class PacketServerIP extends RawPacket {

    public static final String TAG = "ServerIP";

    private String server;
    private InetSocketAddress serverAddress;

    /**
     * Creates a new instance requesting the server IP of the defined server.
     *
     * @param server the server to get the address of
     */
    public PacketServerIP(String server) {
        super(TAG);

        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        this.server = server;
    }

    @IncomingHandler(TAG)
    private PacketServerIP(String server, String ip, short port) {
        this(server);
        serverAddress = InetSocketAddress.createUnresolved(ip, port);
    }

    /**
     * Gets the server in which the address belongs to.
     *
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Gets the unresolved address of the server.
     *
     * @return the unresolved address of the server
     */
    public InetSocketAddress getServerAddress() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return serverAddress;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(server);
    }
}
