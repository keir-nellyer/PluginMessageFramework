package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelBuf;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.PlayerConnection;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 27/03/2015.
 */
public class DefaultSpongeGateway extends ServerGatewaySupport<Player> {

    private final Object plugin;
    private final Server server;

    public DefaultSpongeGateway(String channel, final Object plugin, Server server) {
        super(channel);
        this.plugin = plugin;
        this.server = server;

        server.registerChannel(plugin, new ChannelListener() {
            @Override
            public void handlePayload(@NonnullByDefault PlayerConnection client, @NonnullByDefault String channel, @NonnullByDefault ChannelBuf data) {
                if (channel.equals(getChannel())) {
                    receivePacket(new SpongeConnectionWrapper(client.getPlayer(), plugin), data.array());
                }
            }
        }, getChannel());
    }

    @Override
    public Optional<ConnectionWrapper<Player>> getConnection() {
        Collection<Player> players = server.getOnlinePlayers();
        return players.size() > 0 ?
                Optional.<ConnectionWrapper<Player>>of(new SpongeConnectionWrapper(players.iterator().next(), plugin)) :
                Optional.<ConnectionWrapper<Player>>empty();
    }
}
