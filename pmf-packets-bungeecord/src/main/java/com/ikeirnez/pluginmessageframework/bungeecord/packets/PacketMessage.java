package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Send a message (as in, a chat message) to the specified player.
 */
public class PacketMessage extends RawPacket {

    public static final String TAG = "Message";

    private final String player;
    private final String message;

    /**
     * Creates a new instance.
     *
     * @param player the player to send the message to
     * @param message the chat message to send to the player
     */
    public PacketMessage(String player, String message) {
        super(TAG);

        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }

        this.player = player;
        this.message = message;
    }

    /**
     * Gets the player to send the message to.
     *
     * @return the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the message to send the player.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(player);
        dataOutputStream.writeUTF(message);
    }
}
