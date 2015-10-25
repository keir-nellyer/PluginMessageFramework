package com.ikeirnez.pluginmessageframework.sponge;

import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.Message;
import org.spongepowered.api.util.annotation.NonnullByDefault;

/**
 * Handles reading and writing of byte arrays on the Sponge platform.
 */
public class SpongeByteArrayMessage implements Message {

    private byte[] bytes;

    public SpongeByteArrayMessage(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    @NonnullByDefault
    public void readFrom(ChannelBuf buf) {
        this.bytes = buf.array();
    }

    @Override
    @NonnullByDefault
    public void writeTo(ChannelBuf buf) {
        for (byte b : bytes) {
            buf.writeByte(b);
        }
    }
}
