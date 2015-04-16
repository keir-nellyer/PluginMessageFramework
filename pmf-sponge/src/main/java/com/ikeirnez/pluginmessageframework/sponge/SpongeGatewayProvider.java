package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import com.ikeirnez.pluginmessageframework.sponge.impl.SpongeGateway;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;

/**
 * Provides a {@link ServerGateway} capable of running on the Sponge implementation.
 */
public class SpongeGatewayProvider {

    /**
     * Gets a new {@link ServerGateway} capable of running on the Sponge implementation.
     *
     * @param channel the channel to operate on
     * @param plugin the plugin making use of the framework (used to register plugin channels)
     * @param game the game instance
     * @return the gateway
     */
    public static ServerGateway<Player> getGateway(String channel, Object plugin, Game game) {
        return new SpongeGateway(channel, plugin, game);
    }

    private SpongeGatewayProvider() {}

}
