package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.Gateway;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeeCordGateway extends Gateway<ProxiedPlayer> {

    private final Plugin plugin;

    public BungeeCordGateway(PluginMessageFramework pluginMessageFramework, Plugin plugin) {
        super(pluginMessageFramework);
        this.plugin = plugin;

        plugin.getProxy().registerChannel(pluginMessageFramework.getChannel());
    }

    @Override
    public ConnectionWrapper<ProxiedPlayer> getGateway() {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        return players.size() > 0 ? new BungeeCordConnection(players.iterator().next()) : null;
    }
}
