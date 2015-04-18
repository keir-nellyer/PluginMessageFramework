package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;

import java.io.IOException;

/**
 * Represents a 2-way connection between 2 systems.
 *
 * @param <C> the client connection type
 */
public interface Gateway<C> {

    /**
     * The channel the packets should be sent through.
     * Should be unique and identical for both sides.
     *
     * @return the channel that packets should be sent through
     */
    String getChannel();

    /**
     * Sends a packet via the defined connection.
     *
     * @param connection the connection to send the packet via
     * @param packet the packet to be sent
     * @throws IOException thrown if there is an error sending the packet (usually in the serializing stage)
     */
    void sendPacket(C connection, Packet packet) throws IOException;

    /**
     * <p>Registers a listener to receive incoming packets.</p>
     *
     * <p>Listener methods must be</p>
     * <ul>
     *     <li>annotated with a {@link PacketHandler}</li>
     *     <li><b>OPTIONAL</b> parameter - &lt;T&gt;</li>
     *     <li>parameter - object extending {@link Packet}</li>
     * </ul>
     *
     * @param listener the listener to be registered
     */
    void registerListener(Object listener);

    /**
     * Unregisters a listener, preventing any further received packets from being dispatched to the instance.
     *
     * @param listener the listener to be unregistered
     */
    void unregisterListener(Object listener);

    /**
     * Dispatches a {@link Packet} to listeners.
     * Mainly for internal use.
     *
     * @param connection the connection the packet was received from
     * @param packet the packet received
     */
    void receivePacket(C connection, Packet packet);

    /**
     * Gets the payload handler in use by this gateway.
     *
     * @return the payload handler
     */
    PayloadHandler getPayloadHandler();

    /**
     * Sets the payload handler to be used by this gateway.
     *
     * @param payloadHandler the payload handler
     */
    void setPayloadHandler(PayloadHandler payloadHandler);
}
