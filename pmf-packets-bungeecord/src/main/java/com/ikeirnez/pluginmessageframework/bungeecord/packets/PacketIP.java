package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.net.InetSocketAddress;

/**
 * Gets the players real IP address and port.
 * The receiver of this packet is the user of the IP address and port contained in the packet.
 */
public class PacketIP extends RawPacket {

    public static final String TAG = "IP";

    private InetSocketAddress inetSocketAddress;

    /**
     * Creates a new instance.
     */
    public PacketIP() {
        super(TAG);
    }

    @IncomingHandler(TAG)
    private PacketIP(String ip, int port) {
        this();
        inetSocketAddress = InetSocketAddress.createUnresolved(ip, port);
    }

    /**
     * Gets the {@link InetSocketAddress} of the receiver of this packet.
     *
     * @return the address
     */
    public InetSocketAddress getInetSocketAddress() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return inetSocketAddress;
    }
}
