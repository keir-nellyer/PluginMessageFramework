package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.Packet;

import java.io.IOException;

/**
 * Dummy gateway for use in testing.
 */
public class DummyGateway extends GatewaySupport<Object> {

    public DummyGateway() {
        super("SomeChannel");
    }

    @Override
    public void sendPacket(Object connection, Packet packet) throws IOException {}
}
