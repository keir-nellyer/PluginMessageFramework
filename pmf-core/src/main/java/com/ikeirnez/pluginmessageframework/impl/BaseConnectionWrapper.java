package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;

/**
 * Implementation of {@link ConnectionWrapper}.
 */
public abstract class BaseConnectionWrapper<T> implements ConnectionWrapper<T> {

    private T gateway;

    public BaseConnectionWrapper(T gateway) {
        this.gateway = gateway;
    }

    @Override
    public T getConnection() {
        return gateway;
    }

}
