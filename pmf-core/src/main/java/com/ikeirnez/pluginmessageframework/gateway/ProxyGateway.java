package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.packet.Packet;

/**
 * Represents a 2-way connection on a Proxy-like server (e.g. BungeeCord).
 *
 * @param <C> the client connection type (e.g. BungeeCord ProxiedPlayer)
 * @param <S> the server connection type (e.g. BungeeCord Server)
 * @param <Q> the queueable server connection type (e.g. BungeeCord ServerInfo)
 */
public interface ProxyGateway<C, S, Q> extends Gateway<C> {

    /**
     * Gets the side this proxy gateway is running on.
     *
     * @return the side this proxy gateway is running on
     */
    ProxySide getProxySide();

    /**
     * Sends a {@link Packet} to a server.
     *
     * @param serverConnection the server connection
     * @param packet the packet to send to the server
     */
    void sendPacketServer(S serverConnection, Packet packet);

    /**
     * Sends a {@link Packet} to a specified server.
     *
     * @param queueableConnection the queueable server connection
     * @param packet the packet to send to the server
     * @param queue if enabled, if there is no connection to the server, the packet will be queued until a connection is available
     * @return true if the packet was sent immediately, false if the packet was unable to be sent immediately (and was queued if queueing is enabled)
     */
    boolean sendPacketServer(Q queueableConnection, Packet packet, boolean queue);

}
