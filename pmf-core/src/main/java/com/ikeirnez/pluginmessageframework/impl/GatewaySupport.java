package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.connection.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.packet.PacketHandler;
import com.ikeirnez.pluginmessageframework.gateway.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Class which should be extended by implementations.
 * Provides connections and forwards received packets to the framework.
 */
public abstract class GatewaySupport<T> implements Gateway<T> {

    public static final String PACKET_HEADER = "PluginMessageFramework";

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Class<?> type;
    private final String channel;

    private final Map<Class<? extends Packet>, List<Object>> listeners = new HashMap<>();
    private final List<Packet> sendQueue = new ArrayList<>();

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
    public void sendPacket(ConnectionWrapper connectionWrapper, Packet packet) throws IOException {
        connectionWrapper.sendCustomPayload(channel, packet.writeBytes());
    }

    protected Object handleListenerParameter(Class<?> clazz, Packet packet, ConnectionWrapper<T> connectionWrapper) { // todo do this better? gets overridden
        System.out.println("Type: " + type.getName());
        System.out.println("Clazz: " + clazz.getName());

        if (Packet.class.isAssignableFrom(clazz)) {
            return packet;
        } else if (ConnectionWrapper.class.isAssignableFrom(clazz)) {
            return connectionWrapper;
        } else if (type.isAssignableFrom(clazz)) {
            return type.cast(connectionWrapper.getConnection());
        }

        return null;
    }

    @Override
    public void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)) { // todo check parameters too
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<? extends Packet> packetClazz = null;

                for (Class<?> parameterType : parameterTypes) { // find packet class
                    if (Packet.class.isAssignableFrom(parameterType)) {
                        packetClazz = (Class<? extends Packet>) parameterType;
                        break;
                    }
                }

                if (packetClazz != null) {
                    List<Object> list = listeners.get(packetClazz);
                    if (list == null) {
                        list = new ArrayList<>();
                        listeners.put(packetClazz, list);
                    }

                    list.add(listener);
                    break;
                }
            }
        }
    }

    @Override
    public void unregisterListener(Object listener) {
        for (List<Object> list : listeners.values()) {
            list.remove(listener);
        }
    }

    private void dispatchPacketToListeners(ConnectionWrapper<T> connectionWrapper, Packet packet) throws InvocationTargetException, IllegalAccessException {
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
                                continue methodLoop;
                            }
                        }

                        method.invoke(listener, parameters);
                    }
                }
            }
        }
    }

    public final void receivePacket(ConnectionWrapper<T> connectionWrapper, byte[] bytes) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));

            if (objectInputStream.readUTF().equals(PACKET_HEADER)) {
                Object object = objectInputStream.readObject();

                if (Packet.class.isAssignableFrom(object.getClass())){
                    try {
                        dispatchPacketToListeners(connectionWrapper, (Packet) object);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        logger.error("Error occurred whilst dispatching packet to listeners.", e);
                    }
                } else {
                    throw new IllegalArgumentException("Class does not extend Packet.");
                }
            }
        } catch (IOException e) {
            logger.error("Exception whilst reading custom payload.", e);
        } catch (ClassNotFoundException e) {
            logger.debug("Unable to find packet class whilst de-serializing.", e);
        }
    }

}
