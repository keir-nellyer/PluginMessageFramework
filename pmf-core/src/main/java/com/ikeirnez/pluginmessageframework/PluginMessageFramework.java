package com.ikeirnez.pluginmessageframework;

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
 * Abstract manager of packets, handles the sending of packets and dispatching of received packets.
 */
public class PluginMessageFramework {

    public static final String PACKET_HEADER = "PluginMessageFramework";

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Class<?> type;
    private final AbstractGateway gateway;
    private final String channel;

    private final Map<Class<? extends Packet>, List<Object>> listeners = new HashMap<>();

    public PluginMessageFramework(AbstractGateway gateway, String channel) {
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or an empty string.");
        }

        this.gateway = gateway;
        this.channel = channel;
        this.type = (Class<?>) ((ParameterizedType) gateway.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.gateway.init(this);
    }

    public String getChannel() {
        return channel;
    }

    /**
     * Sends the packet on a gateway provided by the {@link AbstractGateway} specified in the constructor.
     *
     * @param packet the packet to send
     * @return true if the packet was sent, false if a gateway couldn't be found
     * @throws IOException thrown if there is an error whilst serializing the packet
     */
    public boolean sendPacket(Packet packet) throws IOException {
        Optional<ConnectionWrapper> optional = gateway.getConnection();
        if (optional.isPresent()) {
            return false;
        }

        sendPacket(optional.get(), packet);
        return true;
    }

    public void sendPacket(ConnectionWrapper connectionWrapper, Packet packet) throws IOException {
        connectionWrapper.send(channel, packet.writeBytes());
    }

    protected void receivePacket(ConnectionWrapper connectionWrapper, byte[] bytes) throws IOException, ClassNotFoundException {
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
    }

    private boolean isParametersApplicable(Class<? extends Packet> clazz, Class<?>[] parameters) {
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
                        || ConnectionWrapper.class.isAssignableFrom(connectionParam));
    }

    public void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            if (isParametersApplicable(Packet.class, parameterTypes)) {
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

    public void unregisterListener(Object listener) {
        for (List<Object> list : listeners.values()) {
            list.remove(listener);
        }
    }

    private void dispatchPacketToListeners(ConnectionWrapper connectionWrapper, Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Object listener : listeners.get(packet.getClass())) {
            for (Method method : listener.getClass().getMethods()) {
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (isParametersApplicable(packet.getClass(), parameterTypes)) {
                    Object[] parameters = new Object[parameterTypes.length];

                    if (parameters.length == 1) {
                        parameters[0] = packet;
                    } else {
                        parameters[0] = type.isAssignableFrom(parameterTypes[0]) ? connectionWrapper.getGateway() : connectionWrapper;
                        parameters[1] = packet;
                    }

                    method.invoke(packet, parameters);
                }
            }
        }
    }

}
