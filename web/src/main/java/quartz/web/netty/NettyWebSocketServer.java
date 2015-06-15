package quartz.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyWebSocketServer{

	private ServerBootstrap bootstrap;
	private EventLoopGroup acceptorGroup;
	private EventLoopGroup workerGroup;
	private ChannelHandler acceptorHandlerInitializer;
	private ChannelHandler workerHandlerInitializer;
	private int port;

	public NettyWebSocketServer(ServerBootstrap bootstrap,
			EventLoopGroup acceptorGroup, EventLoopGroup workerGroup,
			ChannelHandler acceptorHandlerInitializer,
			ChannelHandler workerHandlerInitializer, int port) {
		super();
		this.bootstrap = bootstrap;
		this.acceptorGroup = acceptorGroup;
		this.workerGroup = workerGroup;
		this.acceptorHandlerInitializer = acceptorHandlerInitializer;
		this.workerHandlerInitializer = workerHandlerInitializer;
		this.port = port;
	}

	protected void initSelf() {
		bootstrap.group(acceptorGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.handler(acceptorHandlerInitializer);
		bootstrap.childHandler(workerHandlerInitializer);
	}

	protected void startSelf(){
		bootstrap.bind("192.168.12.109",port).syncUninterruptibly();
	}

}
