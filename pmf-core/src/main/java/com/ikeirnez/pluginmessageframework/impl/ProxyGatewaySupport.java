package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.connection.ProxyConnectionWrapper;
import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Keir on 02/04/2015.
 */
public abstract class ProxyGatewaySupport<T> extends GatewaySupport<T> implements ProxyGateway<T> {

    private final ProxySide proxySide;

    private final Map<ConnectionWrapper<T>, List<Packet>> packetQueue = new HashMap<>();

    public ProxyGatewaySupport(String channel, ProxySide proxySide) {
        super(channel);
        this.proxySide = proxySide;
    }

    @Override
    public ProxySide getProxySide() {
        return proxySide;
    }

    @Override
    public boolean sendPacketServer(String server, Packet packet) throws IllegalArgumentException, IOException {
        return sendPacketServer(server, packet, true);
    }

    @Override
    public boolean sendPacketServer(String server, Packet packet, boolean queue) throws IllegalArgumentException, IOException {
        Optional<ProxyConnectionWrapper<T>> optional = getServerConnection(server);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Server '" + server + "' doesn't exist.");
        }

        return optional.get().sendCustomPayload(getChannel(), packet.writeBytes(), queue);
    }

    public abstract Optional<ProxyConnectionWrapper<T>> getServerConnection(String server);

}
