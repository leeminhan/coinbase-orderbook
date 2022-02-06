package model;

import utils.OrderBookUtil;
import java.util.*;

public class OrderBook {

    private final SortedMap<Float, Float> asks;
    private final SortedMap<Float, Float> bids;

    public OrderBook(Map<Float, Float> asksPrizeSize, Map<Float, Float> bidsPrizeSize) {
        this.asks = new TreeMap<>(asksPrizeSize);
        this.bids = new TreeMap<>(Collections.reverseOrder());
        bids.putAll(bidsPrizeSize);
    }

    public void updateOrderBook(OrderBookUpdate orderBookUpdate) {
        List<Order> buyUpdates = orderBookUpdate.getBuyUpdates();
        for (Order order : buyUpdates) {
            if (order.getSize() == 0) {
                bids.remove(order.getPrice());
            } else {
                bids.put(order.getPrice(), order.getSize());
            }
        }

        List<Order> sellUpdates = orderBookUpdate.getSellUpdates();
        for (Order order : sellUpdates) {
            if (order.getSize() == 0) {
                asks.remove(order.getPrice());
            } else {
                asks.put(order.getPrice(), order.getSize());
            }
        }
    }

    /**
     * Build OrderBook of N levels of prices & size from OrderBook initially built from snapshot
     * @return A list of 4 lists: [nAsksPrice, nBidsPrice, nAsksSize, nBidsSize]
     */
    public List<List<Float>> buildOrderBookNLevels() {
        List<Float> nAsksPrice = getNPrices(this.asks);
        List<Float> nAsksSize = getNSizes(this.asks);

        List<Float> nBidsPrice = getNPrices(this.bids);
        List<Float> nBidsSize = getNSizes(this.bids);
        return Arrays.asList(nAsksPrice, nBidsPrice, nAsksSize, nBidsSize);
    }

    private List<Float> getNPrices(SortedMap<Float, Float> priceSizeMap) {
        List<Float> result = new ArrayList<>();
        Iterator<Float> iterator = priceSizeMap.keySet().iterator();
        int level = 0;
        while(iterator.hasNext()) {
            float price = iterator.next();
            result.add(price);
            level ++;
            if (level >= OrderBookUtil.NUMBER_OF_LEVELS)
                break;
        }
        return result;
    }

    private List<Float> getNSizes(SortedMap<Float, Float> priceSizeMap) {
        List<Float> nSizes = new ArrayList<>();
        Iterator<Float> iterator = priceSizeMap.keySet().iterator();
        int level = 0;
        while(iterator.hasNext()) {
            float price = iterator.next();
            float size = priceSizeMap.get(price);
            nSizes.add(size);
            level ++;
            if (level >= OrderBookUtil.NUMBER_OF_LEVELS)
                break;
        }
        return nSizes;
    }
}

