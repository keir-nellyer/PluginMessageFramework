package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.AbstractGateway;
import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelBuf;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.PlayerConnection;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Keir on 27/03/2015.
 */
public class SpongeGateway extends AbstractGateway<Player> {

    private final Object plugin;
    private final Server server;

    public SpongeGateway(Object plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    protected void init(PluginMessageFramework pluginMessageFramework) {
        super.init(pluginMessageFramework);

        server.registerChannel(plugin, new ChannelListener() {
            @Override
            public void handlePayload(PlayerConnection client, String channel, ChannelBuf data) {
                if (channel.equals(SpongeGateway.this.pluginMessageFramework.getChannel())) {
                    receivePacket(new SpongeConnection(client.getPlayer(), plugin), data.array());
                }
            }
        }, pluginMessageFramework.getChannel());
    }

    @Override
    public Optional<ConnectionWrapper<Player>> getConnection() {
        Collection<Player> players = server.getOnlinePlayers();
        return players.size() > 0 ?
                Optional.<ConnectionWrapper<Player>>of(new SpongeConnection(players.iterator().next(), plugin)) :
                Optional.<ConnectionWrapper<Player>>empty();
    }
}
