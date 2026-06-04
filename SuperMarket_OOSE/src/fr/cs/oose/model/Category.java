package fr.cs.oose.model;

public class Category {
    private final String name;
    private double discountPercent;

    public Category(String name) {
        this.name = name;
        this.discountPercent = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("discount must be between 0 and 100");
        }
        this.discountPercent = discountPercent;
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discountPercent / 100.0);
    }
}