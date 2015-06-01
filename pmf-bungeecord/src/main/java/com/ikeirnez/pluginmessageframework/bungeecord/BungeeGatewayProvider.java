package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.gateway.ProxyGateway;
import com.ikeirnez.pluginmessageframework.gateway.ProxySide;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Provides a {@link ProxyGateway} capable of running on the BungeeCord implementation.
 */
public class BungeeGatewayProvider {

    /**
     * Gets a new {@link ProxyGateway} capable of running on the BungeeCord implementation.
     *
     * @param channel the channel to operate on
     * @param proxySide the side this gateway should operate on
     * @param plugin the plugin making use of the framework (used to register plugin channels)
     * @return the gateway
     */
    public static ProxyGateway<ProxiedPlayer, Server, ServerInfo> getGateway(String channel, ProxySide proxySide, Plugin plugin) {
        return new ImplBungeeGateway(channel, proxySide, plugin);
    }

    private BungeeGatewayProvider() {}

}
