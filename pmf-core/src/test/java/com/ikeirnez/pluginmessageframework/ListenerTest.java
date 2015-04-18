package com.ikeirnez.pluginmessageframework;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.packet.PrimaryValuePacket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests various listener functions.
 */
public class ListenerTest {

    public enum TestEnum {
        FIRST_VALUE(5),
        SECOND_VALUE(54954);

        private final int value;

        TestEnum(int value) {
            this.value = value;
        }
    }

    private final GatewaySupport<String> gatewaySupport = new DummyGateway();
    private final String fakeSender = "ThisIsAFakeSender"; // we'll just use a string as being the sender (stupid I know)

    private boolean listenerOneWithWrapper = false;
    private boolean listenerTwoWithoutWrapper = false;

    @Before
    public void initialize() {
        gatewaySupport.registerListener(this);
    }

    @Test
    public void testListener() throws IOException {
        gatewaySupport.incomingPayload(fakeSender, gatewaySupport.writePacket(new PrimaryValuePacket<>(TestEnum.SECOND_VALUE)));
        assertThat("Listener with wrapper failed to invoke.", listenerOneWithWrapper, is(true));
        assertThat("Listener without wrapper failed to invoke.", listenerTwoWithoutWrapper, is(true));
    }

    @PacketHandler
    public void onEnumPacketWithWrapper(String fakeSender, PrimaryValuePacket<TestEnum> primaryValuePacket) {
        listenerOneWithWrapper = true;
        assertThat("Sender in listener doesn't match.", this.fakeSender, is(fakeSender));
        assertThat("Enum value does not match sent value.", primaryValuePacket.getValue(), is(TestEnum.SECOND_VALUE));
    }

    @PacketHandler
    public void onEnumPacket(TestEnum testEnum) {
        listenerTwoWithoutWrapper = true;
        assertThat("Enum value does not match sent value.", testEnum, is(TestEnum.SECOND_VALUE));
    }

}
