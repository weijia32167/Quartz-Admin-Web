package quartz.web.netty;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class HandlerWebsocketFrame {
	
	//private Getway getway = new Getway();
	
	public void handler(WebSocketServerHandshaker handshaker,ChannelHandlerContext ctx, WebSocketFrame frame) {
		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(),(CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (frame instanceof TextWebSocketFrame) {
			String json = ((TextWebSocketFrame) frame).text();
		/*	ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readValue(json, JsonNode.class);
				String code = rootNode.get("code").asText();
				JsonNode content = rootNode.get("content");
				switch(code){
				case "1001":
					getway.getway1001(content,ctx.channel());
					break;
				case "1002":
					getway.getway1002(content,ctx.channel());
					break;
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
			return;
		}
		if(frame instanceof BinaryWebSocketFrame){
			try {
					final FileOutputStream fos = new FileOutputStream("D://123.abc");
					BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame)frame;
					ByteBuf byteBuf = binFrame.content();
					byteBuf.forEachByte(new ByteBufProcessor() {
					
					@Override
					public boolean process(byte value) throws Exception {
						fos.write(value);
						fos.flush();
						return true;
					}
					});
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		}		
	}

	
}
