package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeeCordConnection extends ConnectionWrapper<ProxiedPlayer> {

    public BungeeCordConnection(ProxiedPlayer gateway) {
        super(gateway);
    }

    @Override
    protected void send(String channel, byte[] data) {
        getGateway().sendData(channel, data);
    }
}
