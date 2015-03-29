package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.Gateway;
import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.PluginMessageFramework;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelBuf;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.PlayerConnection;

import java.util.Collection;

/**
 * Created by Keir on 27/03/2015.
 */
public class SpongeGateway extends Gateway<Player> implements ChannelListener {

    private final Object plugin;
    private final Server server;

    public SpongeGateway(PluginMessageFramework pluginMessageFramework, Object plugin, Server server) {
        super(pluginMessageFramework);
        this.plugin = plugin;
        this.server = server;

        server.registerChannel(plugin, this, pluginMessageFramework.getChannel());
    }

    @Override
    public ConnectionWrapper getGateway() {
        Collection<Player> players = server.getOnlinePlayers();
        return players.size() > 0 ? new SpongeConnection(players.iterator().next(), plugin) : null;
    }

    @Override
    public void handlePayload(PlayerConnection client, String channel, ChannelBuf data) {
        if (channel.equals(pluginMessageFramework.getChannel())) {
            receive(new SpongeConnection(client.getPlayer(), plugin), data.array());
        }
    }
}
