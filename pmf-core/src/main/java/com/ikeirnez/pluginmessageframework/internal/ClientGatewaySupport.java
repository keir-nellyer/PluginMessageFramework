package com.ikeirnez.pluginmessageframework.internal;

import com.ikeirnez.pluginmessageframework.gateway.ClientGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;

/**
 * Support class for {@link ClientGateway} implementations.
 */
public abstract class ClientGatewaySupport<C> extends GatewaySupport<C> implements ClientGateway<C> {

    protected final C connection;

    public ClientGatewaySupport(String channel, C connection) {
        super(channel);
        this.connection = connection;
    }

    @Override
    public void sendPacket(Packet packet) {
        sendPacket(this.connection, packet);
    }

}
