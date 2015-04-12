package com.ikeirnez.pluginmessageframework.packet;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a packet whilst handles it's own serialization and deserialization.
 */
public abstract class RawPacket extends Packet {

    private static final long serialVersionUID = -5263728666023041777L;

    private transient String subChannel;

    /**
     * Instantiates a new instance.
     *
     * @param subChannel the sub-channel this packet will be sent through
     */
    public RawPacket(String subChannel) {
        this.subChannel = subChannel;
    }

    /**
     * Writes the instances data to be sent to the other side.
     * Classes overriding this method should make sure to call the super method first.
     *
     * @param dataOutputStream the stream to write data to
     * @throws IOException thrown if there is an exception whilst writing the stream
     */
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(subChannel);
    }

    /**
     * Helper method for writing a byte array to an output stream.
     *
     * @param dataOutputStream the output stream to write the byte array to
     * @param bytes the byte array to write
     * @throws IOException thrown if there is an exception whilst writing the byte array
     */
    public final void writeByteArray(DataOutputStream dataOutputStream, byte[] bytes) throws IOException {
        dataOutputStream.writeShort(bytes.length);
        dataOutputStream.write(bytes);
    }
}
