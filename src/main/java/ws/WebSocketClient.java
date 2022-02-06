package ws;

import utils.OrderBookUtil;
import java.net.URI;

public class WebSocketClient {

    public static final String COINBASE_URI = "wss://ws-feed.exchange.coinbase.com";

    WebSocketClientEndpoint clientEndPoint;

    public void start(String instrument) {
        try {
            clientEndPoint = new WebSocketClientEndpoint(new URI(WebSocketClient.COINBASE_URI));

            WebSocketMessageHandler messageHandler = new WebSocketMessageHandler();
            clientEndPoint.addMessageHandler(messageHandler);

            String subscribeMessage = OrderBookUtil.buildL2SubscribeMessage(instrument);
            clientEndPoint.sendMessage(subscribeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void end() {
        clientEndPoint.close();
    }
}
