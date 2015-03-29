package com.ikeirnez.pluginmessageframework;

/**
 * Created by Keir on 27/03/2015.
 */
public abstract class Gateway<T> {

    protected final PluginMessageFramework pluginMessageFramework;

    public Gateway(PluginMessageFramework pluginMessageFramework) {
        this.pluginMessageFramework = pluginMessageFramework;
    }

    public abstract ConnectionWrapper<T> getGateway();

    protected final void receive(ConnectionWrapper connectionWrapper, byte[] bytes) {

    }

}
