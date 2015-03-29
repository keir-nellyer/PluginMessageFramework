package com.ikeirnez.pluginmessageframework;

/**
 * Created by Keir on 27/03/2015.
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
