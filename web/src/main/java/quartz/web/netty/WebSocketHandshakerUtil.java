package quartz.web.netty;

import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public final class WebSocketHandshakerUtil {
	
	private static final String WEBSOCKET_PATH = "/websocket";
	
	public static WebSocketServerHandshaker getWebSocketServerHandshaker(FullHttpRequest req){
		String location = "ws://" + req.headers().get(HOST) + WEBSOCKET_PATH;
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(location, null, false);
		WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
		return handshaker;
	}
	
}
