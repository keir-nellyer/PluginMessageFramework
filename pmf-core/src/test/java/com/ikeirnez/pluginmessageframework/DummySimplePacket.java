package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.packet.StandardPacket;

/**
 * Dummy packet for use in testing.
 */
public class DummySimplePacket extends StandardPacket {

    private static final long serialVersionUID = -5072261878422453241L;

    private String string;

    public DummySimplePacket(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
