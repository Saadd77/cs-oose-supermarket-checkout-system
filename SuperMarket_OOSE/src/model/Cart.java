package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartLine> lines = new ArrayList<>();

    public void addItem(Item item, int quantity) {
        if (quantity > item.getStock()) {
            throw new IllegalArgumentException("not enough stock for item: " + item.getName());
        }

        lines.add(new CartLine(item, quantity));
    }

    public double computeTotal() {
        double total = 0.0;

        for (CartLine line : lines) {
            total += line.getDiscountedTotal();
        }

        return total;
    }

    public double computeRawTotal() {
        double total = 0.0;

        for (CartLine line : lines) {
            total += line.getRawTotal();
        }

        return total;
    }

    public List<CartLine> getLines() {
        return lines;
    }
}