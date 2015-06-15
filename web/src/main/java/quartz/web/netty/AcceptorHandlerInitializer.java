package quartz.web.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class AcceptorHandlerInitializer extends ChannelInitializer<ServerSocketChannel>{
	
	public static final ChannelHandler loggingHandler = new LoggingHandler(AcceptorHandlerInitializer.class,LogLevel.INFO);
	
	@Override
	protected void initChannel(ServerSocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addFirst(AcceptorHandlerInitializer.loggingHandler);
	}
}
