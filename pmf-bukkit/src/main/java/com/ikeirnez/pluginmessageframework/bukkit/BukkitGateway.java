package com.ikeirnez.pluginmessageframework.bukkit;

import com.ikeirnez.pluginmessageframework.AbstractGateway;
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
public class BukkitGateway extends AbstractGateway<Player> {

    private final Plugin plugin;

    public BukkitGateway(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void init(PluginMessageFramework pluginMessageFramework) {
        super.init(pluginMessageFramework);

        Messenger messenger = plugin.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, pluginMessageFramework.getChannel());
        messenger.registerIncomingPluginChannel(plugin, pluginMessageFramework.getChannel(), new PluginMessageListener() {
            @Override
            public void onPluginMessageReceived(String channel, Player player, byte[] message) {
                if (channel.equals(BukkitGateway.this.pluginMessageFramework.getChannel())) {
                    receivePacket(new BukkitConnection(player, plugin), message);
                }
            }
        });
    }

    @Override
    public Optional<ConnectionWrapper<Player>> getConnection() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        return players.size() > 0 ?
                Optional.<ConnectionWrapper<Player>>of(new BukkitConnection(players.iterator().next(), plugin)) :
                Optional.<ConnectionWrapper<Player>>empty();
    }
}
