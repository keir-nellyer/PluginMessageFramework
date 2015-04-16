package com.ikeirnez.pluginmessageframework.bukkit;

import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Provides a {@link ServerGateway} capable of running on the Bukkit implementation.
 */
public class BukkitGatewayProvider {

    /**
     * Gets a new {@link ServerGateway} capable of running on the Bukkit implementation.
     *
     * @param channel the channel to operate on
     * @param plugin the plugin making use of the framework (used to register plugin channels)
     * @return the gateway
     */
    public static ServerGateway<Player> getGateway(String channel, Plugin plugin) {
        return new BukkitGateway(channel, plugin);
    }

    private BukkitGatewayProvider() {}

}
