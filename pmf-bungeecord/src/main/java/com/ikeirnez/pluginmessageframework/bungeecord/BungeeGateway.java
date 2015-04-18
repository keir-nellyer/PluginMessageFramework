package com.ikeirnez.pluginmessageframework.bungeecord;

import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.impl.ProxyGatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;
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
public class BungeeGateway extends ProxyGatewaySupport<ProxiedPlayer, ServerInfo> implements Listener {

    private final Plugin plugin;

    public BungeeGateway(String channel, ProxySide proxySide, Plugin plugin) {
        super(channel, proxySide);

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }

        this.plugin = plugin;

        ProxyServer proxy = plugin.getProxy();
        proxy.registerChannel(getChannel());
        proxy.getPluginManager().registerListener(plugin, this);
    }

    @Override
    public void sendCustomPayload(ProxiedPlayer connection, String channel, byte[] bytes) {
        connection.sendData(channel, bytes);
    }

    @Override
    public boolean sendCustomPayloadServer(ServerInfo serverConnection, String channel, byte[] bytes, boolean queue) {
        return serverConnection.sendData(channel, bytes, queue);
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
    protected Object handleListenerParameter(Class<?> clazz, Packet packet, ProxiedPlayer proxiedPlayer) {
        if (ServerInfo.class.isAssignableFrom(clazz)) { // hack :(
            return proxiedPlayer.getServer().getInfo();
        } else if (Server.class.isAssignableFrom(clazz)) {
            return proxiedPlayer.getServer();
        } else {
            return super.handleListenerParameter(clazz, packet, proxiedPlayer);
        }
    }

}
