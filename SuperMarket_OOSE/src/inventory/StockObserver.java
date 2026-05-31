package inventory;

import model.Item;

public interface StockObserver {
    void update(Item item);
}