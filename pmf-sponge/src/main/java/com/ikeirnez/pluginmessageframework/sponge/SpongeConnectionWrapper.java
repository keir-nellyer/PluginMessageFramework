package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.impl.BaseConnectionWrapper;
import org.spongepowered.api.entity.player.Player;

/**
 * Created by Keir on 27/03/2015.
 */
public class SpongeConnectionWrapper extends BaseConnectionWrapper<Player> {

    private final Object plugin;

    public SpongeConnectionWrapper(Player gateway, Object plugin) {
        super(gateway);
        this.plugin = plugin;
    }

    @Override
    public void sendCustomPayload(String channel, byte[] data) {
        getConnection().getConnection().sendCustomPayload(plugin, channel, data);
    }
}
