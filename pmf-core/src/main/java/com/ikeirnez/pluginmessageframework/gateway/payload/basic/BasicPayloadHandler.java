package com.ikeirnez.pluginmessageframework.gateway.payload.basic;

import com.ikeirnez.pluginmessageframework.Utilities;
import com.ikeirnez.pluginmessageframework.gateway.payload.PayloadHandler;
import com.ikeirnez.pluginmessageframework.packet.Packet;
import com.ikeirnez.pluginmessageframework.packet.RawPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic payload handler, sends Packets in a simple form (intended for when the other side isn't using this framework to read the packet)
 * Please note that this {@link PayloadHandler} will <b>only</b> send packets which extend {@link RawPacket}.
 */
public class BasicPayloadHandler implements PayloadHandler<RawPacket> {

    private static final Logger logger = LoggerFactory.getLogger(BasicPayloadHandler.class);

    private Map<String, Executable> subChannelLookup = new HashMap<>();

    @Override
    public boolean isPacketApplicable(Packet packet) {
        return isPacketClassApplicable(packet.getClass());
    }

    @Override
    public boolean isPacketClassApplicable(Class<? extends Packet> packetClass) {
        return RawPacket.class.isAssignableFrom(packetClass);
    }

    public void registerAllIncomingPackets(Collection<Class<? extends RawPacket>> collection) {
        for (Class<? extends RawPacket> clazz : collection) {
            registerIncomingPacket(clazz);
        }
    }

    public void registerIncomingPacket(final Class<? extends RawPacket> clazz) {
        boolean foundExecutable = false;

        @SuppressWarnings("serial")
        List<Executable> list = new ArrayList<Executable>() {
            {
                addAll(Arrays.asList(clazz.getDeclaredConstructors()));
                addAll(Arrays.asList(clazz.getDeclaredMethods()));
            }
        };

        for (Executable executable : list) {
            if (executable.isAnnotationPresent(IncomingHandler.class)
                    && (!(executable instanceof Method)
                    || (Modifier.isStatic(executable.getModifiers())
                    && clazz.isAssignableFrom(((Method) executable).getReturnType())))) {
                IncomingHandler incomingHandler = executable.getAnnotation(IncomingHandler.class);
                String subChannel = incomingHandler.value();

                if (subChannelLookup.containsKey(subChannel)) {
                    if (!subChannelLookup.get(subChannel).equals(executable)) {
                        throw new UnsupportedOperationException("Cannot register multiple constructors to handle sub-channel: \"" + subChannel + "\".");
                    }
                }

                subChannelLookup.put(subChannel, executable);
                foundExecutable = true;
            }
        }

        if (!foundExecutable) {
            throw new IllegalArgumentException("Packet " + clazz.getName() + " doesn't contain at least one " + IncomingHandler.class.getName() + " annotation.");
        }
    }

    @Override
    public RawPacket readIncomingPacket(byte[] data) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));
        String subChannel = dataInputStream.readUTF();
        Executable executable = subChannelLookup.get(subChannel);

        if (executable != null) {
            try {
                Class<?>[] parameterTypes = executable.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameters.length; i++) {
                    Class<?> parameterType = parameterTypes[i];

                    if (String.class.isAssignableFrom(parameterType)) {
                        parameters[i] = dataInputStream.readUTF();
                    } else if (Short.class.isAssignableFrom(parameterType) || short.class.isAssignableFrom(parameterType)) {
                        parameters[i] = dataInputStream.readShort();
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

                executable.setAccessible(true);
                RawPacket rawPacket = (RawPacket) (executable instanceof Constructor
                        ? ((Constructor) executable).newInstance(parameters) :
                        ((Method) executable).invoke(null, parameters));
                Utilities.setReceived(rawPacket);

                return rawPacket;
            } catch (InstantiationException | InvocationTargetException e) {
                Throwable throwable = e.getCause();
                if (throwable == null) {
                    throwable = e;
                }

                Utilities.sneakyThrow(throwable);
            } catch (IllegalAccessException e) {
                logger.error("Error occurred whilst handling incoming payload.", e);
            }
        }

        return null;
    }

    @Override
    public byte[] writeOutgoingPacket(RawPacket packet) throws IOException {
        return writeBytes(packet);
    }

    public static byte[] writeBytes(RawPacket packet) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        packet.writeData(dataOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
