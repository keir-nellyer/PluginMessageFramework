package com.ikeirnez.pluginmessageframework.sponge;

import com.google.common.eventbus.Subscribe;
import com.ikeirnez.pluginmessageframework.internal.ServerGatewaySupport;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.ChannelListener;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.io.IOException;
import java.util.Collection;

/**
 * The default Sponge implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ServerGateway}.
 */
public class ImplSpongeGateway extends ServerGatewaySupport<Player> implements ChannelListener {

    private final Object plugin;
    private final Game game;

    protected ImplSpongeGateway(String channel, final Object plugin, Game game) {
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
        game.getEventManager().registerListeners(plugin, this);
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

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        if (queuedPackets()) {
            // todo does this need delayed like in DefaultBukkitGateway?
            sendQueuedPackets(event.getTargetEntity());
        }
    }

}
