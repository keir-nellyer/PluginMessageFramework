package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.PrimaryArgumentProvider;
import com.ikeirnez.pluginmessageframework.SneakyThrow;
import com.ikeirnez.pluginmessageframework.gateway.payload.FullPayloadHandler;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.gateway.Gateway;
import com.ikeirnez.pluginmessageframework.packet.PrimaryValuePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

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
            payloadHandler = new FullPayloadHandler();
        }

        return payloadHandler;
    }

    @Override
    public void setPayloadHandler(PayloadHandler payloadHandler) {
        this.payloadHandler = payloadHandler;
    }

    @Override
    public void sendPacket(ConnectionWrapper<T> connectionWrapper, Packet packet) throws IOException {
        connectionWrapper.sendCustomPayload(getChannel(), getPayloadHandler().writeOutgoingPacket(packet));
    }

    protected Object handleListenerParameter(Class<?> clazz, Packet packet, ConnectionWrapper<T> connectionWrapper) { // todo do this better? gets overridden
        if (packet instanceof PrimaryArgumentProvider) {
            Object object = ((PrimaryArgumentProvider) packet).getValue();

            if (clazz.isAssignableFrom(object.getClass())) {
                return object;
            }
        }

        if (Packet.class.isAssignableFrom(clazz)) {
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
                Class<? extends Packet> packetClazz = PrimaryValuePacket.class;

                for (Class<?> parameterType : parameterTypes) { // find packet class
                    if (Packet.class.isAssignableFrom(parameterType)) {
                        packetClazz = (Class<? extends Packet>) parameterType;
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

                            SneakyThrow.sneakyThrow(throwable);
                        }
                    }
                }
            }
        }
    }

}
