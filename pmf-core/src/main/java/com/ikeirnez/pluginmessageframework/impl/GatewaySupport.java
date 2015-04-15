package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.PrimaryArgumentProvider;
import com.ikeirnez.pluginmessageframework.Utilities;
import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.gateway.Gateway;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.StandardPayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.packet.PrimaryValuePacket;
import com.ikeirnez.pluginmessageframework.packet.StandardPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which should be extended by implementations.
 * Provides connections and forwards received packets to the framework.
 */
public abstract class GatewaySupport<T> implements Gateway<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Class<?> type;
    private final String channel;
    private PayloadHandler payloadHandler = null;

    private final Map<Class<? extends Packet>, List<Object>> listeners = new HashMap<>();

    public GatewaySupport(String channel) {
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or an empty string.");
        }

        this.channel = channel;
        this.type = getGenericTypeClass(getClass(), 0);
    }

    protected static Class<?> getGenericTypeClass(Class<?> clazz, int index) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index];
    }

    @Override
    public final String getChannel() {
        return channel;
    }

    @Override
    public final PayloadHandler getPayloadHandler() {
        if (payloadHandler == null) {
            payloadHandler = new StandardPayloadHandler();
        }

        return payloadHandler;
    }

    @Override
    public void setPayloadHandler(PayloadHandler payloadHandler) {
        this.payloadHandler = payloadHandler;
    }

    /**
     * Helper method, checks if packet is applicable (if not an exception is thrown) and then returns the packet in a byte[] form.
     *
     * @param packet the packet to write bytes for
     * @return the byte[] representation of the packet
     * @throws IOException thrown if there is an exception whilst writing the packet
     */
    @SuppressWarnings("unchecked")
    public byte[] writePacket(Packet packet) throws IOException {
        if (!getPayloadHandler().isPacketApplicable(packet)) {
            throw new IllegalArgumentException("Assigned PayloadHandler cannot handle this type of Packet.");
        }

        return getPayloadHandler().writeOutgoingPacket(packet);
    }

    @Override
    public void sendPacket(ConnectionWrapper<T> connectionWrapper, Packet packet) throws IOException {
        connectionWrapper.sendCustomPayload(getChannel(), writePacket(packet));
    }

    protected Object handleListenerParameter(Class<?> clazz, Packet packet, ConnectionWrapper<T> connectionWrapper) {
        // todo do this better? gets overridden
        if (packet instanceof PrimaryArgumentProvider) {
            Object object = ((PrimaryArgumentProvider) packet).getValue();

            if (clazz.isAssignableFrom(object.getClass())) {
                return object;
            }
        }

        if (StandardPacket.class.isAssignableFrom(clazz)) {
            return packet;
        }

        if (ConnectionWrapper.class.isAssignableFrom(clazz)) {
            return connectionWrapper;
        }

        if (type.isAssignableFrom(clazz)) {
            return type.cast(connectionWrapper.getValue());
        }

        return null;
    }

    public void incomingPayload(ConnectionWrapper<T> connectionWrapper, byte[] data) throws IOException {
        Packet packet = getPayloadHandler().readIncomingPacket(data);
        receivePacket(connectionWrapper, packet);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)) { // todo check parameters too
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<? extends StandardPacket> packetClazz = PrimaryValuePacket.class;

                for (Class<?> parameterType : parameterTypes) { // find packet class
                    if (StandardPacket.class.isAssignableFrom(parameterType)) {
                        packetClazz = (Class<? extends StandardPacket>) parameterType;
                        break;
                    }
                }

                List<Object> list = listeners.get(packetClazz);
                if (list == null) {
                    list = new ArrayList<>();
                    listeners.put(packetClazz, list);
                }

                list.add(listener);
            }
        }
    }

    @Override
    public void unregisterListener(Object listener) {
        for (List<Object> list : listeners.values()) {
            list.remove(listener);
        }
    }

    @Override
    public void receivePacket(ConnectionWrapper<T> connectionWrapper, Packet packet) {
        Class<? extends Packet> packetClass = packet.getClass();

        if (listeners.containsKey(packetClass)) {
            for (Object listener : listeners.get(packetClass)) {
                methodLoop: for (Method method : listener.getClass().getMethods()) {
                    if (method.isAnnotationPresent(PacketHandler.class)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] parameters = new Object[parameterTypes.length];

                        for (int i = 0; i < parameters.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            Object parameter = handleListenerParameter(parameterType, packet, connectionWrapper);

                            if (parameter != null) {
                                parameters[i] = parameter;
                            } else {
                                System.out.println("Param " + parameterType + " is null");
                                continue methodLoop;
                            }
                        }

                        try {
                            method.invoke(listener, parameters);
                        } catch (IllegalAccessException e) {
                            logger.error("Error occurred whilst dispatching packet to listeners.", e);
                        } catch (InvocationTargetException e) {
                            Throwable throwable = e.getCause();
                            if (throwable == null) {
                                throwable = e;
                            }

                            Utilities.sneakyThrow(throwable);
                        }
                    }
                }
            }
        }
    }

}
