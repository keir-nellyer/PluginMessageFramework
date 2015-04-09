package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;
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
 * Created by Keir on 27/03/2015.
 */
public class DefaultSpongeGateway extends ServerGatewaySupport<Player> implements SpongeGateway, ChannelListener {

    private final Object plugin;
    private final Game game;

    public DefaultSpongeGateway(String channel, final Object plugin, Game game) {
        super(channel);
        this.plugin = plugin;
        this.game = game;

        game.getServer().registerChannel(plugin, this, getChannel());
        game.getEventManager().register(plugin, this);
    }

    @Override
    public void sendPacket(Player player, Packet packet) throws IOException {
        sendPacket(new SpongeConnectionWrapper(player, plugin), packet);
    }

    @Override
    public ConnectionWrapper<Player> getConnection() {
        Collection<Player> players = game.getServer().getOnlinePlayers();
        return players.size() > 0 ? new SpongeConnectionWrapper(players.iterator().next(), plugin) : null;
    }

    @Override
    @NonnullByDefault
    public void handlePayload(PlayerConnection client, String channel, ChannelBuf data) {
        if (channel.equals(getChannel())) {
            receiveData(new SpongeConnectionWrapper(client.getPlayer(), plugin), data.array());
        }
    }

    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (queuedPackets()) {
            try {
                // todo does this need delayed like in DefaultBukkitGateway?
                connectionAvailable(new SpongeConnectionWrapper(e.getPlayer(), plugin));
            } catch (IOException e1) {
                logger.error("Error whilst sending queued packets.", e1);
            }
        }
    }

}
