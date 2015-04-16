package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.connection.QueueableConnectionWrapper;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Created by Keir on 02/04/2015.
 */
public abstract class ProxyGatewaySupport<T, U> extends GatewaySupport<T> implements ProxyGateway<T, U> {

    private final ProxySide proxySide;

    public ProxyGatewaySupport(String channel, ProxySide proxySide) {
        super(channel);
        this.proxySide = proxySide;
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
        return connectionWrapper.sendCustomPayload(getChannel(), writePacket(packet), queue);
    }

}
