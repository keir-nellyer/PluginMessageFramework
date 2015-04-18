package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ProxySide;
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
    public boolean sendPacketServer(U serverConnection, Packet packet) throws IOException {
        return sendPacketServer(serverConnection, packet, true);
    }

    @Override
    public boolean sendPacketServer(U serverConnection, Packet packet, boolean queue) throws IOException {
        return sendCustomPayloadServer(serverConnection, getChannel(), writePacket(packet), queue);
    }

    // return, true if packet sent instantly, false if queued
    public abstract boolean sendCustomPayloadServer(U serverConnection, String channel, byte[] bytes, boolean queue);

}
