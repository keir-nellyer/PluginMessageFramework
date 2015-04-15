package com.ikeirnez.pluginmessageframework.gateway.payload;

import com.ikeirnez.pluginmessageframework.Utilities;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.StandardPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The default {@link PayloadHandler}, intended for when the packet will be interpreted by this framework on the other side.
 * Adds a header to the packet and uses default Java object serialization/de-serialization.
 */
public class StandardPayloadHandler implements PayloadHandler<StandardPacket> {

    public static final String PACKET_HEADER = "PluginMessageFramework";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean isPacketApplicable(Packet packet) {
        return isPacketClassApplicable(packet.getClass());
    }

    @Override
    public boolean isPacketClassApplicable(Class<? extends Packet> packetClass) {
        return StandardPacket.class.isAssignableFrom(packetClass);
    }

    @Override
    public StandardPacket readIncomingPacket(byte[] bytes) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));

            if (objectInputStream.readUTF().equals(PACKET_HEADER)) {
                Object object = objectInputStream.readObject();

                if (StandardPacket.class.isAssignableFrom(object.getClass())) {
                    StandardPacket standardPacket = (StandardPacket) object;
                    Utilities.setReceived(standardPacket);
                    return standardPacket;
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
    public byte[] writeOutgoingPacket(StandardPacket standardPacket) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeUTF(PACKET_HEADER);
        objectOutputStream.writeObject(standardPacket);
        return byteArrayOutputStream.toByteArray();
    }
}
