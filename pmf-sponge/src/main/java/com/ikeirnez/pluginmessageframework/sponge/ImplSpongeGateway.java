package com.ikeirnez.pluginmessageframework.sponge;

import com.ikeirnez.pluginmessageframework.internal.ServerGatewaySupport;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.*;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.io.IOException;
import java.util.Collection;

/**
 * The default Sponge implementation of a {@link com.ikeirnez.pluginmessageframework.gateway.ServerGateway}.
 */
public class ImplSpongeGateway extends ServerGatewaySupport<Player> implements MessageHandler<SpongeByteArrayMessage> {

    private final Object plugin;
    private final Game game;

    private final ChannelBinding.IndexedMessageChannel dataChannel;

    protected ImplSpongeGateway(String channel, final Object plugin) {
        super(channel);

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }

        this.plugin = plugin;
        this.game = Sponge.getGame();

        this.dataChannel = game.getChannelRegistrar().createChannel(plugin, getChannel());
        this.dataChannel.registerMessage(SpongeByteArrayMessage.class, 1452, this);
        game.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public void sendPayload(Player connection, byte[] bytes) {
        this.dataChannel.sendTo(connection, new SpongeByteArrayMessage(bytes));
    }

    @Override
    public Player getConnection() {
        Collection<Player> players = game.getServer().getOnlinePlayers();
        return players.size() > 0 ? players.iterator().next() : null;
    }

    @Override
    @NonnullByDefault
    public void handleMessage(SpongeByteArrayMessage message, RemoteConnection connection, Platform.Type side) {
        if (connection instanceof PlayerConnection) {
            Player player = ((PlayerConnection) connection).getPlayer();
            byte[] bytes = message.getBytes();

            try {
                incomingPayload(player, bytes);
            } catch(IOException e) {
                logger.error("Error handling incoming payload.", e);
            }
        }
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        if (queuedPackets()) {
            // todo does this need delayed like in DefaultBukkitGateway?
            sendQueuedPackets(event.getTargetEntity());
        }
    }
}
