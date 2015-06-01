package com.ikeirnez.pluginmessageframework.internal;

import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.gateway.ProxySide;
import com.ikeirnez.pluginmessageframework.packet.Packet;

/**
 * Support class for {@link ProxyGateway} implementations.
 *
 * @param <C> the client connection type
 * @param <S> the server connection type
 */
public abstract class ProxyGatewaySupport<C, S, Q> extends GatewaySupport<C> implements ProxyGateway<C, S, Q> {

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
    public void sendPacketServer(S serverConnection, Packet packet) {
        sendCustomPayloadServer(serverConnection, getChannel(), writePacket(packet));
    }

    @Override
    public boolean sendPacketServer(Q queueableConnection, Packet packet, boolean queue) {
        return sendCustomPayloadServer(queueableConnection, getChannel(), writePacket(packet), queue);
    }

    public abstract void sendCustomPayloadServer(S serverConnection, String channel, byte[] bytes);

    public abstract boolean sendCustomPayloadServer(Q queueableConnection, String channel, byte[] bytes, boolean queue);

}
