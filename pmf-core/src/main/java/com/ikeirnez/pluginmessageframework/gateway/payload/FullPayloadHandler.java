package com.ikeirnez.pluginmessageframework.gateway.payload;

import com.ikeirnez.pluginmessageframework.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The default {@link PayloadHandler}, intended for when the packet will be interpreted by this framework on the other side.
 * Adds a header to the packet and uses default Java object serialization/de-serialization.
 */
public class FullPayloadHandler implements PayloadHandler {

    public static final String PACKET_HEADER = "PluginMessageFramework";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Packet readIncomingPacket(byte[] bytes) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));

            if (objectInputStream.readUTF().equals(PACKET_HEADER)) {
                Object object = objectInputStream.readObject();

                if (Packet.class.isAssignableFrom(object.getClass())) {
                    return (Packet) object;
                } else {
                    throw new IllegalArgumentException("Class does not extend Packet.");
                }
            }
        } catch (IOException e) {
            logger.error("Exception whilst reading custom payload.", e);
        } catch (ClassNotFoundException e) {
            logger.debug("Unable to find packet class whilst de-serializing.", e);
        }

        return null;
    }

    @Override
    public byte[] writeOutgoingPacket(Packet packet) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeUTF(PACKET_HEADER);
        objectOutputStream.writeObject(packet);
        return byteArrayOutputStream.toByteArray();
    }
}
