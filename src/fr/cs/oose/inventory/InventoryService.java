package fr.cs.oose.inventory;

import java.util.ArrayList;
import java.util.List;

import fr.cs.oose.model.Cart;
import fr.cs.oose.model.CartLine;
import fr.cs.oose.model.Item;

public class InventoryService {
    private final List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void decreaseStockAfterSale(Cart cart) {
        for (CartLine line : cart.getLines()) {
            Item item = line.getItem();
            item.decreaseStock(line.getQuantity());
            notifyIfLowStock(item);
        }
    }

    private void notifyIfLowStock(Item item) {
        if (item.isPerishable() && item.isLowStock()) {
            for (StockObserver observer : observers) {
                observer.update(item);
            }
        }
    }
}