package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.AbstractGateway;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeeCordGateway extends AbstractGateway<ProxiedPlayer> {

    private final Plugin plugin;

    public BungeeCordGateway(PluginMessageFramework pluginMessageFramework, Plugin plugin) {
        super(pluginMessageFramework);
        this.plugin = plugin;

        plugin.getProxy().registerChannel(pluginMessageFramework.getChannel());
    }

    @Override
    public Optional<ConnectionWrapper<ProxiedPlayer>> getConnection() {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        return players.size() > 0 ?
                        Optional.<ConnectionWrapper<ProxiedPlayer>>of(new BungeeCordConnection(players.iterator().next())) :
                        Optional.<ConnectionWrapper<ProxiedPlayer>>empty();
    }
}
