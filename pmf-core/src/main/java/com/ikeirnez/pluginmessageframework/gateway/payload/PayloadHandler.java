package com.ikeirnez.pluginmessageframework.gateway.payload;

import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Handles incoming and outgoing packets, responsible for serializing and deserializing.
 */
public interface PayloadHandler<T extends Packet> {

    /**
     * Checks if a {@link Packet} is able to be handled by this gateway.
     *
     * @param packet the packet in question
     * @return true if this packet can be handled by this gateway, false otherwise
     */
    boolean isPacketApplicable(Packet packet);

    /**
     * Checks if a {@link Packet} class is able to be handled by this gateway.
     *
     * @param packetClass the packet class in question
     * @return true if this packet class can be handled by this gateway, false otherwise.
     */
    boolean isPacketClassApplicable(Class<? extends Packet> packetClass);

    /**
     * Reads an incoming byte array and converts it to {@link Packet} form.
     *
     * @param bytes the bytes received
     * @return the packet form of this data
     * @throws IOException thrown if there is an exception whilst reading the data
     */
    T readIncomingPacket(byte[] bytes) throws IOException;

    /**
     * Writes a {@link Packet} to a byte array ready for sending.
     *
     * @param packet the packet to convert to a byte array
     * @return the byte array
     * @throws IOException thrown if there is an exception whilst writing the byte array
     */
    byte[] writeOutgoingPacket(T packet) throws IOException;

}
