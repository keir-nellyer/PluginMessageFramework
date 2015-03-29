package com.ikeirnez.pluginmessageframework.bukkit;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Keir on 27/03/2015.
 */
public class BukkitConnection extends ConnectionWrapper<Player> {

    private Plugin plugin;

    public BukkitConnection(Player gateway, Plugin plugin) {
        super(gateway);
        this.plugin = plugin;
    }

    @Override
    protected void send(String channel, byte[] data) {
        getGateway().sendPluginMessage(plugin, channel, data);
    }
}
