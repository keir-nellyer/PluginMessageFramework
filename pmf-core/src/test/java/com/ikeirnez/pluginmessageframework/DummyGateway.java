package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;

import java.io.IOException;

/**
 * Dummy gateway for use in testing.
 */
public class DummyGateway extends GatewaySupport<String> {

    public DummyGateway() {
        super("SomeChannel");
    }


    @Override
    public void sendCustomPayload(String connection, String channel, byte[] bytes) throws IOException {}
}
