package model;

import java.util.List;

public class OrderBookUpdate {
    private final List<Order> buyUpdates;
    private final List<Order> sellUpdates;

    public OrderBookUpdate(List<Order> buyUpdates, List<Order> sellUpdates) {
        this.buyUpdates = buyUpdates;
        this.sellUpdates = sellUpdates;
    }

    public List<Order> getBuyUpdates() {
        return buyUpdates;
    }

    public List<Order> getSellUpdates() {
        return sellUpdates;
    }
}