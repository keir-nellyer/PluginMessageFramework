package com.ikeirnez.pluginmessageframework.bungeecord.impl;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.connection.ProxySide;
import com.ikeirnez.pluginmessageframework.impl.ProxyGatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
        this.plugin = plugin;

        ProxyServer proxy = plugin.getProxy();
        proxy.registerChannel(getChannel());
        proxy.getPluginManager().registerListener(plugin, this);
    }

    @Override
    public void sendPacket(ProxiedPlayer proxiedPlayer, Packet packet) throws IOException {
        sendPacket(new BungeePlayerConnectionWrapper(proxiedPlayer, getProxySide()), packet);
    }

    @Override
    public boolean sendPacketServer(ServerInfo server, Packet packet) throws IOException {
        return sendPacketServer(new BungeeServerConnectionWrapper(server), packet);
    }

    @Override
    public boolean sendPacketServer(ServerInfo server, Packet packet, boolean queue) throws IOException {
        return sendPacketServer(new BungeeServerConnectionWrapper(server), packet, queue);
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
                    incomingPayload(new BungeePlayerConnectionWrapper(proxiedPlayer, getProxySide()), event.getData());
                } catch (IOException e1) {
                    logger.error("Error handling incoming payload.", event);
                }
            }
        }
    }

    @Override
    protected Object handleListenerParameter(Class<?> clazz, Packet packet, ConnectionWrapper<ProxiedPlayer> connectionWrapper) {
        if (ServerInfo.class.isAssignableFrom(clazz)) { // hack :(
            return connectionWrapper.getConnection().getServer().getInfo();
        } else if (ConnectionWrapper.class.isAssignableFrom(clazz)
                && ServerInfo.class.isAssignableFrom(getGenericTypeClass(clazz, 0))) { // another hack :(
            return new BungeeServerConnectionWrapper(connectionWrapper.getConnection().getServer().getInfo());
        } else {
            return super.handleListenerParameter(clazz, packet, connectionWrapper);
        }
    }

}
