package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.internal.GatewaySupport;

/**
 * Dummy gateway for use in testing.
 */
public class DummyGateway extends GatewaySupport<String> {

    public DummyGateway() {
        super("SomeChannel");
    }


    @Override
    public void sendPayload(String connection, byte[] bytes) {}
}
