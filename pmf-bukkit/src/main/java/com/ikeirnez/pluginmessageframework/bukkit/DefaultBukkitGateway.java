package com.ikeirnez.pluginmessageframework.bukkit;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 27/03/2015.
 */
public class DefaultBukkitGateway extends ServerGatewaySupport<Player> {

    private final Plugin plugin;

    public DefaultBukkitGateway(String channel, final Plugin plugin) {
        super(channel);
        this.plugin = plugin;

        Messenger messenger = plugin.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, getChannel());
        messenger.registerIncomingPluginChannel(plugin, getChannel(), new PluginMessageListener() {
            @Override
            public void onPluginMessageReceived(String channel, Player player, byte[] message) {
                if (channel.equals(getChannel())) {
                    receivePacket(new BukkitConnectionWrapper(player, plugin), message);
                }
            }
        });

        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent e) {
                if (queuedPackets()) {
                    try {
                        connectionAvailable(new BukkitConnectionWrapper(e.getPlayer(), plugin));
                    } catch (IOException e1) {
                        logger.error("Error whilst sending queued packets.", e1);
                    }
                }
            }
        }, plugin);
    }

    @Override
    public Optional<ConnectionWrapper<Player>> getConnection() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        return players.size() > 0 ?
                Optional.<ConnectionWrapper<Player>>of(new BukkitConnectionWrapper(players.iterator().next(), plugin)) :
                Optional.<ConnectionWrapper<Player>>empty();
    }
}
