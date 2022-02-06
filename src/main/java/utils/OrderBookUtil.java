package utils;

import model.OrderBook;
import model.Order;
import model.OrderBookUpdate;
import org.apache.logging.log4j.LogManager;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class OrderBookUtil {
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(OrderBookUtil.class);

    public static final String ASKS = "asks";
    public static final String BIDS = "bids";
    public static final String SNAPSHOT = "snapshot";
    public static final String L2_UPDATE = "l2update";
    public static final String CHANGES = "changes";
    public static final String BUY_UPDATE = "buy";
    public static final String SELL_UPDATE = "sell";
    public static final Integer NUMBER_OF_LEVELS = 10;
    private static final JSONParser jsonParser = new JSONParser();

    public static String getResponseType(String response) {
        String responseType = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
            responseType = (String) jsonObject.get("type");
        } catch (ParseException e) {
            LOG.error(e);
        }
        return responseType;
    }

    public static String buildL2SubscribeMessage(String instrument) {
        JSONObject msg = new JSONObject();
        JSONArray productIds = new JSONArray();
        productIds.add(instrument);

        JSONArray channels = new JSONArray();
        channels.add("level2");

        msg.put("type", "subscribe");
        msg.put("product_ids", productIds);
        msg.put("channels", channels);
        return msg.toJSONString();
    }

    /**
     * Build OrderBook from snapshot message
     * @param snapshotMessage snapshot message
     * @return OrderBook(asks, bids)
     */
    public static OrderBook buildOrderBookFromSnapshot(String snapshotMessage) {
        Map<Float, Float> asks = null;
        Map<Float, Float> bids = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(snapshotMessage);
            JSONArray asksArray = (JSONArray) jsonObject.get(OrderBookUtil.ASKS);
            JSONArray bidsArray = (JSONArray) jsonObject.get(OrderBookUtil.BIDS);

            asks = OrderBookUtil.getAllPriceAndSize(asksArray);
            bids = OrderBookUtil.getAllPriceAndSize(bidsArray);
        } catch (Exception e) {
            LOG.error(e);
        }
        return new OrderBook(asks, bids);
    }

    public static OrderBookUpdate buildOrderBookUpdateFromL2Update(String updateResponse) {

        OrderBookUpdate orderBookUpdate = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(updateResponse);
            JSONArray changesList = (JSONArray) jsonObject.get(OrderBookUtil.CHANGES);
            orderBookUpdate = OrderBookUtil.buildOrderBookUpdateFromChangesList(changesList);
        } catch (Exception e) {
            LOG.error(e);
        }
        return orderBookUpdate;
    }

    /**
     * From L2 update response "changes" key, we get an array of [side, price, size],
     * Return OrderBookUpdate object that consists of two collections: buy updates(price, size) & sell updates (price, size).
     * @param jsonArray [side, price, size]
     * @return OrderBookUpdate containing 2 lists: [buyPrice, size] & [sellPrize, size]
     */
    private static OrderBookUpdate buildOrderBookUpdateFromChangesList(JSONArray jsonArray) {
        List<Order> buyUpdates = new ArrayList<>();
        List<Order> sellUpdates = new ArrayList<>();
        for (JSONArray sidePriceSize : (Iterable<JSONArray>) jsonArray) {
            String side = (String) sidePriceSize.get(0);
            float price = Float.parseFloat((String) sidePriceSize.get(1));
            float size = Float.parseFloat((String) sidePriceSize.get(2));
            Order order = new Order(price, size);
            if (OrderBookUtil.BUY_UPDATE.equals(side))
                buyUpdates.add(order);
            else if (OrderBookUtil.SELL_UPDATE.equals(side))
                sellUpdates.add(order);
            else
                LOG.error("Invalid Side: {}", side);
        }
        return new OrderBookUpdate(buyUpdates, sellUpdates);
    }

    /**
     * Extract individual array of [price, size] array into hashmap of {price: size}
     * @param priceAndSizeArray JSONArray of [[price, size], [price, size] ... ]
     * @return {price: size, ...}
     */
    private static Map<Float, Float> getAllPriceAndSize(JSONArray priceAndSizeArray) {

        Map<Float, Float> prices = new HashMap<>();
        for (JSONArray priceAndSize : (Iterable<JSONArray>) priceAndSizeArray) {
            Float price = Float.parseFloat((String) priceAndSize.get(0));
            Float size = Float.parseFloat((String) priceAndSize.get(1));
            prices.put(price, size);
        }
        return prices;
    }
}