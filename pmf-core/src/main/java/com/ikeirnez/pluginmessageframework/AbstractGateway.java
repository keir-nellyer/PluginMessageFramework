package com.ikeirnez.pluginmessageframework;

import java.io.IOException;
import java.util.Optional;

/**
 * Class which should be extended by implementations.
 * Provides connections and forwards received packets to the framework.
 */
public abstract class AbstractGateway<T> {

    protected PluginMessageFramework pluginMessageFramework = null;

    protected void init(PluginMessageFramework pluginMessageFramework) {
        if (this.pluginMessageFramework != null) {
            throw new UnsupportedOperationException("Framework has already been set.");
        }

        this.pluginMessageFramework = pluginMessageFramework;
    }

    public abstract Optional<ConnectionWrapper<T>> getConnection();

    protected final void receivePacket(ConnectionWrapper connectionWrapper, byte[] bytes) {
        try {
            pluginMessageFramework.receivePacket(connectionWrapper, bytes);
        } catch (IOException e) {
            pluginMessageFramework.logger.error("Exception whilst reading custom payload.", e);
        } catch (ClassNotFoundException e) {
            pluginMessageFramework.logger.debug("Unable to find packet class whilst de-serializing.", e);
        }
    }

}
