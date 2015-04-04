package com.ikeirnez.pluginmessageframework.connection;

/**
 * Represents a connection which has 2 sides (e.g. in BungeeCord a ProxiedPlayer has the server side and the client side).
 */
public interface DoubleSidedConnectionWrapper<T> extends ConnectionWrapper<T> {

    /**
     * Gets the side this connection is attached to.
     *
     * @return the side this connection is attached to
     */
    ProxySide getProxySide();

}
