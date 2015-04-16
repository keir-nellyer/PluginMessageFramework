package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.packet.StandardPacket;

import java.io.IOException;

/**
 * Represents a 2-way connection between 2 systems.
 */
public interface Gateway<T> {

    /**
     * The channel the packets should be sent through.
     * Should be unique and identical for both sides.
     *
     * @return the channel that packets should be sent through
     */
    String getChannel();

    /**
     * Sends a packet via the defined {@link ConnectionWrapper}.
     *
     * @param connectionWrapper the connection to send the packet through
     * @param packet the packet to be sent
     * @throws IOException thrown if there is an error sending the packet (usually in the serializing stage)
     */
    void sendPacket(ConnectionWrapper<T> connectionWrapper, Packet packet) throws IOException;

    /**
     * Sends a packet via the defined connection.
     *
     * @param connection the connection to send the packet via
     * @param packet the packet to be sent
     * @throws IOException thrown if there is an error sending the packet (usually in the serializing stage)
     */
    void sendPacket(T connection, Packet packet) throws IOException;

    /**
     * <p>Registers a listener to receive incoming packets.</p>
     *
     * <p>Listener methods must be</p>
     * <ul>
     *     <li>annotated with a {@link PacketHandler}</li>
     *     <li><b>OPTIONAL</b> parameter - {@link ConnectionWrapper}&lt;T&gt; <b>OR</b> simple &lt;T&gt;</li>
     *     <li>parameter - object extending {@link StandardPacket}</li>
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
     * @param connectionWrapper the connection the packet was received from
     * @param packet the packet received
     */
    void receivePacket(ConnectionWrapper<T> connectionWrapper, Packet packet);

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
