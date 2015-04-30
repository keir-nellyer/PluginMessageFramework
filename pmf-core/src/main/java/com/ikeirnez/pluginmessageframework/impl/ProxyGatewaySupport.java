package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.gateway.ProxySide;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

/**
 * Support class for {@link ProxyGateway} implementations.
 *
 * @param <C> the client connection type
 * @param <S> the server connection type
 */
public abstract class ProxyGatewaySupport<C, S> extends GatewaySupport<C> implements ProxyGateway<C, S> {

    private ProxySide proxySide;

    public ProxyGatewaySupport(ProxySide proxySide) {
        super(null);
        setProxySide(proxySide);
    }

    public ProxyGatewaySupport(String channel, ProxySide proxySide) {
        super(channel);
        setProxySide(proxySide);
    }

    private void setProxySide(ProxySide proxySide) {
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
    public boolean sendPacketServer(S serverConnection, Packet packet) {
        return sendPacketServer(serverConnection, packet, true);
    }

    @Override
    public boolean sendPacketServer(S serverConnection, Packet packet, boolean queue) {
        return sendCustomPayloadServer(serverConnection, getChannel(), writePacket(packet), queue);
    }

    // return, true if packet sent instantly, false if queued
    public abstract boolean sendCustomPayloadServer(S serverConnection, String channel, byte[] bytes, boolean queue);

}
