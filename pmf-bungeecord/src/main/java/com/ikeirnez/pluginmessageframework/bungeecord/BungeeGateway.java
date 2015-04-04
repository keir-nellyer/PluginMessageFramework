package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.connection.QueueableConnectionWrapper;
import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.io.IOException;

/**
 * Represents a {@link ProxyGateway} on the BungeeCord implementation.
 */
public interface BungeeGateway extends ProxyGateway<ServerInfo, ProxiedPlayer> {

    /**
     * Sends a packet using the defined player as the gateway.
     *
     * @param proxiedPlayer the player to use as a gateway
     * @param proxySide the proxy side this packet should be sent through
     * @param packet the packet to be sent
     * @throws IOException thrown if there is an error sending the packet (usually in the serializing stage)
     */
    void sendPacket(ProxiedPlayer proxiedPlayer, ProxySide proxySide, Packet packet) throws IOException;

    /**
     * Sends a {@link Packet} to a server with queueing enabled.
     * @see #sendPacketServer(ServerInfo, Packet, boolean)
     */
    boolean sendPacketServer(ServerInfo server, Packet packet) throws IOException;

    /**
     * Sends a {@link Packet} to a specified server.
     *
     * @param server the server to send the packet to
     * @param packet the packet to send to the server
     * @param queue if enabled, if there is no connection to the server, the packet will be queued until a connection is available
     * @return true if the packet was sent immediately, false if the packet was unable to be sent immediately (and was queued if queueing is enabled)
     * @throws IOException thrown if there was an issue sending the packet (will likely be in the serialization stage)
     */
    boolean sendPacketServer(ServerInfo server, Packet packet, boolean queue) throws IOException;

}
