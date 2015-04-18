package com.ikeirnez.pluginmessageframework.bukkit;

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

/**
 * The default Bukkit implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ServerGateway}.
 */
public class BukkitGateway extends ServerGatewaySupport<Player> implements Listener, PluginMessageListener {

    protected final Plugin plugin;

    public BukkitGateway(String channel, final Plugin plugin) {
        super(channel);

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }

        this.plugin = plugin;

        Messenger messenger = plugin.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, getChannel());
        messenger.registerIncomingPluginChannel(plugin, getChannel(), this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void sendCustomPayload(Player connection, String channel, byte[] bytes) {
        connection.sendPluginMessage(plugin, channel, bytes);
    }

    @Override
    public Player getConnection() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        return players.size() > 0 ? players.iterator().next() : null;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(getChannel())) {
            try {
                incomingPayload(player, message);
            } catch (IOException e) {
                logger.error("Error handling incoming payload.", e);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() { // delay to work around plugin messages not sending when players first join
            @Override
            public void run() {
                if (queuedPackets()) {
                    try {
                        sendQueuedPackets(event.getPlayer());
                    } catch (IOException e1) {
                        logger.error("Error whilst sending queued packets.", e1);
                    }
                }
            }
        }, 10L);
    }

}
