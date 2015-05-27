package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.internal.GatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;

/**
 * Represents a connection on a server implement to a proxy/player.
 *
 * @param <C> the client connection type
 */
public interface ServerGateway<C> extends Gateway<C> {
    /**
     * Sends the packet on a gateway provided by the {@link GatewaySupport} specified in the constructor.
     *
     * @param packet the packet to send
     * @return true if the packet was sent immediately, false if a gateway couldn't be found and the packet has been queued for later
     */
    boolean sendPacket(Packet packet);

    /**
     * Sends the packet on a gateway provided by the {@link GatewaySupport} specified in the constructor.
     *
     * @param packet the packet to send
     * @param queue if there is no available gateway, should this packet queue until a connection becomes available
     * @return true if the packet was sent immediately, false if a gateway couldn't be found and the packet has been queued for later (if queue parameter is true)
     */
    boolean sendPacket(Packet packet, boolean queue);

    /**
     * Gets a connection which can be used to send a packet.
     *
     * @return the connection (may be null)
     */
    C getConnection();
}
