package com.ikeirnez.pluginmessageframework.sponge.impl;

import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.net.ChannelBuf;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.PlayerConnection;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.io.IOException;
import java.util.Collection;

/**
 * The default Sponge implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ServerGateway}.
 */
public class SpongeGateway extends ServerGatewaySupport<Player> implements ChannelListener {

    private final Object plugin;
    private final Game game;

    protected SpongeGateway(String channel, final Object plugin, Game game) {
        super(channel);

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }

        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        this.plugin = plugin;
        this.game = game;

        game.getServer().registerChannel(plugin, this, getChannel());
        game.getEventManager().register(plugin, this);
    }

    @Override
    public void sendPayload(Player connection, String channel, byte[] bytes) {
        connection.getConnection().sendCustomPayload(plugin, channel, bytes);
    }

    @Override
    public Player getConnection() {
        Collection<Player> players = game.getServer().getOnlinePlayers();
        return players.size() > 0 ? players.iterator().next() : null;
    }

    @Override
    @NonnullByDefault
    public void handlePayload(PlayerConnection client, String channel, ChannelBuf data) {
        if (channel.equals(getChannel())) {
            try {
                incomingPayload(client.getPlayer(), data.array());
            } catch (IOException e) {
                logger.error("Error handling incoming payload.", e);
            }
        }
    }

    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (queuedPackets()) {
            // todo does this need delayed like in DefaultBukkitGateway?
            sendQueuedPackets(event.getPlayer());
        }
    }

}
