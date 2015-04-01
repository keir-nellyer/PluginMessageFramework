package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.AbstractGateway;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeeCordGateway extends AbstractGateway<ProxiedPlayer> {

    private final Plugin plugin;

    public BungeeCordGateway(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void init(PluginMessageFramework pluginMessageFramework) {
        super.init(pluginMessageFramework);

        plugin.getProxy().registerChannel(pluginMessageFramework.getChannel());
        plugin.getProxy().getPluginManager().registerListener(plugin, new Listener() {
            @EventHandler
            public void onPluginMessage(PluginMessageEvent e) {
                Connection sender = e.getSender();

                if (sender instanceof Server && e.getTag().equals(BungeeCordGateway.this.pluginMessageFramework.getChannel())) { // todo allow plugin messages from players?
                    receivePacket(new BungeeCordConnection((ProxiedPlayer) sender), e.getData());
                }
            }
        });
    }

    @Override
    public Optional<ConnectionWrapper<ProxiedPlayer>> getConnection() {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        return players.size() > 0 ?
                        Optional.<ConnectionWrapper<ProxiedPlayer>>of(new BungeeCordConnection(players.iterator().next())) :
                        Optional.<ConnectionWrapper<ProxiedPlayer>>empty();
    }
}
