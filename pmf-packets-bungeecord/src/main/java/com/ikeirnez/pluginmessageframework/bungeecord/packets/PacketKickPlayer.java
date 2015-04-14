package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Kick any player on the proxy.
 */
public class PacketKickPlayer extends RawPacket {

    public static final String TAG = "KickPlayer";

    private String player;
    private String reason;

    /**
     * Creates a new instance.
     *
     * @param player the player to kick
     * @param reason the reason to kick the player with
     */
    public PacketKickPlayer(String player, String reason) {
        super(TAG);

        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        if (reason == null) {
            throw new IllegalArgumentException("Reason cannot be null.");
        }

        this.player = player;
        this.reason = reason;
    }

    /**
     * Gets the player to be kicked.
     *
     * @return the player to be kicked
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the reason the player should be kicked.
     *
     * @return the reason the player should be kicked
     */
    public String getReason() {
        return reason;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(player);
        dataOutputStream.writeUTF(reason);
    }
}
