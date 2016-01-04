package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Provides a {@link ServerGateway} capable of running on the Sponge implementation.
 */
public class SpongeGatewayProvider {

    /**
     * Gets a new {@link ServerGateway} capable of running on the Sponge implementation.
     *
     * @param channel the channel to operate on
     * @param plugin the plugin making use of the framework (used to register plugin channels)
     * @return the gateway
     */
    public static ServerGateway<Player> getGateway(String channel, Object plugin) {
        return new ImplSpongeGateway(channel, plugin);
    }

    private SpongeGatewayProvider() {}

}
