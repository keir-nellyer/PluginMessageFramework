package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import org.spongepowered.api.entity.player.Player;

import java.io.IOException;

/**
 * Represents a {@link ServerGateway} on the Sponge implementation.
 */
public interface SpongeGateway extends ServerGateway<Player> {

    /**
     * Sends a packet using the defined player as the gateway.
     *
     * @param player the player to use as a gateway
     * @param packet the packet to be sent
     * @throws IOException thrown if there is an error sending the packet (usually in the serializing stage)
     */
    void sendPacket(Player player, Packet packet) throws IOException;

}
