package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.packet.BasePacket;

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
    public static void setReceived(BasePacket packet) {
        try {
            Field field = BasePacket.class.getDeclaredField("received");
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
    private static <T extends Throwable> void sneakyThrowInner(Throwable ex) throws T {
        throw (T) ex;
    }

}
