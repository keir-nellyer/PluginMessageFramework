package com.ikeirnez.pluginmessageframework.bukkit;

import com.ikeirnez.pluginmessageframework.Gateway;
import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 27/03/2015.
 */
public class BukkitGateway extends Gateway<Player> implements PluginMessageListener {

    private final Plugin plugin;

    public BukkitGateway(PluginMessageFramework pluginMessageFramework, Plugin plugin) {
        super(pluginMessageFramework);
        this.plugin = plugin;

        Messenger messenger = plugin.getServer().getMessenger();
        messenger.registerIncomingPluginChannel(plugin, pluginMessageFramework.getChannel(), this);
        messenger.registerOutgoingPluginChannel(plugin, pluginMessageFramework.getChannel());
    }

    @Override
    public Optional<ConnectionWrapper<Player>> getGateway() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        return players.size() > 0 ?
                Optional.<ConnectionWrapper<Player>>of(new BukkitConnection(players.iterator().next(), plugin)) :
                Optional.<ConnectionWrapper<Player>>empty();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(pluginMessageFramework.getChannel())) {
            receive(new BukkitConnection(player, plugin), message);
        }
    }
}
