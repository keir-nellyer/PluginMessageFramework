package com.ikeirnez.pluginmessageframework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Keir on 23/04/2015.
 */
public class NettyServerGatewayProvider extends NettyGatewayProvider {

    protected final ServerBootstrap serverBootstrap = new ServerBootstrap();
    protected final EventLoopGroup childGroup = new NioEventLoopGroup();

    private final int port;

    public NettyServerGatewayProvider(int port) {
        super();
        this.port = port;

        serverBootstrap.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                // todo child handler here
                .localAddress("", port);
    }

    @Override
    public Channel getConnection() {
        return null;
    }
}
