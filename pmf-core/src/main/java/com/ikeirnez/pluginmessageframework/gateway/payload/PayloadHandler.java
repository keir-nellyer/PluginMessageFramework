package com.ikeirnez.pluginmessageframework.gateway.payload;

import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Handles incoming and outgoing packets, responsible for serializing and deserializing.
 */
public interface PayloadHandler {

    /**
     * Reads an incoming byte array and converts it to {@link Packet} form.
     *
     * @param bytes the bytes received
     * @return the packet form of this data
     * @throws IOException thrown if there is an exception whilst reading the data
     */
    Packet readIncomingPacket(byte[] bytes) throws IOException;

    /**
     * Writes a {@link Packet} to a byte array ready for sending.
     *
     * @param packet the packet to convert to a byte array
     * @return the byte array
     * @throws IOException thrown if there is an exception whilst writing the byte array
     */
    byte[] writeOutgoingPacket(Packet packet) throws IOException;

}
