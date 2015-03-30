package com.ikeirnez.pluginmessageframework;

import java.util.Optional;

/**
 * Created by Keir on 27/03/2015.
 */
public abstract class Gateway<T> {

    protected final PluginMessageFramework pluginMessageFramework;

    public Gateway(PluginMessageFramework pluginMessageFramework) {
        this.pluginMessageFramework = pluginMessageFramework;
    }

    public abstract Optional<ConnectionWrapper<T>> getGateway();

    protected final void receive(ConnectionWrapper connectionWrapper, byte[] bytes) {
        // todo
    }

}
