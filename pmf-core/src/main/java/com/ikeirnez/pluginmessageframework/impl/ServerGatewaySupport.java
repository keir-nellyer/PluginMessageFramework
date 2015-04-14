package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Keir on 03/04/2015.
 */
public abstract class ServerGatewaySupport<T> extends GatewaySupport<T> implements ServerGateway<T> {

    private final List<Packet> standardPacketQueue = new ArrayList<>();

    public ServerGatewaySupport(String channel) {
        super(channel);
    }

    protected boolean queuedPackets() {
        return standardPacketQueue.size() > 0;
    }

    protected void connectionAvailable(ConnectionWrapper<T> connection) throws IOException {
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
        ConnectionWrapper<T> connection = getConnection();
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
