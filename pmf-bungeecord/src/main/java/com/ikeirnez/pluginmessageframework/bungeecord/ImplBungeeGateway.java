package com.ikeirnez.pluginmessageframework.bungeecord;

import com.google.common.base.Preconditions;
import com.ikeirnez.pluginmessageframework.gateway.ProxySide;
import com.ikeirnez.pluginmessageframework.internal.ProxyGatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.BasePacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

/**
 * The default BungeeCord implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ProxyGateway}.
 */
public class ImplBungeeGateway extends ProxyGatewaySupport<ProxiedPlayer, Server, ServerInfo> implements Listener {

    protected ImplBungeeGateway(String channel, ProxySide proxySide, Plugin plugin) {
        super(channel, proxySide);

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }

        ProxyServer proxy = plugin.getProxy();
        proxy.registerChannel(getChannel());
        proxy.getPluginManager().registerListener(plugin, this);
    }

    @Override
    public void sendPayload(ProxiedPlayer connection, byte[] bytes) {
        switch (getProxySide()) {
            default:
                throw new UnsupportedOperationException("Don't know how to handle sending payload from the ProxySide: " + getProxySide() + ".");
            case CLIENT:
                connection.sendData(getChannel(), bytes);
                break;
            case SERVER:
                Server server = connection.getServer();
                Preconditions.checkNotNull(server, "Player is not connected to a server.");
                server.sendData(getChannel(), bytes);
                break;
        }
    }

    @Override
    public void sendCustomPayloadServer(Server serverConnection, byte[] bytes) {
        serverConnection.sendData(getChannel(), bytes);
    }

    @Override
    public boolean sendCustomPayloadServer(ServerInfo serverInfo, byte[] bytes, boolean queue) {
        return serverInfo.sendData(getChannel(), bytes, queue);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals(getChannel())) {
            event.setCancelled(true);

            Connection sender = event.getSender();
            Connection receiver = event.getReceiver();
            ProxiedPlayer proxiedPlayer = null;

            if (getProxySide() == ProxySide.CLIENT && sender instanceof ProxiedPlayer) {
                proxiedPlayer = (ProxiedPlayer) sender;
            } else if (getProxySide() == ProxySide.SERVER && receiver instanceof ProxiedPlayer) {
                proxiedPlayer = (ProxiedPlayer) receiver;
            }

            if (proxiedPlayer != null) {
                try {
                    incomingPayload(proxiedPlayer, event.getData());
                } catch (IOException e1) {
                    logger.error("Error handling incoming payload.", event);
                }
            }
        }
    }

    @Override
    protected Object handleListenerParameter(Class<?> clazz, BasePacket packet, ProxiedPlayer proxiedPlayer) {
        if (ServerInfo.class.isAssignableFrom(clazz)) { // hack :(
            return proxiedPlayer.getServer().getInfo();
        } else if (Server.class.isAssignableFrom(clazz)) {
            return proxiedPlayer.getServer();
        } else {
            return super.handleListenerParameter(clazz, packet, proxiedPlayer);
        }
    }

}
