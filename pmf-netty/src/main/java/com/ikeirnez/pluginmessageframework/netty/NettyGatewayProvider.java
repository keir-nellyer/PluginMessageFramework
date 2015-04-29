package com.ikeirnez.pluginmessageframework.netty;

import com.ikeirnez.pluginmessageframework.impl.ServerGatewaySupport;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by Keir on 22/04/2015.
 */
public abstract class NettyGatewayProvider extends ServerGatewaySupport<Channel> {

    protected final EventLoopGroup parentGroup = new NioEventLoopGroup();

    public NettyGatewayProvider() {
        super();
    }

    @Override
    public void sendPayload(Channel connection, String channel, byte[] bytes) {
        connection.writeAndFlush(bytes);
    }

}
