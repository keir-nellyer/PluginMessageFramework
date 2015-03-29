package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import org.spongepowered.api.entity.player.Player;

/**
 * Created by Keir on 27/03/2015.
 */
public class SpongeConnection extends ConnectionWrapper<Player> {

    private Object plugin;

    public SpongeConnection(Player gateway, Object plugin) {
        super(gateway);
        this.plugin = plugin;
    }

    @Override
    protected void send(String channel, byte[] data) {
        getGateway().getConnection().sendCustomPayload(plugin, channel, data);
    }
}
