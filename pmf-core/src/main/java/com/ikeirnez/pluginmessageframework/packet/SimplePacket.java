package com.ikeirnez.pluginmessageframework.packet;

import com.ikeirnez.pluginmessageframework.PrimaryArgumentProvider;

/**
 * A packet which contains a single value (or only 1 important value).
 * This allows the object contained to be passed straight to the listener method, bypassing the Packet wrapper.
 */
public class SimplePacket<T> extends Packet implements PrimaryArgumentProvider<T> {

    private static final long serialVersionUID = 2725432363474347054L;

    private T object;

    public SimplePacket(T object) {
        this.object = object;
    }

    public T getValue() {
        return object;
    }
}
