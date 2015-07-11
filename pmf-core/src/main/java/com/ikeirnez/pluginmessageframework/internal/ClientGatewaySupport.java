package com.ikeirnez.pluginmessageframework.internal;

import com.ikeirnez.pluginmessageframework.gateway.ClientGateway;
import com.ikeirnez.pluginmessageframework.packet.BasePacket;

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
    public void sendPacket(BasePacket packet) {
        sendPacket(this.connection, packet);
    }

}
