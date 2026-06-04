package fr.cs.oose.inventory;

import fr.cs.oose.model.Item;

public interface StockObserver {
    void update(Item item);
}