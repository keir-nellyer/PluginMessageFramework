package com.ikeirnez.pluginmessageframework;

import java.io.IOException;

/**
 * Represents a 2-way connection on a Proxy-like server (e.g. BungeeCord).
 */
public interface ProxyGateway<T> extends Gateway<T> {

    /**
     * Gets the side this proxy gateway is running on.
     *
     * @return the side this proxy gateway is running on
     */
    ProxySide getProxySide();

    /**
     * Sends a {@link Packet} to a server with queueing enabled.
     * @see #sendPacketServer(String, Packet, boolean)
     */
    boolean sendPacketServer(String server, Packet packet) throws IllegalArgumentException, IOException;

    /**
     * Sends a {@link Packet} to a specified server.
     *
     * @param server the name of the server to send the packet to
     * @param packet the packet to send to the server
     * @param queue if enabled, if there is no connection to the server, the packet will be queued until a connection is available
     * @return true if the packet was sent immediately, false if the packet was unable to be sent immediately (and was queued if queueing is enabled)
     * @throws IllegalArgumentException thrown if the server specified doesn't exist
     * @throws IOException thrown if there was an issue sending the packet (will likely be in the serialization stage)
     */
    boolean sendPacketServer(String server, Packet packet, boolean queue) throws IllegalArgumentException, IOException;
}
