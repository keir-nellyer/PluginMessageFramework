package com.ikeirnez.pluginmessageframework;

/**
 * Abstract manager of packets, handles the sending of packets and dispatching of received packets.
 */
public abstract class PluginMessageFramework {

    private Gateway gateway;
    protected String channel;

    public PluginMessageFramework(Gateway gateway, String channel) {
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or an empty string.");
        }

        this.gateway = gateway;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    /**
     * Sends the packet on a gateway provided by the {@link Gateway} specified in the constructor.
     *
     * @param packet the packet to send
     * @return true if the packet was sent, false if a gateway couldn't be found
     */
    public boolean sendPacket(Packet packet) {
        ConnectionWrapper connectionWrapper = gateway.getGateway();
        if (connectionWrapper == null) {
            return false;
        }

        sendPacket(connectionWrapper, packet);
        return true;
    }

    public abstract void sendPacket(ConnectionWrapper connectionWrapper, Packet packet);

}
