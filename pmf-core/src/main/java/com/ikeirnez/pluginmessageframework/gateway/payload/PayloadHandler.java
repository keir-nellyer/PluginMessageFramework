package com.ikeirnez.pluginmessageframework.gateway.payload;

import com.ikeirnez.pluginmessageframework.packet.BasePacket;

import java.io.IOException;

/**
 * Handles incoming and outgoing packets, responsible for serializing and de-serializing.
 *
 * @param <P> the Packet type to be handled by this class (e.g. {@link com.ikeirnez.pluginmessageframework.packet.StandardPacket} and {@link com.ikeirnez.pluginmessageframework.packet.RawPacket}.
 */
public interface PayloadHandler<P extends BasePacket> {

    /**
     * Checks if a {@link BasePacket} is able to be handled by this gateway.
     *
     * @param packet the packet in question
     * @return true if this packet can be handled by this gateway, false otherwise
     */
    boolean isPacketApplicable(BasePacket packet);

    /**
     * Checks if a {@link BasePacket} class is able to be handled by this gateway.
     *
     * @param packetClass the packet class in question
     * @return true if this packet class can be handled by this gateway, false otherwise.
     */
    boolean isPacketClassApplicable(Class<? extends BasePacket> packetClass);

    /**
     * Reads an incoming byte array and converts it to {@link BasePacket} form.
     *
     * @param bytes the bytes received
     * @return the packet form of this data
     * @throws IOException thrown if there is an exception whilst reading the data
     */
    P readIncomingPacket(byte[] bytes) throws IOException;

    /**
     * Writes a {@link BasePacket} to a byte array ready for sending.
     *
     * @param packet the packet to convert to a byte array
     * @return the byte array
     * @throws IOException thrown if there is an exception whilst writing the byte array
     */
    byte[] writeOutgoingPacket(P packet) throws IOException;

}
