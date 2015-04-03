package com.ikeirnez.pluginmessageframework;

/**
 * Represents a connection to the "other side".
 * Allows for compatibility through many different implementations via a wrapper-like class..
 */
public interface ConnectionWrapper<T> {

    /**
     * Sends a custom payload (aka plugin message) on the specified channel (internal use).
     *
     * @param channel the channel to send the custom payload through
     * @param data the data to send
     */
    void sendCustomPayload(String channel, byte[] data);

    /**
     * Gets the connection in the form of the underlying implementation.
     *
     * @return the connection
     */
    T getConnection();
}
