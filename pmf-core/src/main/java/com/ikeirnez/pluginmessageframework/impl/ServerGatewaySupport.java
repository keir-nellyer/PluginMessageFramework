package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Support class for {@link ServerGateway} implementations.
 *
 * @param <C> the client connection type
 */
public abstract class ServerGatewaySupport<C> extends GatewaySupport<C> implements ServerGateway<C> {

    private final List<Packet> standardPacketQueue = new ArrayList<>();

    public ServerGatewaySupport(String channel) {
        super(channel);
    }

    protected boolean queuedPackets() {
        return standardPacketQueue.size() > 0;
    }

    protected void sendQueuedPackets(C connection) throws IOException {
        Iterator<Packet> iterator = standardPacketQueue.iterator();
        while (iterator.hasNext()) {
            Packet standardPacket = iterator.next();
            sendPacket(connection, standardPacket);
            iterator.remove();
        }
    }

    @Override
    public boolean sendPacket(Packet packet) throws IOException {
        return sendPacket(packet, true);
    }

    @Override
    public boolean sendPacket(Packet packet, boolean queue) throws IOException {
        C connection = getConnection();
        if (connection == null) {
            if (queue) {
                standardPacketQueue.add(packet);
            }

            return false;
        }

        sendPacket(connection, packet);
        return true;
    }

}
