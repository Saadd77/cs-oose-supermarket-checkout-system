package fr.cs.oose.inventory;

import fr.cs.oose.model.Item;

public class ManagerNotifier implements StockObserver {
    @Override
    public void update(Item item) {
        System.out.println(
                "LOW STOCK ALERT for manager: " + item.getName()
                        + " has only " + item.getStock() + " units left."
        );
    }
}