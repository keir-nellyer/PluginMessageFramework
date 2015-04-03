package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;

import java.io.*;

/**
 * Base class for creating packets.
 * Extending classes must make sure that all fields are serializable and any fields which <b>should not</b> be serialized
 * are marked as <b>volatile</b>
 */
public abstract class Packet implements Serializable {

    /**
     * Serializes the instance and then converts it to {@link Byte} array.
     *
     * @return the serialized class in byte form
     * @throws IOException thrown if there is an error whilst serializing the instance
     */
    public final byte[] writeBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeUTF(GatewaySupport.PACKET_HEADER);
        objectOutputStream.writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }

}
