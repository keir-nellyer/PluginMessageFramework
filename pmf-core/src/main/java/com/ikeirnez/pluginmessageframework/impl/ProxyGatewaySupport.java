package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Support class for {@link ProxyGateway} implementations.
 *
 * @param <C> the client connection type
 * @param <S> the server connection type
 */
public abstract class ProxyGatewaySupport<C, S> extends GatewaySupport<C> implements ProxyGateway<C, S> {

    private final ProxySide proxySide;

    public ProxyGatewaySupport(String channel, ProxySide proxySide) {
        super(channel);

        if (proxySide == null) {
            throw new IllegalArgumentException("ProxySide cannot be null.");
        }

        this.proxySide = proxySide;
    }

    @Override
    public ProxySide getProxySide() {
        return proxySide;
    }

    @Override
    public boolean sendPacketServer(S serverConnection, Packet packet) throws IOException {
        return sendPacketServer(serverConnection, packet, true);
    }

    @Override
    public boolean sendPacketServer(S serverConnection, Packet packet, boolean queue) throws IOException {
        return sendCustomPayloadServer(serverConnection, getChannel(), writePacket(packet), queue);
    }

    // return, true if packet sent instantly, false if queued
    public abstract boolean sendCustomPayloadServer(S serverConnection, String channel, byte[] bytes, boolean queue);

}
