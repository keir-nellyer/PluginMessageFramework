package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
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
    private final Server server;

    public DefaultSpongeGateway(String channel, final Object plugin, Server server) {
        super(channel);
        this.plugin = plugin;
        this.server = server;

        server.registerChannel(plugin, this, getChannel());
    }

    @Override
    public void sendPacket(Player player, Packet packet) throws IOException {
        sendPacket(new SpongeConnectionWrapper(player, plugin), packet);
    }

    @Override
    public ConnectionWrapper<Player> getConnection() {
        Collection<Player> players = server.getOnlinePlayers();
        return players.size() > 0 ? new SpongeConnectionWrapper(players.iterator().next(), plugin) : null;
    }

    @Override
    @NonnullByDefault
    public void handlePayload(PlayerConnection client, String channel, ChannelBuf data) {
        if (channel.equals(getChannel())) {
            receivePacket(new SpongeConnectionWrapper(client.getPlayer(), plugin), data.array());
        }
    }

}
