package ws;

import utils.OrderBookUtil;
import utils.OrderBookUI;
import model.OrderBook;
import model.OrderBookUpdate;

import java.util.List;

public class WebSocketMessageHandler implements MessageHandler {

    OrderBook orderBook;

    public WebSocketMessageHandler() {
        orderBook = null;
    }

    @Override
    public void handleMessage(String message) {
        String responseType = OrderBookUtil.getResponseType(message);

        if (OrderBookUtil.SNAPSHOT.equals(responseType)) {
            orderBook = OrderBookUtil.buildOrderBookFromSnapshot(message);
            List<List<Float>> orderBookNLevels = orderBook.buildOrderBookNLevels();
            OrderBookUI.display(orderBookNLevels.get(0), orderBookNLevels.get(1), orderBookNLevels.get(2), orderBookNLevels.get(3));

        } else if (OrderBookUtil.L2_UPDATE.equals(responseType)) {
            OrderBookUpdate orderBookUpdate = OrderBookUtil.buildOrderBookUpdateFromL2Update(message);
            if (orderBook != null) {
                orderBook.updateOrderBook(orderBookUpdate);
                List<List<Float>> orderBookNLevels = orderBook.buildOrderBookNLevels();
                OrderBookUI.display(orderBookNLevels.get(0), orderBookNLevels.get(1), orderBookNLevels.get(2), orderBookNLevels.get(3));
            }
        }
    }
}
