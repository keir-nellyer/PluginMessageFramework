package com.ikeirnez.pluginmessageframework;

/**
 * Provides a value that may be used when invoking a method, bypassing the need to access this value via another class.
 *
 * @param <T> the class type to be held by this instance
 */
public interface PrimaryArgumentProvider<T> {

    /**
     * Gets the primary value held by this instance.
     *
     * @return the primary value
     */
    T getValue();

}
