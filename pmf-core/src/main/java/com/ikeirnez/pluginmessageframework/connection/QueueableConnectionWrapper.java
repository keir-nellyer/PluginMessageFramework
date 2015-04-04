package com.ikeirnez.pluginmessageframework.connection;

/**
 * An extension of {@link ConnectionWrapper} allowing the packet to be queued through a particular gateway.
 */
public interface QueueableConnectionWrapper<T> extends ConnectionWrapper<T> {

    /**
     * Sends a custom payload (aka plugin message) on the specified channel (internal use).
     *
     * @param channel the channel to send the custom payload through
     * @param data the data to send
     * @param queue should the packet be queued if it cannot be sent immediately
     * @return true if the data was sent immediately, false if a gateway couldn't be found and the data has been queued for later (if queue parameter is true)
     */
    boolean sendCustomPayload(String channel, byte[] data, boolean queue);

}
