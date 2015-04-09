package com.ikeirnez.pluginmessageframework.packet;

import com.ikeirnez.pluginmessageframework.PrimaryArgumentProvider;

/**
 * A simple packet implementation of {@link PrimaryValuePacket}.
 * This allows the object contained to be passed straight to the listener method, bypassing the Packet wrapper.
 */
public class PrimaryValuePacket<T> extends Packet implements PrimaryArgumentProvider<T> {

    private static final long serialVersionUID = 2725432363474347054L;

    private T object;

    public PrimaryValuePacket(T object) {
        this.object = object;
    }

    public T getValue() {
        return object;
    }
}
