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

    private boolean listenerOneWithWrapperFired = false;
    private boolean listenerTwoWithoutWrapperFired = false;

    private String dummyPacketData = "The quick brown fox.";
    private boolean simplePacketListenerFired = false;

    @Before
    public void initialize() {
        gatewaySupport.registerListener(this);
    }

    @Test
    public void testEnumListeners() throws IOException {
        gatewaySupport.incomingPayload(fakeSender, gatewaySupport.writePacket(new PrimaryValuePacket<>(TestEnum.SECOND_VALUE)));
        assertThat("Listener with wrapper failed to invoke.", listenerOneWithWrapperFired, is(true));
        assertThat("Listener without wrapper failed to invoke.", listenerTwoWithoutWrapperFired, is(true));
    }

    @PacketHandler
    public void onEnumPacketWithWrapper(String fakeSender, PrimaryValuePacket<TestEnum> primaryValuePacket) {
        listenerOneWithWrapperFired = true;
        assertThat("Sender in listener doesn't match.", this.fakeSender, is(fakeSender));
        assertThat("Enum value does not match sent value.", primaryValuePacket.getValue(), is(TestEnum.SECOND_VALUE));
    }

    @PacketHandler
    public void onEnumPacket(TestEnum testEnum) {
        listenerTwoWithoutWrapperFired = true;
        assertThat("Enum value does not match sent value.", testEnum, is(TestEnum.SECOND_VALUE));
    }

    @Test
    public void testSimpleListeners() throws IOException {
        gatewaySupport.incomingPayload(fakeSender, gatewaySupport.writePacket(new DummySimplePacket(dummyPacketData)));
        assertThat("Simple listener failed to invoke.", simplePacketListenerFired, is(true));
    }

    @PacketHandler
    public void onSimplePacket(DummySimplePacket dummySimplePacket) {
        simplePacketListenerFired = true;
        assertThat("Packet doesn't contain same data.", dummySimplePacket.getString(), is(this.dummyPacketData));
    }

}
