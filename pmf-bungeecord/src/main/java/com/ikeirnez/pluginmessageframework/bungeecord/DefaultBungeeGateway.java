package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.ProxyConnectionWrapper;
import com.ikeirnez.pluginmessageframework.ProxySide;
import com.ikeirnez.pluginmessageframework.impl.ProxyGatewaySupport;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

/**
 * Created by Keir on 29/03/2015.
 */
public class DefaultBungeeGateway extends ProxyGatewaySupport<ServerInfo> {

    private final Plugin plugin;

    public DefaultBungeeGateway(String channel, final ProxySide proxySide, Plugin plugin) {
        super(channel, proxySide);
        this.plugin = plugin;

        ProxyServer proxy = plugin.getProxy();

        proxy.registerChannel(getChannel());
        proxy.getPluginManager().registerListener(plugin, new Listener() {
            @EventHandler
            public void onPluginMessage(PluginMessageEvent e) {
                Connection sender = e.getSender();

                if (e.getTag().equals(getChannel())) {
                    e.setCancelled(true);

                    // check proxy side matches sender connection
                    if (proxySide == ProxySide.CLIENT ? sender instanceof ProxiedPlayer : proxySide == ProxySide.SERVER && sender instanceof Server){
                        receivePacket(new BungeePlayerConnectionWrapper((ProxiedPlayer) sender), e.getData());
                    }
                }
            }
        });
    }

    @Override
    public Optional<ProxyConnectionWrapper<ServerInfo>> getServerConnection(String server) {
        ServerInfo serverInfo = plugin.getProxy().getServerInfo(server);
        return serverInfo != null ?
                Optional.<ProxyConnectionWrapper<ServerInfo>>of(new BungeeServerConnectionWrapper(serverInfo)) :
                Optional.<ProxyConnectionWrapper<ServerInfo>>empty();
    }
}
