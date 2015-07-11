package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.packet.BasePacket;

/**
 * Represents an instance which has a single connection.
 */
public interface ClientGateway<C> extends Gateway<C> {

    /**
     * Sends a packet to the other side of the connection.
     *
     * @param packet the packet
     */
    void sendPacket(BasePacket packet);

}
