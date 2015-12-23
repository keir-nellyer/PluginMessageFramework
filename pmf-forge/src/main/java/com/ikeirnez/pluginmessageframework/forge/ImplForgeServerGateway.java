package com.ikeirnez.pluginmessageframework.forge;

import com.ikeirnez.pluginmessageframework.internal.ServerGatewaySupport;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;
import java.util.List;

/**
 * The default Forge implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ServerGateway}.
 */
public class ImplForgeServerGateway extends ServerGatewaySupport<EntityPlayerMP> {

    FMLEventChannel channel;

    protected ImplForgeServerGateway(String channelName) {
        super(channelName);
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        channel.register(this);
    }

    @Override
    public void sendPayload(EntityPlayerMP connection, byte[] bytes) {
        this.channel.sendTo(new FMLProxyPacket(new PacketBuffer(Unpooled.wrappedBuffer(bytes)), getChannel()), connection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityPlayerMP getConnection() {
        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        return players.size() > 0 ? players.iterator().next() : null;
    }

    @SubscribeEvent
    public void onPacketEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {
        EntityPlayerMP client = ((NetHandlerPlayServer) event.handler).playerEntity;
        if (event.packet.channel().equals(getChannel())) {
            try {
                incomingPayload(client, event.packet.payload().array());
            } catch (IOException e) {
                logger.error("Error handling incoming payload.", e);
            }
        }
    }

}
