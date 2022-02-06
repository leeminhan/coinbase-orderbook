package utils;

import java.util.List;

public class OrderBookUI {

    public static void display(List<Float> nAsksPrices, List<Float> nBidsPrices, List<Float> nAsksSizes, List<Float> nBidsSizes) {
        System.out.println("==================================================");
        System.out.printf("%10s %10s %10s %10s %n", "SIZE","ASKS", "BIDS", "SIZE");
        int depth = Math.max(nAsksPrices.size(), nBidsPrices.size());
        for (int i = 0; i < depth; i ++) {
            System.out.printf("%10.2f %10.2f %10.2f %10.2f %n", nAsksSizes.get(i), nAsksPrices.get(i), nBidsPrices.get(i), nBidsSizes.get(i));
        }
    }
}
