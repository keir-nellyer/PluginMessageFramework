package com.ikeirnez.pluginmessageframework.bukkit.impl;

import com.ikeirnez.pluginmessageframework.impl.BaseConnectionWrapper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Keir on 27/03/2015.
 */
public class BukkitConnectionWrapper extends BaseConnectionWrapper<Player> {

    private final Plugin plugin;

    public BukkitConnectionWrapper(Player gateway, Plugin plugin) {
        super(gateway);
        this.plugin = plugin;
    }

    @Override
    public void sendCustomPayload(String channel, byte[] data) {
        getConnection().sendPluginMessage(plugin, channel, data);
    }
}
