package com.ikeirnez.pluginmessageframework;

import java.io.*;

/**
 * Base class for creating packets.
 */
public abstract class Packet implements Serializable {

    public final byte[] writeBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeUTF(PluginMessageFramework.PACKET_HEADER);
        objectOutputStream.writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }

}
