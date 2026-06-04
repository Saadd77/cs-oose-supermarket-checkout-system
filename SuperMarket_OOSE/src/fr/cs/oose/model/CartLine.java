package fr.cs.oose.model;

public class CartLine {
    private final Item item;
    private final int quantity;

    public CartLine(Item item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRawTotal() {
        return item.getUnitPrice() * quantity;
    }

    public double getDiscountedTotal() {
        return item.getCategory().applyDiscount(getRawTotal());
    }
}