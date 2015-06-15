package quartz.web.netty;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;




public class HandlerWebsocketUpgrade {
	
	public static final ChannelGroup Web_Socket_ChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public WebSocketServerHandshaker handler(ChannelHandlerContext ctx, FullHttpRequest req) {
		HttpHeaders httpHeaders = req.headers();
		/*Chrom的HTTP请求头是这个*/
		boolean containConnectionUpgrade = httpHeaders.contains(HttpHeaders.Names.CONNECTION, "Upgrade", false);
		/*FireFox的HTTP请求头是这个*/
		containConnectionUpgrade = containConnectionUpgrade || httpHeaders.contains(HttpHeaders.Names.CONNECTION, "keep-alive, Upgrade", false);
		boolean containUpgradeWebsocket = httpHeaders.contains(HttpHeaders.Names.UPGRADE, "websocket", false);
		if(containConnectionUpgrade&&containUpgradeWebsocket){
			WebSocketServerHandshaker handshaker = WebSocketHandshakerUtil.getWebSocketServerHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req).addListener(new GenericFutureListener<ChannelFuture>() {
					@Override
					public void operationComplete(ChannelFuture future)throws Exception {
						if(future.isSuccess()){
							Channel channel = future.channel();	
							Web_Socket_ChannelGroup.add(channel);
					}}
				});
			}
			return handshaker;
		}else{
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
            return null;
		}
	}
	
	private static void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
}
