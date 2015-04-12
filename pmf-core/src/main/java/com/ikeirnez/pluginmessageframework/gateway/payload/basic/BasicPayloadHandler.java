package com.ikeirnez.pluginmessageframework.gateway.payload.basic;

import com.ikeirnez.pluginmessageframework.SneakyThrow;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic payload handler, sends Packets in a simple form (intended for when the other side isn't using this framework to read the packet)
 * Please note that this {@link PayloadHandler} will <b>only</b> send packets which extend {@link RawPacket}.
 */
public class BasicPayloadHandler implements PayloadHandler {

    private static final Logger logger = LoggerFactory.getLogger(BasicPayloadHandler.class);

    private Map<String, Class<? extends Packet>> subChannelLookup = new HashMap<>();

    public void registerAllSubChannels(Map<String, Class<? extends Packet>> map) {
        subChannelLookup.putAll(map);
    }

    public void registerSubChannel(String subChannel, Class<? extends Packet> clazz) {
        subChannelLookup.put(subChannel, clazz);
    }

    @Override
    public Packet readIncomingPacket(byte[] data) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));
        String subChannel = dataInputStream.readUTF();

        Class<? extends Packet> clazz = subChannelLookup.get(subChannel);

        if (clazz != null) {
            try {
                for (Constructor constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.isAnnotationPresent(IncomingHandler.class)) {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameters = new Object[parameterTypes.length];

                        for (int i = 0; i < parameters.length; i++) {
                            Class<?> parameterType = parameterTypes[i];

                            if (String.class.isAssignableFrom(parameterType)) {
                                parameters[i] = dataInputStream.readUTF();
                            } else if (Integer.class.isAssignableFrom(parameterType) || int.class.isAssignableFrom(parameterType)) { // todo util method
                                parameters[i] = dataInputStream.readInt();
                            } else if (Byte[].class.isAssignableFrom(parameterType) || byte[].class.isAssignableFrom(parameterType)) { // todo util method
                                byte[] bytes = new byte[dataInputStream.readShort()];
                                dataInputStream.readFully(bytes);
                                parameters[i] = bytes;
                            } else {
                                throw new IllegalArgumentException("Don't know how to handle parameter type: " + parameterType.getName());
                            }
                        }

                        constructor.setAccessible(true);
                        return (Packet) constructor.newInstance(parameters);
                    }
                }

                throw new IllegalArgumentException("Class " + clazz.getName() + " doesn't have a constructor annotated with " + IncomingHandler.class);
            } catch (InstantiationException | InvocationTargetException e) {
                Throwable throwable = e.getCause();
                if (throwable == null) {
                    throwable = e;
                }

                SneakyThrow.sneakyThrow(throwable);
            } catch (IllegalAccessException e) {
                logger.error("Error occurred whilst handling incoming payload.", e);
            }
        }

        return null;
    }

    @Override
    public byte[] writeOutgoingPacket(Packet packet) throws IOException {
        return writeBytes(packet);
    }

    private static boolean isApplicableFieldModifiers(int modifiers) {
        return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
    }

    public static byte[] writeBytes(Packet packet) throws IOException {
        if (packet instanceof RawPacket) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            RawPacket rawPacket = (RawPacket) packet;
            rawPacket.writeData(dataOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new UnsupportedOperationException("Unable to send packet which doesn't extend " + RawPacket.class.getName());
        }
    }
}
