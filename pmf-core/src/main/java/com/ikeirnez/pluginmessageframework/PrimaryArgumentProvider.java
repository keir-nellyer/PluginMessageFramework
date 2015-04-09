package com.ikeirnez.pluginmessageframework;

/**
 * Provides a value that may be used when invoking a method, bypassing the need to access this value via another class.
 */
public interface PrimaryArgumentProvider<T> {

    /**
     * Gets the primary value held by this instance.
     *
     * @return the primary value
     */
    T getValue();

}
