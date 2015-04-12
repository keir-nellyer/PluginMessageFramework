package com.ikeirnez.pluginmessageframework;

import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.impl.GatewaySupport;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.packet.PrimaryValuePacket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Keir on 09/04/2015.
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

    private GatewaySupport<Object> gatewaySupport = new GatewaySupport<Object>("SomeChannel") {};
    @SuppressWarnings("unchecked") private ConnectionWrapper<Object> connectionWrapper = mock(ConnectionWrapper.class);
    private boolean listenerOneWithWrapper = false, listenerTwoWithoutWrapper = false;

    @Before
    public void initialize() {
        gatewaySupport.registerListener(this);
    }

    @Test
    public void testListener() throws IOException {
        gatewaySupport.incomingPayload(connectionWrapper, gatewaySupport.getPayloadHandler().writeOutgoingPacket(new PrimaryValuePacket<>(TestEnum.SECOND_VALUE)));
        assertThat("Listener with wrapper failed to invoke.", listenerOneWithWrapper, is(true));
        assertThat("Listener without wrapper failed to invoke.", listenerTwoWithoutWrapper, is(true));
    }

    @PacketHandler
    public void onEnumPacketWithWrapper(ConnectionWrapper<Object> connectionWrapper, PrimaryValuePacket<TestEnum> primaryValuePacket) {
        listenerOneWithWrapper = true;
        assertThat("ConnectionWrapper in listener does not match sender ConnectionWrapper.", this.connectionWrapper, is(connectionWrapper));
        assertThat("Enum value does not match sent value.", primaryValuePacket.getValue(), is(TestEnum.SECOND_VALUE));
    }

    @PacketHandler
    public void onEnumPacket(TestEnum testEnum) {
        listenerTwoWithoutWrapper = true;
        assertThat("Enum value does not match sent value.", testEnum, is(TestEnum.SECOND_VALUE));
    }

}
