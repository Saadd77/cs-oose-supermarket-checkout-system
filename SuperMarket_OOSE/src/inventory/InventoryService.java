package inventory;

import model.Cart;
import model.CartLine;
import model.Item;

import java.util.ArrayList;
import java.util.List;

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