package quartz.web.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WorkerWebSocketHandlerInitializer extends ChannelInitializer<SocketChannel>{
	
	public static final ChannelHandler loggingHandler = new LoggingHandler(WorkerWebSocketHandlerInitializer.class,LogLevel.INFO);
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		/*SelfSignedCertificate ssc = new SelfSignedCertificate();
	    SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		pipeline.addLast("ssl",sslCtx.newHandler(ch.alloc()));*/
		pipeline.addFirst("logger",loggingHandler);
		pipeline.addLast("beatHeart",new IdleStateHandler(60,0,30));
		pipeline.addLast(new HttpServerCodec());
	    pipeline.addLast(new HttpObjectAggregator(65536));
	    pipeline.addLast(new WebSocketServerHandler());
	}

}
