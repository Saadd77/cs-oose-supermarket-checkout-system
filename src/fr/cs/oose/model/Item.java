package fr.cs.oose.model;

public class Item {
    private final String name;
    private final Category category;
    private final double unitPrice;
    private final double weight;
    private int stock;
    private int lowStockThreshold;

    public Item(String name, Category category, double unitPrice, double weight, int stock) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("unit price cannot be negative");
        }

        if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be negative");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }

        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.weight = weight;
        this.stock = stock;
        this.lowStockThreshold = 5;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getWeight() {
        return weight;
    }

    public int getStock() {
        return stock;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        if (lowStockThreshold < 0) {
            throw new IllegalArgumentException("low-stock threshold cannot be negative");
        }

        this.lowStockThreshold = lowStockThreshold;
    }

    public boolean isLowStock() {
        return stock <= lowStockThreshold;
    }

    public boolean isPerishable() {
        String categoryName = category.getName().toLowerCase();
        return categoryName.equals("meat") || categoryName.equals("dairy");
    }

    public void restock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        stock += quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        if (quantity > stock) {
            throw new IllegalArgumentException("not enough stock for item: " + name);
        }

        stock -= quantity;
    }
}