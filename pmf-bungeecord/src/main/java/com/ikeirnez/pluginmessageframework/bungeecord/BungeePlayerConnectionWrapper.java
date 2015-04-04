package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.connection.DoubleSidedConnectionWrapper;
import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.impl.BaseConnectionWrapper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Keir on 29/03/2015.
 */
public class BungeePlayerConnectionWrapper extends BaseConnectionWrapper<ProxiedPlayer> implements DoubleSidedConnectionWrapper<ProxiedPlayer> {

    private ProxySide proxySide;

    public BungeePlayerConnectionWrapper(ProxiedPlayer gateway, ProxySide proxySide) {
        super(gateway);
        this.proxySide = proxySide;
    }

    @Override
    public ProxySide getProxySide() {
        return proxySide;
    }

    @Override
    public void sendCustomPayload(String channel, byte[] data) {
        switch (proxySide) {
            default: throw new UnsupportedOperationException("Don't know how to handle ProxySide: " + proxySide + ".");
            case CLIENT:
                getConnection().sendData(channel, data);
                break;
            case SERVER:
                getConnection().getServer().sendData(channel, data);
                break;
        }
    }
}
