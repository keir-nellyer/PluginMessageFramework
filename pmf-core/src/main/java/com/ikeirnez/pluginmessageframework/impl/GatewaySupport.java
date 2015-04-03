package com.ikeirnez.pluginmessageframework.impl;

import com.ikeirnez.pluginmessageframework.Packet;
import com.ikeirnez.pluginmessageframework.ConnectionWrapper;
import com.ikeirnez.pluginmessageframework.PacketHandler;
import com.ikeirnez.pluginmessageframework.Gateway;
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
    private final Class<?> type;
    private final String channel;

    private final Map<Class<? extends Packet>, List<Object>> listeners = new HashMap<>();
    private final List<Packet> sendQueue = new ArrayList<>();

    public GatewaySupport(String channel) {
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or an empty string.");
        }

        this.channel = channel;
        this.type = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public final String getChannel() {
        return channel;
    }

    @Override
    public void sendPacket(ConnectionWrapper connectionWrapper, Packet packet) throws IOException {
        connectionWrapper.sendCustomPayload(channel, packet.writeBytes());
    }

    private boolean isParametersApplicable(Class<? extends Packet> clazz, Method method) {
        if (!method.isAnnotationPresent(PacketHandler.class)) return false;

        Class<?>[] parameters =  method.getParameterTypes();
        Class<?> packetParam = null;
        Class<?> connectionParam = null;

        if (parameters.length == 1) {
            packetParam = parameters[0];
        } else if (parameters.length == 2) {
            connectionParam = parameters[0];
            packetParam = parameters[1];
        }

        return packetParam != null &&
                clazz.isAssignableFrom(packetParam) &&
                (connectionParam == null || type.isAssignableFrom(connectionParam)
                        || BaseConnectionWrapper.class.isAssignableFrom(connectionParam));
    }

    @Override
    public void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (isParametersApplicable(Packet.class, method)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<? extends Packet> packetClazz = (Class<? extends Packet>) (parameterTypes.length == 1 ? parameterTypes[0] : parameterTypes[1]);
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

    private void dispatchPacketToListeners(BaseConnectionWrapper connectionWrapper, Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Object listener : listeners.get(packet.getClass())) {
            for (Method method : listener.getClass().getMethods()) {
                if (isParametersApplicable(packet.getClass(), method)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];

                    if (parameters.length == 1) {
                        parameters[0] = packet;
                    } else {
                        parameters[0] = type.isAssignableFrom(parameterTypes[0]) ? connectionWrapper.getConnection() : connectionWrapper;
                        parameters[1] = packet;
                    }

                    method.invoke(packet, parameters);
                }
            }
        }
    }

    protected final void receivePacket(BaseConnectionWrapper connectionWrapper, byte[] bytes) {
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
