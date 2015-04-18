package com.ikeirnez.pluginmessageframework.packet;

import com.ikeirnez.pluginmessageframework.PrimaryArgumentProvider;

/**
 * A simple packet implementation of {@link PrimaryValuePacket}.
 * This allows the object contained to be passed straight to the listener method, bypassing the Packet wrapper.
 *
 * @param <T> the class type to be held by this instance
 */
public class PrimaryValuePacket<T> extends StandardPacket implements PrimaryArgumentProvider<T> {

    private static final long serialVersionUID = 2725432363474347054L;

    private final T primaryValue;

    /**
     * Instantiates a new instance.
     *
     * @param primaryValue the primary value held by this packet.
     */
    public PrimaryValuePacket(T primaryValue) {
        this.primaryValue = primaryValue;
    }

    /**
     * Gets the primary value held by this packet.
     *
     * @return the primary value
     */
    public T getValue() {
        return primaryValue;
    }
}
