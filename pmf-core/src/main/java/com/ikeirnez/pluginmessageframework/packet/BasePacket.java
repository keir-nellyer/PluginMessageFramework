package com.ikeirnez.pluginmessageframework.packet;

/**
 * Base class for all packets.
 */
public abstract class BasePacket {

    @SuppressWarnings({"CanBeFinal", "FieldCanBeLocal"})
    private boolean received = false;

    /**
     * Gets the received status.
     *
     * @return true if the packet has been received from an external source, false otherwise
     */
    public boolean hasBeenReceived() {
        return received;
    }

    /**
     * Used to throw an exception if an attempt is made to access data which hasn't been populated yet.
     */
    protected void throwExceptionIfAttemptingReadBeforeReceived() { // todo this is a horrible method name
        if (!hasBeenReceived()) {
            throw new UnsupportedOperationException("This packet has not been received and therefore the data being accessed hasn't yet been populated.");
        }
    }

}
