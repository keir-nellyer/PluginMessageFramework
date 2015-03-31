package com.ikeirnez.pluginmessageframework;

/**
 * Wrapper class for connections.
 * Allows compatibility with many implementations through a simple wrapper class.
 */
public abstract class ConnectionWrapper<T> {

    private T gateway;

    public ConnectionWrapper(T gateway) {
        this.gateway = gateway;
    }

    protected abstract void send(String channel, byte[] data);

    public T getGateway() {
        return gateway;
    }

}
