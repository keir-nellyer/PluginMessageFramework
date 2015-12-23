package com.ikeirnez.pluginmessageframework.forge;

import com.ikeirnez.pluginmessageframework.gateway.ClientGateway;
import com.ikeirnez.pluginmessageframework.gateway.ServerGateway;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Provides a {@link ClientGateway} capable of running on the Forge implementation.
 */
public class ForgeGatewayProvider {

    /**
     * Gets a new {@link ClientGateway} capable of running on the Forge implementation.
     *
     * @param channel the channel to operate on
     * @return the gateway
     */
    public static ClientGateway getClientGateway(String channel) {
        return new ImplForgeClientGateway(channel);
    }

    /**
     * Gets a new {@link ServerGateway} capable of running on the Forge implementation.
     *
     * @param channel the channel to operate on
     * @return the gateway
     */
    public static ServerGateway<EntityPlayerMP> getServerGateway(String channel) {
        return new ImplForgeServerGateway(channel);
    }

    private ForgeGatewayProvider() {
    }

}
