package quartz.web.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;


/**
 *  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-------+-+-------------+-------------------------------+
 |F|R|R|R| opcode|M| Payload len |    Extended payload length    |
 |I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
 |N|V|V|V|       |S|             |   (if payload len==126/127)   |
 | |1|2|3|       |K|             |                               |
 +-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
 |     Extended payload length continued, if payload len == 127  |
 + - - - - - - - - - - - - - - - +-------------------------------+
 |                               |Masking-key, if MASK set to 1  |
 +-------------------------------+-------------------------------+
 | Masking-key (continued)       |          Payload Data         |
 +-------------------------------- - - - - - - - - - - - - - - - +
 :                     Payload Data continued ...                :
 + - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
 |                     Payload Data continued ...                |
 +---------------------------------------------------------------+
 *
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	
	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//static final Map<User,Channel> allUser = new ConcurrentHashMap<>();
	
	private WebSocketServerHandshaker handshaker = null;
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg)throws Exception {
		if (msg instanceof FullHttpRequest) {
			handshaker = new HandlerWebsocketUpgrade().handler(ctx,(FullHttpRequest)msg);
		} else if (msg instanceof WebSocketFrame) {
			new HandlerWebsocketFrame().handler(handshaker,ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
		if(evt instanceof SslHandshakeCompletionEvent){
			SslHandshakeCompletionEvent event = (SslHandshakeCompletionEvent)evt;
		}else{
			IdleStateEvent event = (IdleStateEvent)evt;
			IdleState state = event.state();
			if(state.equals(IdleState.READER_IDLE)){
				ctx.channel().closeFuture().syncUninterruptibly();
			}else if(state.equals(IdleState.WRITER_IDLE)){
				
			}else{
				ctx.channel().writeAndFlush(new PingWebSocketFrame()).syncUninterruptibly();
			}
		}
	}


}
