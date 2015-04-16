package com.ikeirnez.pluginmessageframework.bungeecord.packets;

import com.ikeirnez.pluginmessageframework.gateway.payload.basic.IncomingHandler;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Request the UUID of any player connected to the BungeeCord proxy.
 */
public class PacketUUID extends RawPacket {

    public static final String TAG = "UUID";
    public static final String TAG_OTHER = "UUIDOther";

    @IncomingHandler(TAG)
    private static PacketUUID createInstance(String uuid) { // work-around multiple constructors with same args
        PacketUUID packetUUID = new PacketUUID(null);
        packetUUID.uuid = UUID.fromString(uuid);
        return packetUUID;
    }

    private String player;
    private UUID uuid;

    /**
     * Creates a new instance getting the UUID of the sender of this packet.
     */
    public PacketUUID() {
        this(null);
    }

    /**
     * Creates a new instance getting the UUID of the defined player.
     *
     * @param player the player to get the UUID of (or null to get the UUID of the sender of the packet).
     */
    public PacketUUID(String player) {
        super(player == null ? TAG : TAG_OTHER);
    }

    @IncomingHandler(TAG_OTHER)
    private PacketUUID(String player, String uuid) {
        super(TAG);
        this.player = player;
        this.uuid = UUID.fromString(uuid);
    }

    /**
     * Gets the player the {@link UUID} belongs to.
     * Player may be null when received, in which case assume the sender of the packet is the owner of the uuid.
     *
     * @return the name of the player the uuid belongs to
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the UUID of said player.
     *
     * @return the UUID
     */
    public UUID getUUID() {
        throwExceptionIfAttemptingReadBeforeReceived();
        return uuid;
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        super.writeData(dataOutputStream);
        dataOutputStream.writeUTF(player);
    }
}
