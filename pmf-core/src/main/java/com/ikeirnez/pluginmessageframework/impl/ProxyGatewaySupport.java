package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.QueueableConnectionWrapper;
import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Created by Keir on 02/04/2015.
 */
public abstract class ProxyGatewaySupport<U, T> extends GatewaySupport<T> implements ProxyGateway<U, T> {

    private final ProxySide proxySide;

    public ProxyGatewaySupport(String channel, ProxySide proxySide) {
        super(channel);
        this.proxySide = proxySide;
        this.type = getGenericTypeClass(getClass(), 1); // update this as we've added an extra type which breaks type check in super class
    }

    @Override
    public ProxySide getProxySide() {
        return proxySide;
    }

    @Override
    public boolean sendPacketServer(QueueableConnectionWrapper<U> connectionWrapper, Packet packet) throws IOException {
        return sendPacketServer(connectionWrapper, packet, true);
    }

    @Override
    public boolean sendPacketServer(QueueableConnectionWrapper<U> connectionWrapper, Packet packet, boolean queue) throws IOException {
        return connectionWrapper.sendCustomPayload(getChannel(), getPayloadHandler().writeOutgoingPacket(packet), queue);
    }

}
