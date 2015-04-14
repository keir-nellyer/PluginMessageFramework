package com.ikeirnez.pluginmessageframework.gateway;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.StandardPacket;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;

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
     * Registers a listener to receive incoming packets.
     *
     * Listener methods must be
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

    void receivePacket(ConnectionWrapper<T> connectionWrapper, Packet packet);

    PayloadHandler getPayloadHandler();

    void setPayloadHandler(PayloadHandler payloadHandler);
}
