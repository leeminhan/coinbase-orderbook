package model;

public class Order {
    private final Float price;
    private final Float size;

    public Order(Float price, Float size) {
        this.price = price;
        this.size = size;
    }

    public float getPrice() {
        return this.price;
    }

    public float getSize() {
        return this.size;
    }
}