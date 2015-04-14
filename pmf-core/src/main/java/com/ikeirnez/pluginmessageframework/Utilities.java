package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.lang.reflect.Field;

/**
 * Utilities for internal use.
 */
public class Utilities {

    private Utilities() {}

    /**
     * Sets a packet as being received.
     *
     * @param packet the packet to set as being received
     */
    public static void setReceived(Packet packet) {
        try {
            Field field = Packet.class.getDeclaredField("received");
            field.setAccessible(true);
            field.setBoolean(packet, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throws an exception without having it checked.
     *
     * @param ex the exception to throw
     */
    public static void sneakyThrow(Throwable ex) {
        //noinspection ThrowableResultOfMethodCallIgnored
        Utilities.<RuntimeException>sneakyThrowInner(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrowInner(Throwable ex) throws T {
        throw (T) ex;
    }

}
