package com.ikeirnez.pluginmessageframework;

/**
 * A small utility class to sneakily throw exceptions without having them checked.
 */
public class SneakyThrow {

    private SneakyThrow() {}

    /**
     * Throws an exception without having it checked.
     *
     * @param ex the exception to throw
     */
    public static void sneakyThrow(Throwable ex) {
        //noinspection ThrowableResultOfMethodCallIgnored
        SneakyThrow.<RuntimeException>sneakyThrowInner(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrowInner(Throwable ex) throws T {
        throw (T) ex;
    }

}
