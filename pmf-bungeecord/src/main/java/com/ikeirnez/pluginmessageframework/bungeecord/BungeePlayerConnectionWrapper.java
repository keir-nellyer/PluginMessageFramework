package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.impl.BaseConnectionWrapper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeePlayerConnectionWrapper extends BaseConnectionWrapper<ProxiedPlayer> {

    public BungeePlayerConnectionWrapper(ProxiedPlayer gateway) {
        super(gateway);
    }

    @Override
    public void sendCustomPayload(String channel, byte[] data) {
        getConnection().sendData(channel, data);
    }
}
